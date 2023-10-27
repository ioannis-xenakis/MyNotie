package com.code_that_up.john_xenakis.my_notie.db;

import androidx.annotation.NonNull;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.lang.Override;
import java.lang.SuppressWarnings;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
final class NotesDB_AutoMigration_1_2_Impl extends Migration {
  private final AutoMigrationSpec callback = new NotesDB.MyAutoMigration();

  public NotesDB_AutoMigration_1_2_Impl() {
    super(1, 2);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    callback.onPostMigrate(db);
  }
}
