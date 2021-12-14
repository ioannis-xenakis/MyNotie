package com.code_that_up.john_xenakis.my_notie.utils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.code_that_up.john_xenakis.my_notie.model.Note;

import java.util.List;

public class NoteDiffCallback extends DiffUtil.Callback {
    private final List<Note> oldNoteList;
    private final List<Note> newNoteList;

    public NoteDiffCallback(List<Note> oldNoteList, List<Note> newNoteList) {
        this.oldNoteList = oldNoteList;
        this.newNoteList = newNoteList;
    }

    @Override
    public int getOldListSize() {
        return oldNoteList.size();
    }

    @Override
    public int getNewListSize() {
        return newNoteList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNoteList.get(oldItemPosition).getId() == newNoteList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Note oldNote = oldNoteList.get(oldItemPosition);
        final Note newNote = newNoteList.get(newItemPosition);
        return oldNote.getNoteTitle().equals(newNote.getNoteTitle());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
