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

package org.sonar.server.issue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.IssueQuery;
import org.sonar.api.issue.IssueQueryResult;
import org.sonar.api.issue.internal.DefaultIssue;
import org.sonar.api.issue.internal.IssueChangeContext;
import org.sonar.api.web.UserRole;
import org.sonar.core.dryrun.DryRunCache;
import org.sonar.core.issue.IssueNotifications;
import org.sonar.core.issue.db.IssueStorage;
import org.sonar.server.exceptions.BadRequestException;
import org.sonar.server.user.UserSession;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

public class IssueBulkChangeService {

  private static final Logger LOG = LoggerFactory.getLogger(IssueBulkChangeService.class);

  private final DefaultIssueFinder issueFinder;
  private final IssueStorage issueStorage;
  private final IssueNotifications issueNotifications;
  private final DryRunCache dryRunCache;
  private final List<Action> actions;

  public IssueBulkChangeService(DefaultIssueFinder issueFinder, IssueStorage issueStorage, IssueNotifications issueNotifications, List<Action> actions, DryRunCache dryRunCache) {
    this.issueFinder = issueFinder;
    this.issueStorage = issueStorage;
    this.issueNotifications = issueNotifications;
    this.actions = actions;
    this.dryRunCache = dryRunCache;
  }

  public IssueBulkChangeResult execute(IssueBulkChangeQuery issueBulkChangeQuery, UserSession userSession) {
    LOG.debug("BulkChangeQuery : {}", issueBulkChangeQuery);
    long start = System.currentTimeMillis();
    userSession.checkLoggedIn();

    IssueBulkChangeResult result = new IssueBulkChangeResult();
    IssueQueryResult issueQueryResult = issueFinder.find(IssueQuery.builder().issueKeys(issueBulkChangeQuery.issues()).pageSize(-1).requiredRole(UserRole.USER).build());
    List<Issue> issues = issueQueryResult.issues();
    List<Action> bulkActions = getActionsToApply(issueBulkChangeQuery, issues, userSession);

    IssueChangeContext issueChangeContext = IssueChangeContext.createUser(new Date(), userSession.login());
    Set<String> concernedProjects = new HashSet<String>();
    for (Issue issue : issues) {
      ActionContext actionContext = new ActionContext(issue, issueChangeContext);
      for (Action action : bulkActions) {
        applyAction(action, actionContext, issueBulkChangeQuery, result);
      }
      if (result.issuesChanged().contains(issue)) {
        // Apply comment action only on changed issues
        if (issueBulkChangeQuery.hasComment()) {
          applyAction(getAction(CommentAction.KEY), actionContext, issueBulkChangeQuery, result);
        }
        issueStorage.save((DefaultIssue) issue);
        issueNotifications.sendChanges((DefaultIssue) issue, issueChangeContext, issueQueryResult);
        concernedProjects.add(((DefaultIssue) issue).projectKey());
      }
    }
    // Purge dryRun cache
    for (String projectKey : concernedProjects) {
      dryRunCache.reportResourceModification(projectKey);
    }
    LOG.debug("BulkChange execution time : {} ms", System.currentTimeMillis() - start);
    return result;
  }

  private List<Action> getActionsToApply(IssueBulkChangeQuery issueBulkChangeQuery, List<Issue> issues, UserSession userSession) {
    List<Action> bulkActions = newArrayList();
    for (String actionKey : issueBulkChangeQuery.actions()) {
      Action action = getAction(actionKey);
      if (action.verify(issueBulkChangeQuery.properties(actionKey), issues, userSession)) {
        bulkActions.add(action);
      }
    }
    return bulkActions;
  }

  private void applyAction(Action action, ActionContext actionContext, IssueBulkChangeQuery issueBulkChangeQuery, IssueBulkChangeResult result) {
    Issue issue = actionContext.issue();
    try {
      if (action.supports(issue) && action.execute(issueBulkChangeQuery.properties(action.key()), actionContext)) {
        result.addIssueChanged(issue);
      } else {
        result.addIssueNotChanged(issue);
      }
    } catch (Exception e) {
      result.addIssueNotChanged(issue);
      LOG.info("An error occur when trying to apply the action : " + action.key() + " on issue : " + issue.key() + ". This issue has been ignored.", e);
    }
  }

  private Action getAction(final String actionKey) {
    Action action = Iterables.find(actions, new Predicate<Action>() {
      @Override
      public boolean apply(Action action) {
        return action.key().equals(actionKey);
      }
    }, null);
    if (action == null) {
      throw new BadRequestException("The action : '" + actionKey + "' is unknown");
    }
    return action;
  }

  static class ActionContext implements Action.Context {
    private final Issue issue;
    private final IssueChangeContext changeContext;

    ActionContext(Issue issue, IssueChangeContext changeContext) {
      this.issue = issue;
      this.changeContext = changeContext;
    }

    @Override
    public Issue issue() {
      return issue;
    }

    @Override
    public IssueChangeContext issueChangeContext() {
      return changeContext;
    }
  }
}
