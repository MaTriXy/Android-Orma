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

import com.github.gfx.android.orma.ColumnDef;
import com.github.gfx.android.orma.Inserter;
import com.github.gfx.android.orma.ModelFactory;
import com.github.gfx.android.orma.function.Function1;
import com.github.gfx.android.orma.test.model.Author;
import com.github.gfx.android.orma.test.model.Author_AssociationCondition;
import com.github.gfx.android.orma.test.model.ModelWithDirectAssociation;
import com.github.gfx.android.orma.test.model.ModelWithDirectAssociation2;
import com.github.gfx.android.orma.test.model.ModelWithDirectAssociation_AssociationCondition;
import com.github.gfx.android.orma.test.model.ModelWithDirectAssociation_Relation;
import com.github.gfx.android.orma.test.model.ModelWithDirectAssociation_Schema;
import com.github.gfx.android.orma.test.model.ModelWithDirectAssociation_Selector;
import com.github.gfx.android.orma.test.model.ModelWithMoreNestedDirectAssociations;
import com.github.gfx.android.orma.test.model.ModelWithMoreNestedDirectAssociations_Selector;
import com.github.gfx.android.orma.test.model.ModelWithNestedDirectAssociations;
import com.github.gfx.android.orma.test.model.ModelWithNestedDirectAssociations_AssociationCondition;
import com.github.gfx.android.orma.test.model.ModelWithNullableDirectAssociations;
import com.github.gfx.android.orma.test.model.OrmaDatabase;
import com.github.gfx.android.orma.test.model.Publisher;
import com.github.gfx.android.orma.test.toolbox.OrmaFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @see ForeignKeysTest
 */
@RunWith(AndroidJUnit4.class)
public class DirectAssociationsTest {

    OrmaDatabase orma;

    Author author1;

    Author author2;

    Publisher publisher;

    @Before
    public void setUp() throws Exception {
        orma = OrmaFactory.create();

        author1 = orma.createAuthor(new ModelFactory<Author>() {
            @NonNull
            @Override
            public Author call() {
                Author author = new Author();
                author.name = "A";
                author.note = "A's note";
                return author;
            }
        });

        author2 = orma.createAuthor(new ModelFactory<Author>() {
            @NonNull
            @Override
            public Author call() {
                Author author = new Author();
                author.name = "B";
                author.note = "B's note";
                return author;
            }
        });

        publisher = orma.createPublisher(new ModelFactory<Publisher>() {
            @NonNull
            @Override
            public Publisher call() {
                Publisher publisher = new Publisher();
                publisher.name = "publisher's note";
                publisher.startedYear = 2015;
                publisher.startedMonth = 12;
                return publisher;
            }
        });
    }

    @Test
    public void testCreate() throws Exception {
        ModelWithDirectAssociation model = orma.createModelWithDirectAssociation(
                new ModelFactory<ModelWithDirectAssociation>() {
                    @NonNull
                    @Override
                    public ModelWithDirectAssociation call() {
                        ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                        model.name = "foo";
                        model.author = author1;
                        model.publisher = publisher;
                        model.note = "SQLite rocks";
                        return model;
                    }
                });

        assertThat(model.name, is("foo"));
        assertThat(model.note, is("SQLite rocks"));
        assertThat(model.author, is(notNullValue()));
        assertThat(model.author.name, is(author1.name));
        assertThat(model.author.note, is(author1.note));
        assertThat(model.publisher, is(notNullValue()));
        assertThat(model.publisher.id, is(publisher.id));
        assertThat(model.publisher.name, is(publisher.name));
        assertThat(model.publisher.startedYear, is(publisher.startedYear));
        assertThat(model.publisher.startedMonth, is(publisher.startedMonth));
    }

