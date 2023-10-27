package com.code_that_up.john_xenakis.my_notie.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.code_that_up.john_xenakis.my_notie.model.Folder;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FoldersDAO_Impl implements FoldersDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Folder> __insertionAdapterOfFolder;

  private final EntityDeletionOrUpdateAdapter<Folder> __deletionAdapterOfFolder;

  private final EntityDeletionOrUpdateAdapter<Folder> __updateAdapterOfFolder;

  public FoldersDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFolder = new EntityInsertionAdapter<Folder>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `folders` (`folderId`,`folderName`,`checked`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Folder entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFolderName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFolderName());
        }
        final int _tmp = entity.getChecked() ? 1 : 0;
        statement.bindLong(3, _tmp);
      }
    };
    this.__deletionAdapterOfFolder = new EntityDeletionOrUpdateAdapter<Folder>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `folders` WHERE `folderId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Folder entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfFolder = new EntityDeletionOrUpdateAdapter<Folder>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `folders` SET `folderId` = ?,`folderName` = ?,`checked` = ? WHERE `folderId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Folder entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFolderName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFolderName());
        }
        final int _tmp = entity.getChecked() ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindLong(4, entity.getId());
      }
    };
  }

  @Override
  public void insertFolder(final Folder folder) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFolder.insert(folder);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteFolder(final Folder folder) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFolder.handle(folder);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateFolder(final Folder folder) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfFolder.handle(folder);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Folder> getAllFolders() {
    final String _sql = "SELECT * FROM folders";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "folderId");
      final int _cursorIndexOfFolderName = CursorUtil.getColumnIndexOrThrow(_cursor, "folderName");
      final int _cursorIndexOfChecked = CursorUtil.getColumnIndexOrThrow(_cursor, "checked");
      final List<Folder> _result = new ArrayList<Folder>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Folder _item;
        _item = new Folder();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpFolderName;
        if (_cursor.isNull(_cursorIndexOfFolderName)) {
          _tmpFolderName = null;
        } else {
          _tmpFolderName = _cursor.getString(_cursorIndexOfFolderName);
        }
        _item.setFolderName(_tmpFolderName);
        final boolean _tmpChecked;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfChecked);
        _tmpChecked = _tmp != 0;
        _item.setChecked(_tmpChecked);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getMaxFolderId() {
    final String _sql = "SELECT MAX(folderId) FROM folders";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Integer> getListOfFolderIds() {
    final String _sql = "SELECT folderId FROM folders";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<Integer> _result = new ArrayList<Integer>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Integer _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getInt(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
