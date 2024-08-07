package com.code_that_up.john_xenakis.my_notie.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

/*
    My Notie is a note taking app, write notes and save them to see them and remember later.
    Copyright (C) 2021  Ioannis Xenakis

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

    Anything you want to contact me for, contact me with an e-mail, at: Xenakis.i.Contact@gmail.com
    I'll be happy to help you, or discuss anything with you! */
/**
 * <h2>Note</h2> is a model class, for managing note/note data
 * and how each note should be structured, in database.
 * @author John/Ioannis Xenakis
 * @version 1.0
 */
@Parcelize
@Entity(tableName = "notes")
class Note(): Parcelable
{
    /**
     * The primary key and the id number, for identifying a unique note, in *notes* entity/table.
     */
    @ColumnInfo(name = "noteId")
    @PrimaryKey
    var id = 0

    /**
     * The folder id/the folder, associated with the note.
     */
    @ColumnInfo(name = "folderId")
    var folderId = 0

    /**
     * The note title, written by the user.
     */
    @ColumnInfo(name = "noteTitle")
    var noteTitle: String? = null

    /**
     * The main text, written by the user.
     */
    @ColumnInfo(name = "text")
    var noteBodyText: String? = null

    /**
     * The note date, when note has been created/edited.
     */
    @ColumnInfo(name = "date")
    var noteDate: Long = 0

    /**
     * The column, identifying if the note is checked/selected by the user.
     */
    @Ignore
    var isChecked = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        folderId = parcel.readInt()
        noteTitle = parcel.readString()
        noteBodyText = parcel.readString()
        noteDate = parcel.readLong()
        isChecked = parcel.readByte() != 0.toByte()
    }

    /**
     * toString, converts any given input, into *String*
     * @return the notes id and note date.
     */
    override fun toString(): String {
        return "Note{" +
                "id=" + id +
                ", noteDate=" + noteDate +
                '}'
    }

    companion object : Parceler<Note> {

        override fun Note.write(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeInt(folderId)
            parcel.writeString(noteTitle)
            parcel.writeString(noteBodyText)
            parcel.writeLong(noteDate)
            parcel.writeByte(if (isChecked) 1 else 0)
        }

        override fun create(parcel: Parcel): Note {
            return Note(parcel)
        }
    }

    override fun equals(other: Any?): Boolean = (other is Note)
            && this.id == other.id

    override fun hashCode(): Int {
        return id
    }
}