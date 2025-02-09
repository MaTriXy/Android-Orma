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

package com.github.gfx.android.orma.test.model;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.Getter;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Setter;
import com.github.gfx.android.orma.annotation.Table;

import androidx.annotation.Nullable;

/**
 * {@link ModelWithAccessors} with the {@code @Setter} constructor
 */
@Table
public class ModelWithAccessorsWithSetterConstructor {

    static final String kId = "id";

    static final String kKey = "key";

    @PrimaryKey
    @Column(kId)
    private long id;

    @Column(kKey)
    private String key;

    @Column // omit the name
    private String value;

    @Column // boolean type getters might have "is" prefix
    private boolean checked;

    @Column
    @Nullable // Boolean is a kind of boolean
    private Boolean done;

    @Column // without @Getter and @Setter annotations
    private long order;

    @Setter
    public ModelWithAccessorsWithSetterConstructor(long id, String key, String value, boolean checked, @Nullable Boolean done,
            long order) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.checked = checked;
        this.done = done;
        this.order = order;
    }

    @Getter(kId)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Getter(kKey)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Getter // omit the name
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Getter
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Getter
    @Nullable
    public Boolean isDone() {
        return done;
    }

    public void setDone(@Nullable Boolean done) {
        this.done = done;
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }
}
