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

package org.sonar.core.source.jdbc;

import org.apache.ibatis.session.SqlSession;
import org.sonar.core.persistence.MyBatis;

/**
 * @since 3.6
 */
public class SnapshotSourceDao {

  private final MyBatis mybatis;

  public SnapshotSourceDao(MyBatis myBatis) {
    this.mybatis = myBatis;
  }

  public String selectSnapshotSource(long snapshotId) {

    SqlSession session = mybatis.openBatchSession();

    try {
      SnapshotSourceMapper mapper = session.getMapper(SnapshotSourceMapper.class);
      return mapper.selectSnapshotSource(snapshotId);

    } finally {
      MyBatis.closeQuietly(session);
    }
  }
}
