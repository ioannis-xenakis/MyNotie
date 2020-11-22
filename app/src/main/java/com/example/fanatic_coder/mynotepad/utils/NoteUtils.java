package com.example.fanatic_coder.mynotepad.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteUtils {
    public static String dateFromLong (long time) {
        DateFormat format = new SimpleDateFormat("'Last edited: ' dd/MMM/yyyy ' ' hh:mm", Locale.US);
        return format.format(new Date(time));
    }
}
