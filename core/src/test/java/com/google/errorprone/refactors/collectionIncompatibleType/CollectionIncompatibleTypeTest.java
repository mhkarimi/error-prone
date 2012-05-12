/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.errorprone.refactors.collectionIncompatibleType;

import static com.google.errorprone.DiagnosticTestHelper.diagnosticMessage;
import static com.google.errorprone.DiagnosticTestHelper.diagnosticOnLine;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.StringContains.containsString;

import com.google.errorprone.DiagnosticTestHelper;
import com.google.errorprone.ErrorProneCompiler;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class CollectionIncompatibleTypeTest {
  private ErrorProneCompiler compiler;
  private DiagnosticTestHelper diagnosticHelper;

  @Before
  public void setUp() {
    diagnosticHelper = new DiagnosticTestHelper();
    compiler = new ErrorProneCompiler.Builder()
        .refactor(new CollectionIncompatibleType.Scanner())
        .listenToDiagnostics(diagnosticHelper.collector)
        .build();
  }

  @Test
  public void testPositiveCase() throws Exception {
    File source = new File(this.getClass().getResource("PositiveCases.java").toURI());
    assertThat(compiler.compile(new String[]{"-Xjcov", source.getAbsolutePath()}), is(1));
    Matcher<Iterable<? super Diagnostic<JavaFileObject>>> matcher = allOf(
        hasItem(diagnosticOnLine(31)),
        hasItem(diagnosticOnLine(35)),
        hasItem(diagnosticOnLine(40)),
        hasItem(diagnosticMessage(containsString("did you mean 'return false;'"))));
    assertThat(diagnosticHelper.getDiagnostics(), matcher);
  }


}
