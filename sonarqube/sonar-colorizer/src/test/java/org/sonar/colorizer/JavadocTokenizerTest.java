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
package org.sonar.colorizer;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.sonar.colorizer.SyntaxHighlighterTestingHarness.highlight;

import org.junit.Test;

public class JavadocTokenizerTest {

  JavadocTokenizer tokenizer = new JavadocTokenizer("<j>", "</j>");

  @Test
  public void testHighlighting() {
    assertThat(highlight("/**this is a javadoc*/ public ...", tokenizer), is("<j>/**this is a javadoc*/</j> public ..."));
    assertThat(highlight("//this is not a javadoc", tokenizer), is("//this is not a javadoc"));
  }

  @Test
  public void testHighlightingOnMultipleLines() {
    assertThat(highlight("/**this is \n a javadoc*/ private", tokenizer), is("<j>/**this is </j>\n<j> a javadoc*/</j> private"));
  }
}
