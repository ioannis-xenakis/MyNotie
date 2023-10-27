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
import com.code_that_up.john_xenakis.my_notie.model.Note;
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
public final class NotesDAO_Impl implements NotesDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Note> __insertionAdapterOfNote;

  private final EntityDeletionOrUpdateAdapter<Note> __deletionAdapterOfNote;

  private final EntityDeletionOrUpdateAdapter<Note> __updateAdapterOfNote;

  public NotesDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNote = new EntityInsertionAdapter<Note>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `notes` (`noteId`,`folderId`,`noteTitle`,`text`,`date`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Note entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFolderId());
        if (entity.getNoteTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNoteTitle());
        }
        if (entity.getNoteBodyText() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNoteBodyText());
        }
        statement.bindLong(5, entity.getNoteDate());
      }
    };
    this.__deletionAdapterOfNote = new EntityDeletionOrUpdateAdapter<Note>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `notes` WHERE `noteId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Note entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfNote = new EntityDeletionOrUpdateAdapter<Note>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `notes` SET `noteId` = ?,`folderId` = ?,`noteTitle` = ?,`text` = ?,`date` = ? WHERE `noteId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Note entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFolderId());
        if (entity.getNoteTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNoteTitle());
        }
        if (entity.getNoteBodyText() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNoteBodyText());
        }
        statement.bindLong(5, entity.getNoteDate());
        statement.bindLong(6, entity.getId());
      }
    };
  }

  @Override
  public void insertNote(final Note note) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfNote.insert(note);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteNote(final Note note) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfNote.handle(note);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateNote(final Note note) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfNote.handle(note);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Note> getNotes() {
    final String _sql = "SELECT * FROM notes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "noteId");
      final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folderId");
      final int _cursorIndexOfNoteTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "noteTitle");
      final int _cursorIndexOfNoteBodyText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
      final int _cursorIndexOfNoteDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final List<Note> _result = new ArrayList<Note>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Note _item;
        _item = new Note();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpFolderId;
        _tmpFolderId = _cursor.getInt(_cursorIndexOfFolderId);
        _item.setFolderId(_tmpFolderId);
        final String _tmpNoteTitle;
        if (_cursor.isNull(_cursorIndexOfNoteTitle)) {
          _tmpNoteTitle = null;
        } else {
          _tmpNoteTitle = _cursor.getString(_cursorIndexOfNoteTitle);
        }
        _item.setNoteTitle(_tmpNoteTitle);
        final String _tmpNoteBodyText;
        if (_cursor.isNull(_cursorIndexOfNoteBodyText)) {
          _tmpNoteBodyText = null;
        } else {
          _tmpNoteBodyText = _cursor.getString(_cursorIndexOfNoteBodyText);
        }
        _item.setNoteBodyText(_tmpNoteBodyText);
        final long _tmpNoteDate;
        _tmpNoteDate = _cursor.getLong(_cursorIndexOfNoteDate);
        _item.setNoteDate(_tmpNoteDate);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Note getNoteById(final int noteId) {
    final String _sql = "SELECT * FROM notes WHERE noteId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, noteId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "noteId");
      final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folderId");
      final int _cursorIndexOfNoteTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "noteTitle");
      final int _cursorIndexOfNoteBodyText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
      final int _cursorIndexOfNoteDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final Note _result;
      if (_cursor.moveToFirst()) {
        _result = new Note();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpFolderId;
        _tmpFolderId = _cursor.getInt(_cursorIndexOfFolderId);
        _result.setFolderId(_tmpFolderId);
        final String _tmpNoteTitle;
        if (_cursor.isNull(_cursorIndexOfNoteTitle)) {
          _tmpNoteTitle = null;
        } else {
          _tmpNoteTitle = _cursor.getString(_cursorIndexOfNoteTitle);
        }
        _result.setNoteTitle(_tmpNoteTitle);
        final String _tmpNoteBodyText;
        if (_cursor.isNull(_cursorIndexOfNoteBodyText)) {
          _tmpNoteBodyText = null;
        } else {
          _tmpNoteBodyText = _cursor.getString(_cursorIndexOfNoteBodyText);
        }
        _result.setNoteBodyText(_tmpNoteBodyText);
        final long _tmpNoteDate;
        _tmpNoteDate = _cursor.getLong(_cursorIndexOfNoteDate);
        _result.setNoteDate(_tmpNoteDate);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getMaxNoteId() {
    final String _sql = "SELECT MAX(noteId) FROM notes";
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
  public List<Integer> getListOfNoteIds() {
    final String _sql = "SELECT noteId FROM notes";
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
