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

import com.sonar.sslr.api.AstNode;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.java.ast.api.JavaKeyword;
import org.sonar.java.ast.parser.JavaGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S00121",
  priority = Priority.MAJOR,
  tags={"convention"})
@BelongsToProfile(title = "Sonar way", priority = Priority.MAJOR)
public class MissingCurlyBraces_S00121_Check extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(
      JavaGrammar.IF_STATEMENT,
      JavaGrammar.FOR_STATEMENT,
      JavaGrammar.WHILE_STATEMENT,
      JavaGrammar.DO_STATEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    AstNode statement = astNode.getFirstChild(JavaGrammar.STATEMENT);
    if (!statement.getFirstChild().is(JavaGrammar.BLOCK)) {
      getContext().createLineViolation(this, "Missing curly brace.", astNode);
    }

    if (astNode.is(JavaGrammar.IF_STATEMENT)) {
      AstNode elseClause = astNode.getFirstChild(JavaKeyword.ELSE);
      if (elseClause != null) {
        statement = elseClause.getNextSibling();
        if (!statement.getFirstChild().is(JavaGrammar.BLOCK) && !statement.getFirstChild().is(JavaGrammar.IF_STATEMENT)) {
          getContext().createLineViolation(this, "Missing curly brace.", elseClause);
        }
      }
    }
  }

}
