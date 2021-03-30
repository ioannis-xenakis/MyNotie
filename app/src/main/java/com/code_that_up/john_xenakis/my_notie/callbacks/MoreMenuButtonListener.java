package com.code_that_up.john_xenakis.my_notie.callbacks;

import android.view.View;

import com.code_that_up.john_xenakis.my_notie.MyNotesActivity;
import com.code_that_up.john_xenakis.my_notie.model.Note;

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
