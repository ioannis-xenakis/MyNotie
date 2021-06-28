package com.code_that_up.john_xenakis.my_notie.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
 * <h2>OtherUtils</h2> is a class for containing various and different utilities,
 * that cannot be contained/grouped elsewhere.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see <a href="https://github.com/ioannis-xenakis/MyNotie">https://github.com/ioannis-xenakis/MyNotie</a> This project, is uploaded at Github. Visit it if you want!
 */
public class OtherUtils {

    /**
     * closeKeyboard, closes the keyboard, if open.
     * @param activity The activity, the keyboard is on.
     */
    public static void closeKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * closeKeyboard, closes the keyboard, if open.
     * @param context The context of the activity, the keyboard is on.
     * @param view The view(for ex. an Edittext or a Button).
     */
    public static void closeKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * openKeyboard, opens/shows the keyboard, if closed.
     * @param activity The activity, the keyboard is on.
     */
    public static void openKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null) {
                imm.showSoftInput(view, 0);
            }
        }
    }

}