    @Test
    public void testCreateNested() throws Exception {
        ModelWithNestedDirectAssociations model = orma.createModelWithNestedDirectAssociations(
                new ModelFactory<ModelWithNestedDirectAssociations>() {
                    @NonNull
                    @Override
                    public ModelWithNestedDirectAssociations call() {
                        ModelWithNestedDirectAssociations model = new ModelWithNestedDirectAssociations();
                        model.note = "This is a nested model";
                        model.md = orma.createModelWithDirectAssociation(new ModelFactory<ModelWithDirectAssociation>() {
                            @NonNull
                            @Override
                            public ModelWithDirectAssociation call() {
                                ModelWithDirectAssociation md = new ModelWithDirectAssociation();
                                md.name = "foo";
                                md.author = author1;
                                md.publisher = publisher;
                                md.note = "SQLite rocks";
                                return md;
                            }
                        });
                        return model;
                    }
                });

        assertThat(model.note, is("This is a nested model"));
        assertThat(model.md.name, is("foo"));
        assertThat(model.md.note, is("SQLite rocks"));
        assertThat(model.md.author, is(notNullValue()));
        assertThat(model.md.author.name, is(author1.name));
        assertThat(model.md.author.note, is(author1.note));
        assertThat(model.md.publisher, is(notNullValue()));
        assertThat(model.md.publisher.id, is(publisher.id));
        assertThat(model.md.publisher.name, is(publisher.name));
        assertThat(model.md.publisher.startedYear, is(publisher.startedYear));
        assertThat(model.md.publisher.startedMonth, is(publisher.startedMonth));
    }

    @Test
    public void testCreateMoreNested() throws Exception {
        ModelWithMoreNestedDirectAssociations model = orma.createModelWithMoreNestedDirectAssociations(
                new ModelFactory<ModelWithMoreNestedDirectAssociations>() {
                    @NonNull
                    @Override
                    public ModelWithMoreNestedDirectAssociations call() {
                        ModelWithMoreNestedDirectAssociations model = new ModelWithMoreNestedDirectAssociations();
                        model.note = "This is a more nested model";
                        model.mnd = orma.createModelWithNestedDirectAssociations(
                                new ModelFactory<ModelWithNestedDirectAssociations>() {
                                    @NonNull
                                    @Override
                                    public ModelWithNestedDirectAssociations call() {
                                        ModelWithNestedDirectAssociations model = new ModelWithNestedDirectAssociations();
                                        model.note = "This is a nested model";
                                        model.md = orma.createModelWithDirectAssociation(
                                                new ModelFactory<ModelWithDirectAssociation>() {
                                                    @NonNull
                                                    @Override
                                                    public ModelWithDirectAssociation call() {
                                                        ModelWithDirectAssociation md = new ModelWithDirectAssociation();
                                                        md.name = "foo";
                                                        md.author = author1;
                                                        md.publisher = publisher;
                                                        md.note = "SQLite rocks";
                                                        return md;
                                                    }
                                                });
                                        return model;
                                    }
                                });
                        return model;
                    }
                });

        assertThat(model.note, is("This is a more nested model"));
        assertThat(model.mnd.note, is("This is a nested model"));
        assertThat(model.mnd.md.name, is("foo"));
        assertThat(model.mnd.md.author, is(notNullValue()));
        assertThat(model.mnd.md.author.name, is(author1.name));
        assertThat(model.mnd.md.author.note, is(author1.note));
        assertThat(model.mnd.md.publisher, is(notNullValue()));
        assertThat(model.mnd.md.publisher.id, is(publisher.id));
        assertThat(model.mnd.md.publisher.name, is(publisher.name));
        assertThat(model.mnd.md.publisher.startedYear, is(publisher.startedYear));
        assertThat(model.mnd.md.publisher.startedMonth, is(publisher.startedMonth));
    }

    @Test
    public void testCreateDirectAssociation2() throws Exception {
        ModelWithDirectAssociation2 model = orma.createModelWithDirectAssociation2(
                new ModelFactory<ModelWithDirectAssociation2>() {
                    @NonNull
                    @Override
                    public ModelWithDirectAssociation2 call() {
                        ModelWithDirectAssociation2 model = new ModelWithDirectAssociation2();

                        model.name = "model with multiple associations with the same type";
                        model.note = "blah blah blah";

                        model.author1 = orma.createAuthor(new ModelFactory<Author>() {
                            @NonNull
                            @Override
                            public Author call() {
                                Author author = new Author();
                                author.name = "author (1)";
                                return author;
                            }
                        });
                        model.author2 = orma.createAuthor(new ModelFactory<Author>() {
                            @NonNull
                            @Override
                            public Author call() {
                                Author author = new Author();
                                author.name = "author (2)";
                                return author;
                            }
                        });

                        model.publisher1 = orma.createPublisher(new ModelFactory<Publisher>() {
                            @NonNull
                            @Override
                            public Publisher call() {
                                return Publisher.create("publisher (1)", 2016, 12);
                            }
                        });

                        model.publisher2 = orma.createPublisher(new ModelFactory<Publisher>() {
                            @NonNull
                            @Override
                            public Publisher call() {
                                return Publisher.create("publisher (2)", 2010, 9);
                            }
                        });

                        return model;
                    }
                });

        assertThat(model.author1.name, is("author (1)"));
        assertThat(model.author2.name, is("author (2)"));
        assertThat(model.publisher1.name, is("publisher (1)"));
        assertThat(model.publisher2.name, is("publisher (2)"));
    }

