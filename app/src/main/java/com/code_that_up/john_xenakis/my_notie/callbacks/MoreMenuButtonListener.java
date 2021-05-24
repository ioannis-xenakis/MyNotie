package com.code_that_up.john_xenakis.my_notie.callbacks;

import android.view.View;

import com.code_that_up.john_xenakis.my_notie.MyNotesActivity;
import com.code_that_up.john_xenakis.my_notie.model.Note;
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
 * <h2>MoreMenuButtonListener</h2> is a listener for listening clicks on <i>More Menu Button</i> (three vertical dots button),
 * that is on each note, from <i>MyNotesActivity</i>.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see MyNotesActivity the activity/page which calls this java interface(MoreMenuButtonListener.java) <br>
 * @see <a href="https://github.com/ioannis-xenakis/MyNotie">https://github.com/ioannis-xenakis/MyNotie</a> This project, is uploaded at Github. Visit it if you want!
 */
public interface MoreMenuButtonListener {
    /**
     * onMoreMenuButtonClick, runs/is called, when <i>More Menu Button</i> is clicked.
     * @param note the note.
     * @param view the <i>More Menu Button</i>.
     * @param position the notes position.
     */
    void onMoreMenuButtonClick(Note note, View view, final int position);
}
