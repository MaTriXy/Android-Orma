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
package com.github.gfx.android.orma.example.orma;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

@Table
public class Todo {

    public static Todo create(@NonNull String title, @Nullable String content) {
        Todo todo = new Todo();
        todo.title = title;
        todo.content = content;
        todo.createdTime = new Date();
        return todo;
    }

    @PrimaryKey
    public long id;

    @Column(indexed = true)
    public String title;

    @Column
    @Nullable
    public String content;

    @Column(indexed = true, defaultExpr = "0")
    public boolean done;

    @Column(indexed = true, helpers = Column.Helpers.ORDERS, defaultExpr = "0")
    public Date createdTime;
}