    @Test
    public void testUpdate() throws Exception {
        orma.createModelWithDirectAssociation(
                new ModelFactory<ModelWithDirectAssociation>() {
                    @NonNull
                    @Override
                    public ModelWithDirectAssociation call() {
                        ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                        model.name = "foo";
                        model.author = author1;
                        model.publisher = publisher;
                        model.note = "SQLite rocks";
                        return model;
                    }
                });

        orma.updateModelWithDirectAssociation()
                .authorEq(author1)
                .author(author2)
                .execute();

        ModelWithDirectAssociation model = orma.selectFromModelWithDirectAssociation().value();

        assertThat(model.name, is("foo"));
        assertThat(model.note, is("SQLite rocks"));
        assertThat(model.author, is(notNullValue()));
        assertThat(model.author.name, is(author2.name));
        assertThat(model.author.note, is(author2.note));
    }

    @Test
    public void testDelete() throws Exception {
        orma.createModelWithDirectAssociation(
                new ModelFactory<ModelWithDirectAssociation>() {
                    @NonNull
                    @Override
                    public ModelWithDirectAssociation call() {
                        ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                        model.name = "foo";
                        model.author = author1;
                        model.publisher = publisher;
                        model.note = "SQLite rocks";
                        return model;
                    }
                });

        orma.deleteFromModelWithDirectAssociation()
                .authorEq(author1)
                .execute();

        assertThat(orma.selectFromModelWithDirectAssociation().isEmpty(), is(true));
    }

