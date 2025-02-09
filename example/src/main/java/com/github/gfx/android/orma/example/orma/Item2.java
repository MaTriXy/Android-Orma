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

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZonedDateTime;

import androidx.annotation.Nullable;

/**
 * To demonstrate multiple associations to the same model.
 *
 * @see Item2_Schema
 */
@Table
public class Item2 {

    @PrimaryKey
    public String name;

    @Column(indexed = true)
    public Category category1;

    @Nullable
    @Column(indexed = true)
    public Category category2;

    @Column
    public ZonedDateTime zonedTimestamp = ZonedDateTime.now();

    @Column
    public LocalDateTime localDateTime = LocalDateTime.now();
}
