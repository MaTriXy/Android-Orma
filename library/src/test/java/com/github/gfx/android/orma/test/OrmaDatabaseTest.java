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
package com.github.gfx.android.orma.test;

import com.github.gfx.android.orma.AccessThreadConstraint;
import com.github.gfx.android.orma.ModelFactory;
import com.github.gfx.android.orma.core.Database;
import com.github.gfx.android.orma.test.database_package_test.OrmaDatabaseInAnotherPackage;
import com.github.gfx.android.orma.test.model.Author;
import com.github.gfx.android.orma.test.model.OrmaDatabase;
import com.github.gfx.android.orma.test.model.OrmaDatabaseToAvoidTryParsing;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
public class OrmaDatabaseTest {

    static final String NAME = "main.db";

    Context getContext() {
        return ApplicationProvider.getApplicationContext();
    }

    @After
    public void tearDown() throws Exception {
        getContext().deleteDatabase(NAME);
    }

    @Test
    public void testDefaultDatabaseName() throws Exception {
        assertThat(OrmaDatabase.Builder.getDefaultDatabaseName(getContext()), is(not(isEmptyOrNullString())));
    }

    @Test
    public void testSchemaHash() throws Exception {
        assertThat(OrmaDatabase.SCHEMA_HASH, is(not(isEmptyOrNullString())));
        assertThat(OrmaDatabaseToAvoidTryParsing.SCHEMA_HASH, is(not(isEmptyOrNullString())));
        assertThat(OrmaDatabaseInAnotherPackage.SCHEMA_HASH, is(not(isEmptyOrNullString())));
        assertThat(OrmaDatabaseToAvoidTryParsing.SCHEMA_HASH, is(not(OrmaDatabase.SCHEMA_HASH)));
        assertThat(OrmaDatabaseInAnotherPackage.SCHEMA_HASH, is(not(OrmaDatabase.SCHEMA_HASH)));
    }

    @Test
    public void testNumberOfSchemas() throws Exception {
        assertThat(OrmaDatabaseInAnotherPackage.SCHEMAS, hasSize(2));
    }

    @Test
    public void testDeleteAll() throws Exception {
        OrmaDatabase db = OrmaDatabase.builder(getContext())
                .name(NAME)
                .trace(true)
                .tryParsingSql(false)
                .build();

        db.prepareInsertIntoAuthor().execute(new ModelFactory<Author>() {
            @NonNull
            @Override
            public Author call() {
                Author author = new Author();
                author.name = "Jack and Jill";
                return author;
            }
        });

        assertThat(db.selectFromAuthor().count(), is(1));

        db.deleteAll();

        assertThat(db.selectFromAuthor().count(), is(0));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Test
    public void testCreateInstance() throws Exception {
        OrmaDatabase db = OrmaDatabase.builder(getContext())
                .name(NAME)
                .readOnMainThread(AccessThreadConstraint.NONE)
                .writeOnMainThread(AccessThreadConstraint.NONE)
                .tryParsingSql(false)
                .trace(true)
                .build();

        assertThat(db.getConnection(), is(not(nullValue())));
        assertThat(db.getSchemas(), is(not(nullValue())));
        assertThat(db.getConnection().getDatabaseName(), is(NAME));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Test
    public void testCreateInstanceWithWriteAheadLogging() throws Exception {
        OrmaDatabase db = OrmaDatabase.builder(getContext())
                .name(NAME)
                .writeAheadLogging(true)
                .tryParsingSql(false)
                .build();

        assertThat(db.getConnection().getReadableDatabase().isWriteAheadLoggingEnabled(), is(true));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Test
    public void testCreateInstanceWithoutWriteAheadLogging() throws Exception {
        OrmaDatabase db = OrmaDatabase.builder(getContext())
                .name(NAME)
                .writeAheadLogging(false)
                .tryParsingSql(false)
                .build();

        assertThat(db.getConnection().getReadableDatabase().isWriteAheadLoggingEnabled(), is(false));
    }

    @Test
    public void testDefaultForeignKeySetting() throws Exception {
        OrmaDatabase db = OrmaDatabase.builder(getContext())
                .tryParsingSql(false)
                .build();
        assertThat(isForeignKeyEnabled(db), is(true));
    }

    @Test
    public void testDisableForeignKey() throws Exception {
        OrmaDatabase db = OrmaDatabase.builder(getContext())
                .foreignKeys(false)
                .tryParsingSql(false)
                .build();
        assertThat(isForeignKeyEnabled(db), is(false));
    }

    boolean isForeignKeyEnabled(OrmaDatabase orma) {
        Database db = orma.getConnection().getReadableDatabase();
        return db.longForQuery("PRAGMA foreign_keys", null) != 0;
    }
}
