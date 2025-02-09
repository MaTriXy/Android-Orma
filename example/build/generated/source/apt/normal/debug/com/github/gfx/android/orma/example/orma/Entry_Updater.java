package com.github.gfx.android.orma.example.orma;

import androidx.annotation.NonNull;
import com.github.gfx.android.orma.rx.RxOrmaConnection;
import com.github.gfx.android.orma.rx.RxUpdater;
import java.util.Arrays;
import java.util.Collection;

public class Entry_Updater extends RxUpdater<Entry, Entry_Updater> {
  final Entry_Schema schema;

  public Entry_Updater(RxOrmaConnection conn, Entry_Schema schema) {
    super(conn);
    this.schema = schema;
  }

  public Entry_Updater(Entry_Updater that) {
    super(that);
    this.schema = that.getSchema();
  }

  public Entry_Updater(Entry_Relation relation) {
    super(relation);
    this.schema = relation.getSchema();
  }

  @Override
  public Entry_Updater clone() {
    return new Entry_Updater(this);
  }

  @NonNull
  @Override
  public Entry_Schema getSchema() {
    return schema;
  }

  public Entry_Updater resourceType(@NonNull String resourceType) {
    contents.put("`resourceType`", resourceType);
    return this;
  }

  public Entry_Updater resourceId(long resourceId) {
    contents.put("`resourceId`", resourceId);
    return this;
  }

  public Entry_Updater idEq(long id) {
    return where(schema.id, "=", id);
  }

  public Entry_Updater idNotEq(long id) {
    return where(schema.id, "<>", id);
  }

  public Entry_Updater idIn(@NonNull Collection<Long> values) {
    return in(false, schema.id, values);
  }

  public Entry_Updater idNotIn(@NonNull Collection<Long> values) {
    return in(true, schema.id, values);
  }

  public final Entry_Updater idIn(@NonNull Long... values) {
    return idIn(Arrays.asList(values));
  }

  public final Entry_Updater idNotIn(@NonNull Long... values) {
    return idNotIn(Arrays.asList(values));
  }

  public Entry_Updater idLt(long id) {
    return where(schema.id, "<", id);
  }

  public Entry_Updater idLe(long id) {
    return where(schema.id, "<=", id);
  }

  public Entry_Updater idGt(long id) {
    return where(schema.id, ">", id);
  }

  public Entry_Updater idGe(long id) {
    return where(schema.id, ">=", id);
  }

  /**
   * To build a condition <code>id BETWEEN a AND b</code>, which is equivalent to <code>a <= id AND id <= b</code>.
   */
  public Entry_Updater idBetween(long idA, long idB) {
    return whereBetween(schema.id, idA, idB);
  }

  public Entry_Updater resourceTypeAndResourceIdEq(@NonNull String resourceType, long resourceId) {
    return where(schema.resourceType, "=", resourceType).where(schema.resourceId, "=", resourceId);
  }
}
