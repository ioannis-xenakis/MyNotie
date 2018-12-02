package com.example.fanatic_coder.mynotepad.model;

public class Note {
    private String writeText;
    private long noteDate;

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

    public Note(String writeText, long noteDate) {
        this.writeText = writeText;
        this.noteDate = noteDate;
    }
}
