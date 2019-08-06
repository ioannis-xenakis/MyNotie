package com.example.fanatic_coder.mynotepad.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "isNewNote")
    private Boolean isNewNote = true;
    @ColumnInfo(name = "noteTitle")
    private String noteTitle;
    @ColumnInfo(name = "text")
    private String writeText;
    @ColumnInfo(name = "date")
    private long noteDate;
    @Ignore
    private boolean isChecked;

    public Note() {
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getWriteText() {
        return writeText;
    }

    public void setWriteText(String writeText) {
        this.writeText = writeText;
    }

    public long getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public Boolean getIsNewNote() {
        return isNewNote;
    }

    public void setIsNewNote(@NonNull Boolean newNote) {
        isNewNote = newNote;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", noteDate=" + noteDate +
                '}';
    }
}
