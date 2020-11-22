package com.example.fanatic_coder.mynotepad.callbacks;

import android.view.View;

import com.example.fanatic_coder.mynotepad.model.Note;

public interface MoreMenuButtonListener {
    void onMoreMenuButtonClick(Note note, View view, final int position);
}
