/*
 * Copyright (c) 2015 FUJI Goro (gfx).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.gfx.android.orma.processor.test;

import com.google.testing.compile.JavaFileObjects;

import com.github.gfx.android.orma.processor.OrmaProcessor;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class SchemaValidatorTest {

    @Test
    public void testDuplicateColumnNames() throws Exception {
        JavaFileObject modelFile = JavaFileObjects.forResource("ColumnNameDuplication.java");

        assert_().about(javaSource())
                .that(modelFile)
                .processedWith(new OrmaProcessor())
                .failsToCompile()
                .withErrorCount(2)
                .withErrorContaining("Duplicate column names \"foo\" found");
    }

    @Test
    public void testDuplicateColumnNamesCaseInsensitive() throws Exception {
        JavaFileObject modelFile = JavaFileObjects.forResource("CaseInsensitiveDuplication.java");

        assert_().about(javaSource())
                .that(modelFile)
                .processedWith(new OrmaProcessor())
                .failsToCompile()
                .withErrorCount(2)
                .withErrorContaining("Duplicate column names \"foo\" found");
    }

    @Test
    public void testDuplicateColumnNamesIncludingPrimaryKey() throws Exception {
        JavaFileObject modelFile = JavaFileObjects.forResource("PrimaryKeyAndColumnNamesDuplication.java");

        assert_().about(javaSource())
                .that(modelFile)
                .processedWith(new OrmaProcessor())
                .failsToCompile()
                .withErrorCount(2)
                .withErrorContaining("Duplicate column names \"foo\" found");
    }

    @Test
    public void testNoColumnInTable() throws Exception {
        JavaFileObject modelFile = JavaFileObjects.forResource("NoColumn.java");

        assert_().about(javaSource())
                .that(modelFile)
                .processedWith(new OrmaProcessor())
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining("No @Column nor @PrimaryKey is defined");
    }

    @Test
    public void testUnsatisfiedConstructor() throws Exception {
        JavaFileObject modelFile = JavaFileObjects.forResource("UnsatisfiedConstructor.java");

        assert_().about(javaSource())
                .that(modelFile)
                .processedWith(new OrmaProcessor())
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining("The @Setter constructor parameters must satisfy all the @Column fields");
    }

    @Test
    public void ambiguousSetterConstructors() throws Exception {
        JavaFileObject modelFile = JavaFileObjects.forResource("AmbiguousSetterConstructors.java");

        assert_().about(javaSource())
                .that(modelFile)
                .processedWith(new OrmaProcessor())
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining("Cannot detect a constructor from multiple @Setter constructors that have the same parameter size");
    }

    @Test
    public void testModelInheritance() throws Exception {
        JavaFileObject modelFile = JavaFileObjects.forResource("ModelInheritance.java");

        assert_().about(javaSource())
                .that(modelFile)
                .processedWith(new OrmaProcessor())
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining("The superclasses of Orma models are not allowed to have @Table annotation");
    }
}
