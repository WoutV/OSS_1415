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
package org.sonar.batch.scan.filesystem;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.sonar.api.scan.filesystem.FileSystemFilter;

import java.io.File;
import java.io.IOException;

public class FileFilterWrapperTest {

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  @Test
  public void should_wrap_java_io_filefilter() throws IOException {
    IOFileFilter filter = Mockito.mock(IOFileFilter.class);
    FileFilterWrapper wrapper = new FileFilterWrapper(filter);

    File file = temp.newFile();
    wrapper.accept(file, Mockito.mock(FileSystemFilter.Context.class));

    Mockito.verify(filter).accept(file);
  }
}
