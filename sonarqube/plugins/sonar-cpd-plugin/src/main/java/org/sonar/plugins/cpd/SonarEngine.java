/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2013 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.sonar.plugins.cpd;

import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.CoreProperties;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.database.model.ResourceModel;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.resources.*;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.utils.SonarException;
import org.sonar.duplications.block.Block;
import org.sonar.duplications.block.BlockChunker;
import org.sonar.duplications.detector.suffixtree.SuffixTreeCloneDetectionAlgorithm;
import org.sonar.duplications.index.CloneGroup;
import org.sonar.duplications.index.CloneIndex;
import org.sonar.duplications.index.ClonePart;
import org.sonar.duplications.java.JavaStatementBuilder;
import org.sonar.duplications.java.JavaTokenProducer;
import org.sonar.duplications.statement.Statement;
import org.sonar.duplications.statement.StatementChunker;
import org.sonar.duplications.token.TokenChunker;
import org.sonar.plugins.cpd.index.IndexFactory;
import org.sonar.plugins.cpd.index.SonarDuplicationsIndex;

import javax.annotation.Nullable;

import java.io.File;
import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class SonarEngine extends CpdEngine {

  private static final Logger LOG = LoggerFactory.getLogger(SonarEngine.class);

  private static final int BLOCK_SIZE = 10;

  /**
   * Limit of time to analyse one file (in seconds).
   */
  private static final int TIMEOUT = 5 * 60;

  private final IndexFactory indexFactory;
  private final ModuleFileSystem fileSystem;
  private final PathResolver pathResolver;
  private final Settings settings;

  public SonarEngine(IndexFactory indexFactory, ModuleFileSystem moduleFileSystem, PathResolver pathResolver, Settings settings) {
    this.indexFactory = indexFactory;
    this.fileSystem = moduleFileSystem;
    this.pathResolver = pathResolver;
    this.settings = settings;
  }

  @Override
  public boolean isLanguageSupported(Language language) {
    return Java.KEY.equals(language.getKey());
  }

  static String getFullKey(Project project, Resource<?> resource) {
    return new StringBuilder(ResourceModel.KEY_SIZE)
        .append(project.getKey())
        .append(':')
        .append(resource.getKey())
        .toString();
  }

  @Override
  public void analyse(Project project, SensorContext context) {
    String[] cpdExclusions = settings.getStringArray(CoreProperties.CPD_EXCLUSIONS);
    logExclusions(cpdExclusions, LOG);
    List<File> sourceFiles = fileSystem.files(FileQuery.onSource().onLanguage(project.getLanguageKey()).withExclusions(cpdExclusions));
    if (sourceFiles.isEmpty()) {
      return;
    }
    SonarDuplicationsIndex index = createIndex(project, sourceFiles);
    detect(index, context, project, sourceFiles);
  }

  private SonarDuplicationsIndex createIndex(Project project, List<File> sourceFiles) {
    final SonarDuplicationsIndex index = indexFactory.create(project);

    TokenChunker tokenChunker = JavaTokenProducer.build();
    StatementChunker statementChunker = JavaStatementBuilder.build();
    BlockChunker blockChunker = new BlockChunker(BLOCK_SIZE);

    for (File file : sourceFiles) {
      LOG.debug("Populating index from {}", file);
      Resource<?> resource = getResource(file);
      String resourceKey = getFullKey(project, resource);

      List<Statement> statements;

      Reader reader = null;
      try {
        reader = new InputStreamReader(new FileInputStream(file), fileSystem.sourceCharset());
        statements = statementChunker.chunk(tokenChunker.chunk(reader));
      } catch (FileNotFoundException e) {
        throw new SonarException("Cannot find file "+ file, e);
      } finally {
        IOUtils.closeQuietly(reader);
      }

      List<Block> blocks = blockChunker.chunk(resourceKey, statements);
      index.insert(resource, blocks);
    }

    return index;
  }

  private void detect(SonarDuplicationsIndex index, SensorContext context, Project project, List<File> sourceFiles) {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    try {
      for (File file : sourceFiles) {
        LOG.debug("Detection of duplications for {}", file);
        Resource<?> resource = getResource(file);
        String resourceKey = getFullKey(project, resource);

        Collection<Block> fileBlocks = index.getByResource(resource, resourceKey);

        List<CloneGroup> clones;
        try {
          clones = executorService.submit(new Task(index, fileBlocks)).get(TIMEOUT, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
          clones = null;
          LOG.warn("Timeout during detection of duplications for " + file, e);
        } catch (InterruptedException e) {
          throw new SonarException("Fail during detection of duplication for "+ file, e);
        } catch (ExecutionException e) {
          throw new SonarException("Fail during detection of duplication for "+ file, e);
        }

        save(context, resource, clones);
      }
    } finally {
      executorService.shutdown();
    }
  }

  static class Task implements Callable<List<CloneGroup>> {
    private final CloneIndex index;
    private final Collection<Block> fileBlocks;

    public Task(CloneIndex index, Collection<Block> fileBlocks) {
      this.index = index;
      this.fileBlocks = fileBlocks;
    }

    public List<CloneGroup> call() {
      return SuffixTreeCloneDetectionAlgorithm.detect(index, fileBlocks);
    }
  }

  protected Resource<?> getResource(File file) {
    String relativePath = pathResolver.relativePath(fileSystem.sourceDirs(), file).path();
    return JavaFile.fromRelativePath(relativePath, false);
  }

  static void save(SensorContext context, Resource<?> resource, @Nullable Iterable<CloneGroup> duplications) {
    if (duplications == null || Iterables.isEmpty(duplications)) {
      return;
    }
    // Calculate number of lines and blocks
    Set<Integer> duplicatedLines = new HashSet<Integer>();
    double duplicatedBlocks = 0;
    for (CloneGroup clone : duplications) {
      ClonePart origin = clone.getOriginPart();
      for (ClonePart part : clone.getCloneParts()) {
        if (part.getResourceId().equals(origin.getResourceId())) {
          duplicatedBlocks++;
          for (int duplicatedLine = part.getStartLine(); duplicatedLine < part.getStartLine() + part.getLines(); duplicatedLine++) {
            duplicatedLines.add(duplicatedLine);
          }
        }
      }
    }
    // Save
    context.saveMeasure(resource, CoreMetrics.DUPLICATED_FILES, 1.0);
    context.saveMeasure(resource, CoreMetrics.DUPLICATED_LINES, (double) duplicatedLines.size());
    context.saveMeasure(resource, CoreMetrics.DUPLICATED_BLOCKS, duplicatedBlocks);

    Measure data = new Measure(CoreMetrics.DUPLICATIONS_DATA, toXml(duplications))
        .setPersistenceMode(PersistenceMode.DATABASE);
    context.saveMeasure(resource, data);
  }

  private static String toXml(Iterable<CloneGroup> duplications) {
    StringBuilder xml = new StringBuilder();
    xml.append("<duplications>");
    for (CloneGroup duplication : duplications) {
      xml.append("<g>");
      for (ClonePart part : duplication.getCloneParts()) {
        xml.append("<b s=\"").append(part.getStartLine())
            .append("\" l=\"").append(part.getLines())
            .append("\" r=\"").append(StringEscapeUtils.escapeXml(part.getResourceId()))
            .append("\"/>");
      }
      xml.append("</g>");
    }
    xml.append("</duplications>");
    return xml.toString();
  }

}