    @Test
    public void testFindByAssociatedModel() throws Exception {
        Inserter<ModelWithDirectAssociation> inserter = orma.prepareInsertIntoModelWithDirectAssociation();
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "foo";
                model.author = author1;
                model.publisher = publisher;
                model.note = "SQLite rocks";
                return model;
            }
        });
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "bar";
                model.author = author2;
                model.publisher = publisher;
                model.note = "SQLite supports most of SQL92";
                return model;
            }
        });

        ModelWithDirectAssociation_Selector selector = orma.selectFromModelWithDirectAssociation()
                .authorEq(author1);

        assertThat(selector.count(), is(1));

        ModelWithDirectAssociation model = selector.value();
        assertThat(model.name, is("foo"));
        assertThat(model.note, is("SQLite rocks"));
        assertThat(model.author, is(notNullValue()));
        assertThat(model.author.name, is(author1.name));
        assertThat(model.author.note, is(author1.note));
    }

    @Test
    public void testFindByPrimaryKey() throws Exception {
        Inserter<ModelWithDirectAssociation> inserter = orma.prepareInsertIntoModelWithDirectAssociation();
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "foo";
                model.author = author1;
                model.publisher = publisher;
                model.note = "SQLite rocks";
                return model;
            }
        });
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "bar";
                model.author = author2;
                model.publisher = publisher;
                model.note = "SQLite supports most of SQL92";
                return model;
            }
        });

        ModelWithDirectAssociation_Selector selector = orma.selectFromModelWithDirectAssociation()
                .authorEq(author1.name);

        assertThat(selector.count(), is(1));

        ModelWithDirectAssociation model = selector.value();
        assertThat(model.name, is("foo"));
        assertThat(model.note, is("SQLite rocks"));
        assertThat(model.author, is(notNullValue()));
        assertThat(model.author.name, is(author1.name));
        assertThat(model.author.note, is(author1.note));
    }

    @Test
    public void testFindByFieldWithConflict() throws Exception {
        Inserter<ModelWithDirectAssociation> inserter = orma.prepareInsertIntoModelWithDirectAssociation();
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "foo";
                model.author = author1;
                model.publisher = publisher;
                model.note = "SQLite rocks";
                return model;
            }
        });
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "bar";
                model.author = author2;
                model.publisher = publisher;
                model.note = "SQLite supports most of SQL92";
                return model;
            }
        });

        // Both ModelWithDirectAssociation and Author has "note" column
        ModelWithDirectAssociation_Selector selector = orma.selectFromModelWithDirectAssociation()
                .noteEq("SQLite supports most of SQL92");

        assertThat(selector.count(), is(1));

        ModelWithDirectAssociation model = selector.value();
        assertThat(model.name, is("bar"));
    }

    @Test
    public void testFindByAssociatedModelCondition() throws Exception {
        Inserter<ModelWithDirectAssociation> inserter = orma.prepareInsertIntoModelWithDirectAssociation();
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "foo";
                model.author = author1;
                model.publisher = publisher;
                model.note = "SQLite rocks";
                return model;
            }
        });
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "bar";
                model.author = author2;
                model.publisher = publisher;
                model.note = "SQLite supports most of SQL92";
                return model;
            }
        });

        ModelWithDirectAssociation_Selector selector = orma.selectFromModelWithDirectAssociation()
                .author(new Function1<Author_AssociationCondition, Author_AssociationCondition>() {
                    @Override
                    public Author_AssociationCondition apply(Author_AssociationCondition cond) {
                        return cond.noteEq(author1.note);
                    }
                });

        assertThat(selector.count(), is(1));

        ModelWithDirectAssociation model = selector.value();
        assertThat(model.name, is("foo"));
        assertThat(model.note, is("SQLite rocks"));
        assertThat(model.author, is(notNullValue()));
        assertThat(model.author.name, is(author1.name));
        assertThat(model.author.note, is(author1.note));
    }

    @Test
    public void testFindByMoreNestedAssociatedModelCondition() throws Exception {
        ModelWithMoreNestedDirectAssociations model =  orma.createModelWithMoreNestedDirectAssociations(
                new ModelFactory<ModelWithMoreNestedDirectAssociations>() {
                    @NonNull
                    @Override
                    public ModelWithMoreNestedDirectAssociations call() {
                        ModelWithMoreNestedDirectAssociations model = new ModelWithMoreNestedDirectAssociations();
                        model.note = "This is a more nested model";
                        model.mnd = orma.createModelWithNestedDirectAssociations(
                                new ModelFactory<ModelWithNestedDirectAssociations>() {
                                    @NonNull
                                    @Override
                                    public ModelWithNestedDirectAssociations call() {
                                        ModelWithNestedDirectAssociations model = new ModelWithNestedDirectAssociations();
                                        model.note = "This is a nested model";
                                        model.md = orma.createModelWithDirectAssociation(
                                                new ModelFactory<ModelWithDirectAssociation>() {
                                                    @NonNull
                                                    @Override
                                                    public ModelWithDirectAssociation call() {
                                                        ModelWithDirectAssociation md = new ModelWithDirectAssociation();
                                                        md.name = "foo";
                                                        md.author = author1;
                                                        md.publisher = publisher;
                                                        md.note = "SQLite rocks";
                                                        return md;
                                                    }
                                                });
                                        return model;
                                    }
                                });
                        return model;
                    }
                });

        ModelWithMoreNestedDirectAssociations_Selector selector = orma.selectFromModelWithMoreNestedDirectAssociations()
                .mnd(new Function1<ModelWithNestedDirectAssociations_AssociationCondition, ModelWithNestedDirectAssociations_AssociationCondition>() {
                    @Override
                    public ModelWithNestedDirectAssociations_AssociationCondition apply(ModelWithNestedDirectAssociations_AssociationCondition cond) {
                        return cond.md(new Function1<ModelWithDirectAssociation_AssociationCondition, ModelWithDirectAssociation_AssociationCondition>() {
                                    @Override
                                    public ModelWithDirectAssociation_AssociationCondition apply(ModelWithDirectAssociation_AssociationCondition cond) {
                                        return cond.author(new Function1<Author_AssociationCondition, Author_AssociationCondition>() {
                                                    @Override
                                                    public Author_AssociationCondition apply(Author_AssociationCondition cond) {
                                                        return cond.noteEq(author1.note);
                                                    }
                                                });
                                    }
                                });
                    }
                });

        assertThat(selector.count(), is(1));

        ModelWithMoreNestedDirectAssociations actual = selector.value();
        assertThat(actual.note, is(model.note));
    }

    @Test
    public void testCascadingDelete() throws Exception {
        orma.createModelWithDirectAssociation(
                new ModelFactory<ModelWithDirectAssociation>() {
                    @NonNull
                    @Override
                    public ModelWithDirectAssociation call() {
                        ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                        model.name = "foo";
                        model.author = author1;
                        model.publisher = publisher;
                        model.note = "SQLite rocks";
                        return model;
                    }
                });

        orma.deleteFromAuthor()
                .nameEq(author1.name)
                .execute();

        assertThat(orma.selectFromModelWithDirectAssociation().isEmpty(), is(true));
    }

    @Test
    public void testNullableDirectAssociations() throws Exception {
        ModelWithNullableDirectAssociations model = orma
                .createModelWithNullableDirectAssociations(new ModelFactory<ModelWithNullableDirectAssociations>() {
                    @NonNull
                    @Override
                    public ModelWithNullableDirectAssociations call() {
                        return new ModelWithNullableDirectAssociations();
                    }
                });

        assertThat(model.author, is(nullValue()));
        assertThat(orma.selectFromModelWithNullableDirectAssociations().authorIsNull().count(), is(1));
    }

    @Test
    public void testRelationWithOrderByClauses() throws Exception {
        Inserter<ModelWithDirectAssociation> inserter = orma.prepareInsertIntoModelWithDirectAssociation();
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "foo";
                model.author = author1;
                model.publisher = publisher;
                model.note = "SQLite rocks";
                return model;
            }
        });
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "bar";
                model.author = author2;
                model.publisher = publisher;
                model.note = "SQLite supports most of SQL92";
                return model;
            }
        });

        ModelWithDirectAssociation_Selector selector = orma.relationOfModelWithDirectAssociation()
                .authorEq(author1.name)
                .orderByNameAsc()
                .selector();

        assertThat(selector.count(), is(1));

        ModelWithDirectAssociation model = selector.value();
        assertThat(model.name, is("foo"));
        assertThat(model.note, is("SQLite rocks"));
        assertThat(model.author, is(notNullValue()));
        assertThat(model.author.name, is(author1.name));
        assertThat(model.author.note, is(author1.note));
    }

    @Test
    public void testRelationWithWhereClauses() throws Exception {
        Inserter<ModelWithDirectAssociation> inserter = orma.prepareInsertIntoModelWithDirectAssociation();
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "foo";
                model.author = author1;
                model.publisher = publisher;
                model.note = "SQLite rocks";
                return model;
            }
        });
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "bar";
                model.author = author2;
                model.publisher = publisher;
                model.note = "SQLite supports most of SQL92";
                return model;
            }
        });

        ModelWithDirectAssociation_Relation relation = orma.relationOfModelWithDirectAssociation()
                .nameEq("foo")
                .orderByNameAsc();

        assertThat(relation.count(), is(1));

        ModelWithDirectAssociation model = relation.get(0);
        assertThat(model.name, is("foo"));
        assertThat(model.note, is("SQLite rocks"));
        assertThat(model.author, is(notNullValue()));
        assertThat(model.author.name, is(author1.name));
        assertThat(model.author.note, is(author1.note));
    }

    @Test
    public void testNestedWhere() throws Exception {
        Inserter<ModelWithDirectAssociation> inserter = orma.prepareInsertIntoModelWithDirectAssociation();
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "foo";
                model.author = author1;
                model.publisher = publisher;
                model.note = "SQLite rocks";
                return model;
            }
        });
        inserter.execute(new ModelFactory<ModelWithDirectAssociation>() {
            @NonNull
            @Override
            public ModelWithDirectAssociation call() {
                ModelWithDirectAssociation model = new ModelWithDirectAssociation();
                model.name = "bar";
                model.author = author2;
                model.publisher = publisher;
                model.note = "SQLite supports most of SQL92";
                return model;
            }
        });

        ModelWithDirectAssociation_Relation relation = orma.relationOfModelWithDirectAssociation()
                .where(ModelWithDirectAssociation_Schema.INSTANCE.author, "=", author1.name);

        assertThat(relation.count(), is(1));

        ModelWithDirectAssociation model = relation.get(0);
        assertThat(model.author.name, is(author1.name));
    }

}
