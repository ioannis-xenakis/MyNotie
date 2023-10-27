package com.code_that_up.john_xenakis.my_notie.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NotesDB_Impl extends NotesDB {
  private volatile NotesDAO _notesDAO;

  private volatile FoldersDAO _foldersDAO;

  private volatile NotesFoldersJoinDAO _notesFoldersJoinDAO;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `notes` (`noteId` INTEGER NOT NULL, `folderId` INTEGER NOT NULL, `noteTitle` TEXT, `text` TEXT, `date` INTEGER NOT NULL, PRIMARY KEY(`noteId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `folders` (`folderId` INTEGER NOT NULL, `folderName` TEXT, `checked` INTEGER NOT NULL, PRIMARY KEY(`folderId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `note_folder_join` (`noteId` INTEGER NOT NULL, `folderId` INTEGER NOT NULL, PRIMARY KEY(`noteId`, `folderId`), FOREIGN KEY(`noteId`) REFERENCES `notes`(`noteId`) ON UPDATE SET NULL ON DELETE CASCADE , FOREIGN KEY(`folderId`) REFERENCES `folders`(`folderId`) ON UPDATE SET NULL ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_note_folder_join_noteId` ON `note_folder_join` (`noteId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_note_folder_join_folderId` ON `note_folder_join` (`folderId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '22504cb73cf3d2fa3f1c0fbce2ced191')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `notes`");
        db.execSQL("DROP TABLE IF EXISTS `folders`");
        db.execSQL("DROP TABLE IF EXISTS `note_folder_join`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsNotes = new HashMap<String, TableInfo.Column>(5);
        _columnsNotes.put("noteId", new TableInfo.Column("noteId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("folderId", new TableInfo.Column("folderId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("noteTitle", new TableInfo.Column("noteTitle", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("text", new TableInfo.Column("text", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotes = new TableInfo("notes", _columnsNotes, _foreignKeysNotes, _indicesNotes);
        final TableInfo _existingNotes = TableInfo.read(db, "notes");
        if (!_infoNotes.equals(_existingNotes)) {
          return new RoomOpenHelper.ValidationResult(false, "notes(com.code_that_up.john_xenakis.my_notie.model.Note).\n"
                  + " Expected:\n" + _infoNotes + "\n"
                  + " Found:\n" + _existingNotes);
        }
        final HashMap<String, TableInfo.Column> _columnsFolders = new HashMap<String, TableInfo.Column>(3);
        _columnsFolders.put("folderId", new TableInfo.Column("folderId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolders.put("folderName", new TableInfo.Column("folderName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolders.put("checked", new TableInfo.Column("checked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFolders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFolders = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFolders = new TableInfo("folders", _columnsFolders, _foreignKeysFolders, _indicesFolders);
        final TableInfo _existingFolders = TableInfo.read(db, "folders");
        if (!_infoFolders.equals(_existingFolders)) {
          return new RoomOpenHelper.ValidationResult(false, "folders(com.code_that_up.john_xenakis.my_notie.model.Folder).\n"
                  + " Expected:\n" + _infoFolders + "\n"
                  + " Found:\n" + _existingFolders);
        }
        final HashMap<String, TableInfo.Column> _columnsNoteFolderJoin = new HashMap<String, TableInfo.Column>(2);
        _columnsNoteFolderJoin.put("noteId", new TableInfo.Column("noteId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNoteFolderJoin.put("folderId", new TableInfo.Column("folderId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNoteFolderJoin = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysNoteFolderJoin.add(new TableInfo.ForeignKey("notes", "CASCADE", "SET NULL", Arrays.asList("noteId"), Arrays.asList("noteId")));
        _foreignKeysNoteFolderJoin.add(new TableInfo.ForeignKey("folders", "CASCADE", "SET NULL", Arrays.asList("folderId"), Arrays.asList("folderId")));
        final HashSet<TableInfo.Index> _indicesNoteFolderJoin = new HashSet<TableInfo.Index>(2);
        _indicesNoteFolderJoin.add(new TableInfo.Index("index_note_folder_join_noteId", false, Arrays.asList("noteId"), Arrays.asList("ASC")));
        _indicesNoteFolderJoin.add(new TableInfo.Index("index_note_folder_join_folderId", false, Arrays.asList("folderId"), Arrays.asList("ASC")));
        final TableInfo _infoNoteFolderJoin = new TableInfo("note_folder_join", _columnsNoteFolderJoin, _foreignKeysNoteFolderJoin, _indicesNoteFolderJoin);
        final TableInfo _existingNoteFolderJoin = TableInfo.read(db, "note_folder_join");
        if (!_infoNoteFolderJoin.equals(_existingNoteFolderJoin)) {
          return new RoomOpenHelper.ValidationResult(false, "note_folder_join(com.code_that_up.john_xenakis.my_notie.model.NoteFolderJoin).\n"
                  + " Expected:\n" + _infoNoteFolderJoin + "\n"
                  + " Found:\n" + _existingNoteFolderJoin);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "22504cb73cf3d2fa3f1c0fbce2ced191", "7e2099072662468bd18bf06da1515992");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "notes","folders","note_folder_join");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `notes`");
      _db.execSQL("DELETE FROM `folders`");
      _db.execSQL("DELETE FROM `note_folder_join`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(NotesDAO.class, NotesDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(FoldersDAO.class, FoldersDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(NotesFoldersJoinDAO.class, NotesFoldersJoinDAO_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    _autoMigrations.add(new NotesDB_AutoMigration_1_2_Impl());
    return _autoMigrations;
  }

  @Override
  public NotesDAO notesDAO() {
    if (_notesDAO != null) {
      return _notesDAO;
    } else {
      synchronized(this) {
        if(_notesDAO == null) {
          _notesDAO = new NotesDAO_Impl(this);
        }
        return _notesDAO;
      }
    }
  }

  @Override
  public FoldersDAO foldersDAO() {
    if (_foldersDAO != null) {
      return _foldersDAO;
    } else {
      synchronized(this) {
        if(_foldersDAO == null) {
          _foldersDAO = new FoldersDAO_Impl(this);
        }
        return _foldersDAO;
      }
    }
  }

  @Override
  public NotesFoldersJoinDAO notesFoldersJoinDAO() {
    if (_notesFoldersJoinDAO != null) {
      return _notesFoldersJoinDAO;
    } else {
      synchronized(this) {
        if(_notesFoldersJoinDAO == null) {
          _notesFoldersJoinDAO = new NotesFoldersJoinDAO_Impl(this);
        }
        return _notesFoldersJoinDAO;
      }
    }
  }
}
