package com.example.fanatic_coder.mynotepad.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * <h2>Note</h2> is a model class, for managing note/note data
 * and how each note should be structured, in database.
 * @author John/Ioannis Xenakis
 * @version 1.0
 */
@Entity(tableName = "notes")
public class Note {
    /**
     * The primary key and the id number, for identifying a unique note, in <i>notes</i> entity/table.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * The note title, written by the user.
     */
    @ColumnInfo(name = "noteTitle")
    private String noteTitle;

    /**
     * The main text, written by the user.
     */
    @ColumnInfo(name = "text")
    private String noteBodyText;

    /**
     * The note date, when note has been created/edited.
     */
    @ColumnInfo(name = "date")
    private long noteDate;

    /**
     * The column, identifying if the note is checked/selected by the user.
     */
    @Ignore
    private boolean isChecked;

    /**
     * The constructor for the note.
     */
    public Note() {
    }

    /**
     * getNoteTitle, gets and returns the note title, from note.
     * @return the note title text.
     */
    public String getNoteTitle() {
        return noteTitle;
    }

    /**
     * setNoteTitle, sets the note title, in note.
     * @param noteTitle the note title text.
     */
    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    /**
     * getNoteBodyText, gets and returns the main text/body text, from note.
     * @return the main text/body text of note.
     */
    public String getNoteBodyText() {
        return noteBodyText;
    }

    /**
     * setNoteBodyText, sets the main text/body text, in note.
     * @param noteBodyText the main text/body text, of note.
     */
    public void setNoteBodyText(String noteBodyText) {
        this.noteBodyText = noteBodyText;
    }

    /**
     * getNoteDate, gets the note date, from note.
     * @return the note date.
     */
    public long getNoteDate() {
        return noteDate;
    }

    /**
     * setNoteDate, sets the note date, in note.
     * @param noteDate the note date.
     */
    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    /**
     * getId, gets the primary key/unique id of a note.
     * @return the note id number.
     */
    public int getId() {
        return id;
    }

    /**
     * setId, sets the id of a note.
     * @param id the note id number.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * isChecked, gets if the note is checked/selected.
     * @return the variable responsible if the note is checked/selected.
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * setChecked, sets the checked/selected state of the note.
     * @param checked the checked/selected state of note.
     */
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    /**
     * toString, converts any given input, into <i>String</i>
     * @return the notes id and note date.
     */
    @NonNull
    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", noteDate=" + noteDate +
                '}';
    }
}
