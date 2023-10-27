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
import com.code_that_up.john_xenakis.my_notie.model.Note;
import com.code_that_up.john_xenakis.my_notie.model.NoteFolderJoin;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NotesFoldersJoinDAO_Impl implements NotesFoldersJoinDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<NoteFolderJoin> __insertionAdapterOfNoteFolderJoin;

  private final EntityDeletionOrUpdateAdapter<NoteFolderJoin> __deletionAdapterOfNoteFolderJoin;

  public NotesFoldersJoinDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNoteFolderJoin = new EntityInsertionAdapter<NoteFolderJoin>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `note_folder_join` (`noteId`,`folderId`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NoteFolderJoin entity) {
        statement.bindLong(1, entity.getNoteId());
        statement.bindLong(2, entity.getFolderId());
      }
    };
    this.__deletionAdapterOfNoteFolderJoin = new EntityDeletionOrUpdateAdapter<NoteFolderJoin>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `note_folder_join` WHERE `noteId` = ? AND `folderId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NoteFolderJoin entity) {
        statement.bindLong(1, entity.getNoteId());
        statement.bindLong(2, entity.getFolderId());
      }
    };
  }

  @Override
  public void insertNoteFolderJoin(final NoteFolderJoin noteFolderJoin) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfNoteFolderJoin.insert(noteFolderJoin);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteNoteNoteFolderJoin(final NoteFolderJoin noteFolderJoin) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfNoteFolderJoin.handle(noteFolderJoin);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Note> getNotesFromFolder(final int folderId) {
    final String _sql = "SELECT * FROM notes INNER JOIN note_folder_join ON notes.noteId=note_folder_join.noteId WHERE note_folder_join.folderId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, folderId);
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
  public List<Folder> getFoldersFromNote(final int noteId) {
    final String _sql = "SELECT * FROM folders INNER JOIN note_folder_join ON folders.folderId=note_folder_join.folderId WHERE note_folder_join.noteId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, noteId);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
