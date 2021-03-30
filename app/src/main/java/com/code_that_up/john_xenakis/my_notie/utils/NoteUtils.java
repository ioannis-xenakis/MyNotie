package com.code_that_up.john_xenakis.my_notie.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <h2>NoteUtils</h2> is a class, which contains utilities/tools for notes.
 * @author John/Ioannis Xenakis
 * @version 1.0
 */
public class NoteUtils {
    /**
     * dateFromLong, creates and returns a date when the note is created/edited.
     * @param time the time/date the note is created/edited.
     * @return the time/date, the note is created/edited.
     */
    public static String dateFromLong (long time) {
        DateFormat format = new SimpleDateFormat("'Last edited: ' dd/MMM/yyyy ' ' hh:mm", Locale.US);
        return format.format(new Date(time));
    }
}
