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
package org.sonar.core.dryrun;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sonar.api.platform.ServerFileSystem;
import org.sonar.core.persistence.DryRunDatabaseFactory;
import org.sonar.core.properties.PropertiesDao;
import org.sonar.core.properties.PropertyDto;
import org.sonar.core.resource.ResourceDao;
import org.sonar.core.resource.ResourceDto;

import java.io.File;
import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DryRunCacheTest {

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  private DryRunCache dryRunCache;
  private ServerFileSystem serverFileSystem;
  private PropertiesDao propertiesDao;
  private ResourceDao resourceDao;

  private DryRunDatabaseFactory dryRunDatabaseFactory;

  private File dryRunCacheLocation;

  @Before
  public void prepare() throws IOException {
    serverFileSystem = mock(ServerFileSystem.class);
    propertiesDao = mock(PropertiesDao.class);
    resourceDao = mock(ResourceDao.class);
    dryRunDatabaseFactory = mock(DryRunDatabaseFactory.class);

    File tempLocation = temp.newFolder();
    when(serverFileSystem.getTempDir()).thenReturn(tempLocation);
    dryRunCacheLocation = new File(tempLocation, "dryRun");

    dryRunCache = new DryRunCache(serverFileSystem, propertiesDao, resourceDao, dryRunDatabaseFactory);
  }

  @Test
  public void test_getDatabaseForDryRun_on_new_project() throws Exception {
    when(dryRunDatabaseFactory.createNewDatabaseForDryRun(isNull(Long.class), any(File.class), anyString())).thenAnswer(new Answer<File>() {
      public File answer(InvocationOnMock invocation) throws IOException {
        Object[] args = invocation.getArguments();
        File dbFile = new File(new File(dryRunCacheLocation, "default"), (String) args[2] + ".h2.db");
        FileUtils.write(dbFile, "fake db content");
        return dbFile;
      }
    });
    byte[] dbContent = dryRunCache.getDatabaseForDryRun(null);
    assertThat(new String(dbContent)).isEqualTo("fake db content");

    dbContent = dryRunCache.getDatabaseForDryRun(null);
    assertThat(new String(dbContent)).isEqualTo("fake db content");

    verify(dryRunDatabaseFactory, times(1)).createNewDatabaseForDryRun(anyLong(), any(File.class), anyString());
  }

  @Test
  public void test_getDatabaseForDryRun_on_existing_project() throws Exception {
    when(dryRunDatabaseFactory.createNewDatabaseForDryRun(eq(123L), any(File.class), anyString())).thenAnswer(new Answer<File>() {
      public File answer(InvocationOnMock invocation) throws IOException {
        Object[] args = invocation.getArguments();
        File dbFile = new File(new File(dryRunCacheLocation, "123"), (String) args[2] + ".h2.db");
        FileUtils.write(dbFile, "fake db content");
        return dbFile;
      }
    });
    when(resourceDao.getRootProjectByComponentId(123L)).thenReturn(new ResourceDto().setId(123L));
    byte[] dbContent = dryRunCache.getDatabaseForDryRun(123L);
    assertThat(new String(dbContent)).isEqualTo("fake db content");

    dbContent = dryRunCache.getDatabaseForDryRun(123L);
    assertThat(new String(dbContent)).isEqualTo("fake db content");

    verify(dryRunDatabaseFactory, times(1)).createNewDatabaseForDryRun(anyLong(), any(File.class), anyString());
  }

  @Test
  public void test_getDatabaseForDryRun_global_invalidation() throws Exception {
    when(dryRunDatabaseFactory.createNewDatabaseForDryRun(isNull(Long.class), any(File.class), anyString()))
      .thenAnswer(new Answer<File>() {
        public File answer(InvocationOnMock invocation) throws IOException {
          Object[] args = invocation.getArguments();
          File dbFile = new File(new File(dryRunCacheLocation, "default"), (String) args[2] + ".h2.db");
          FileUtils.write(dbFile, "fake db content 1");
          return dbFile;
        }
      })
      .thenAnswer(new Answer<File>() {
        public File answer(InvocationOnMock invocation) throws IOException {
          Object[] args = invocation.getArguments();
          File dbFile = new File(new File(dryRunCacheLocation, "default"), (String) args[2] + ".h2.db");
          FileUtils.write(dbFile, "fake db content 2");
          return dbFile;
        }
      });
    byte[] dbContent = dryRunCache.getDatabaseForDryRun(null);
    assertThat(new String(dbContent)).isEqualTo("fake db content 1");

    // Emulate invalidation of cache
    Thread.sleep(100);
    when(propertiesDao.selectGlobalProperty(DryRunCache.SONAR_DRY_RUN_CACHE_LAST_UPDATE_KEY)).thenReturn(new PropertyDto().setValue("" + System.currentTimeMillis()));

    dbContent = dryRunCache.getDatabaseForDryRun(null);
    assertThat(new String(dbContent)).isEqualTo("fake db content 2");

    verify(dryRunDatabaseFactory, times(2)).createNewDatabaseForDryRun(anyLong(), any(File.class), anyString());
  }

  @Test
  public void test_getDatabaseForDryRun_project_invalidation() throws Exception {
    when(dryRunDatabaseFactory.createNewDatabaseForDryRun(eq(123L), any(File.class), anyString()))
      .thenAnswer(new Answer<File>() {
        public File answer(InvocationOnMock invocation) throws IOException {
          Object[] args = invocation.getArguments();
          File dbFile = new File(new File(dryRunCacheLocation, "123"), (String) args[2] + ".h2.db");
          FileUtils.write(dbFile, "fake db content 1");
          return dbFile;
        }
      })
      .thenAnswer(new Answer<File>() {
        public File answer(InvocationOnMock invocation) throws IOException {
          Object[] args = invocation.getArguments();
          File dbFile = new File(new File(dryRunCacheLocation, "123"), (String) args[2] + ".h2.db");
          FileUtils.write(dbFile, "fake db content 2");
          return dbFile;
        }
      });
    when(resourceDao.getRootProjectByComponentId(123L)).thenReturn(new ResourceDto().setId(123L));

    byte[] dbContent = dryRunCache.getDatabaseForDryRun(123L);
    assertThat(new String(dbContent)).isEqualTo("fake db content 1");

    // Emulate invalidation of cache
    Thread.sleep(100);
    when(propertiesDao.selectProjectProperty(123L, DryRunCache.SONAR_DRY_RUN_CACHE_LAST_UPDATE_KEY)).thenReturn(new PropertyDto().setValue("" + System.currentTimeMillis()));

    dbContent = dryRunCache.getDatabaseForDryRun(123L);
    assertThat(new String(dbContent)).isEqualTo("fake db content 2");

    verify(dryRunDatabaseFactory, times(2)).createNewDatabaseForDryRun(anyLong(), any(File.class), anyString());
  }

  @Test
  public void test_get_cache_location() throws Exception {
    File tempFolder = temp.newFolder();
    when(serverFileSystem.getTempDir()).thenReturn(tempFolder);

    assertThat(dryRunCache.getCacheLocation(null)).isEqualTo(new File(new File(tempFolder, "dryRun"), "default"));
    assertThat(dryRunCache.getCacheLocation(123L)).isEqualTo(new File(new File(tempFolder, "dryRun"), "123"));
  }

  @Test
  public void test_clean_all() throws Exception {
    File tempFolder = temp.newFolder();
    when(serverFileSystem.getTempDir()).thenReturn(tempFolder);
    File cacheLocation = dryRunCache.getCacheLocation(null);
    FileUtils.forceMkdir(cacheLocation);

    dryRunCache.cleanAll();
    verify(propertiesDao).deleteAllProperties(DryRunCache.SONAR_DRY_RUN_CACHE_LAST_UPDATE_KEY);

    assertThat(cacheLocation).doesNotExist();
  }

  @Test
  public void test_report_global_modification() {
    dryRunCache.reportGlobalModification();

    verify(propertiesDao).setProperty(
      new PropertyDto()
        .setKey(DryRunCache.SONAR_DRY_RUN_CACHE_LAST_UPDATE_KEY)
        .setValue(anyString()));
  }

  @Test
  public void test_report_resource_modification() {
    when(resourceDao.getRootProjectByComponentKey("foo")).thenReturn(new ResourceDto().setId(456L));

    dryRunCache.reportResourceModification("foo");

    verify(propertiesDao).setProperty(
      new PropertyDto()
        .setKey(DryRunCache.SONAR_DRY_RUN_CACHE_LAST_UPDATE_KEY)
        .setValue(anyString())
        .setResourceId(456L));
  }
}
