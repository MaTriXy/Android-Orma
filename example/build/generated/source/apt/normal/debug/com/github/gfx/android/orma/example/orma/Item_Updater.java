package com.github.gfx.android.orma.example.orma;

import androidx.annotation.NonNull;
import com.github.gfx.android.orma.function.Function1;
import com.github.gfx.android.orma.rx.RxOrmaConnection;
import com.github.gfx.android.orma.rx.RxUpdater;
import java.util.Arrays;
import java.util.Collection;

public class Item_Updater extends RxUpdater<Item, Item_Updater> {
  final Item_Schema schema;

  public Item_Updater(RxOrmaConnection conn, Item_Schema schema) {
    super(conn);
    this.schema = schema;
  }

  public Item_Updater(Item_Updater that) {
    super(that);
    this.schema = that.getSchema();
  }

  public Item_Updater(Item_Relation relation) {
    super(relation);
    this.schema = relation.getSchema();
  }

  @Override
  public Item_Updater clone() {
    return new Item_Updater(this);
  }

  @NonNull
  @Override
  public Item_Schema getSchema() {
    return schema;
  }

  public Item_Updater category(@NonNull Category category) {
    contents.put("`category`", category.id);
    return this;
  }

  public Item_Updater name(@NonNull String name) {
    contents.put("`name`", name);
    return this;
  }

  public Item_Updater categoryEq(@NonNull Category category) {
    return where(schema.category, "=", category.id);
  }

  public Item_Updater categoryEq(long categoryId) {
    return where(schema.category, "=", categoryId);
  }

  public Item_Updater category(
      @NonNull Function1<Category_AssociationCondition, Category_AssociationCondition> block) {
    return block.apply(new Category_AssociationCondition(getConnection(), schema.category.associationSchema)).appendTo(this);
  }

  public Item_Updater nameEq(@NonNull String name) {
    return where(schema.name, "=", name);
  }

  public Item_Updater nameNotEq(@NonNull String name) {
    return where(schema.name, "<>", name);
  }

  public Item_Updater nameIn(@NonNull Collection<String> values) {
    return in(false, schema.name, values);
  }

  public Item_Updater nameNotIn(@NonNull Collection<String> values) {
    return in(true, schema.name, values);
  }

  public final Item_Updater nameIn(@NonNull String... values) {
    return nameIn(Arrays.asList(values));
  }

  public final Item_Updater nameNotIn(@NonNull String... values) {
    return nameNotIn(Arrays.asList(values));
  }

  public Item_Updater nameLt(@NonNull String name) {
    return where(schema.name, "<", name);
  }

  public Item_Updater nameLe(@NonNull String name) {
    return where(schema.name, "<=", name);
  }

  public Item_Updater nameGt(@NonNull String name) {
    return where(schema.name, ">", name);
  }

  public Item_Updater nameGe(@NonNull String name) {
    return where(schema.name, ">=", name);
  }
}
