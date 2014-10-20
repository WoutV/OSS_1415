/*
 * SonarQube Java
 * Copyright (C) 2012 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.java.checks;

import org.sonar.squidbridge.checks.CheckMessagesVerifierRule;
import org.junit.Rule;
import org.junit.Test;
import org.sonar.java.JavaAstScanner;
import org.sonar.squidbridge.api.SourceFile;

import java.io.File;

public class SwitchCaseTooBigCheckTest {

  @Rule
  public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

  @Test
  public void detected() {
    SourceFile file = JavaAstScanner.scanSingleFile(new File("src/test/files/checks/SwitchCaseTooBigCheck.java"), new SwitchCaseTooBigCheck());
    checkMessagesVerifier.verify(file.getCheckMessages())
        .next().atLine(9).withMessage("Reduce this switch case number of lines from 6 to at most 5, for example by extracting code into methods.")
        .next().atLine(15).withMessage("Reduce this switch case number of lines from 6 to at most 5, for example by extracting code into methods.")
        .next().atLine(21).withMessage("Reduce this switch case number of lines from 7 to at most 5, for example by extracting code into methods.")
        .next().atLine(29)
        .next().atLine(43);
  }

  @Test
  public void custom() {
    SwitchCaseTooBigCheck check = new SwitchCaseTooBigCheck();
    check.max = 6;

    SourceFile file = JavaAstScanner.scanSingleFile(new File("src/test/files/checks/SwitchCaseTooBigCheck.java"), check);
    checkMessagesVerifier.verify(file.getCheckMessages())
        .next().atLine(21).withMessage("Reduce this switch case number of lines from 7 to at most 6, for example by extracting code into methods.");
  }

  @Test
  public void limit() {
    SwitchCaseTooBigCheck check = new SwitchCaseTooBigCheck();
    check.max = 0;

    SourceFile file = JavaAstScanner.scanSingleFile(new File("src/test/files/checks/SwitchCaseTooBigCheck.java"), check);
    checkMessagesVerifier.verify(file.getCheckMessages())
        .next().atLine(4)
        .next().atLine(9)
        .next().atLine(15)
        .next().atLine(21)
        .next().atLine(28)
        .next().atLine(29)
        .next().atLine(38)
        .next().atLine(43)
        .next().atLine(52)
        .next().atLine(52);
  }

}