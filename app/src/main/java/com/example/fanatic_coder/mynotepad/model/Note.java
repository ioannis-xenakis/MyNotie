package com.example.fanatic_coder.mynotepad.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "text")
    private String writeText;
    @ColumnInfo(name = "date")
    private long noteDate;

    public Note(String writeText, long noteDate) {
        this.writeText = writeText;
        this.noteDate = noteDate;
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
}
