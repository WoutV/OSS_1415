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
package org.sonar.java.jacoco;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG and JUnit listener that instructs JaCoCo to create one session per test.
 */
public class TestNGListener extends JUnitListener implements ITestListener {

  public TestNGListener() {
    this(JacocoController.getInstance());
  }

  TestNGListener(JacocoController jacoco) {
    super(jacoco);
  }

  public void onTestStart(ITestResult result) {
    jacoco.onTestStart(getName(result));
  }

  private static String getName(ITestResult result) {
    return result.getTestClass().getName() + " " + result.getMethod().getMethodName();
  }

  public void onTestSuccess(ITestResult result) {
    jacoco.onTestFinish(getName(result));
  }

  public void onTestFailure(ITestResult result) {
    jacoco.onTestFinish(getName(result));
  }

  public void onTestSkipped(ITestResult result) {
    jacoco.onTestFinish(getName(result));
  }

  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    jacoco.onTestFinish(getName(result));
  }

  public void onStart(ITestContext context) {
    // nop
  }

  public void onFinish(ITestContext context) {
    // nop
  }

}
