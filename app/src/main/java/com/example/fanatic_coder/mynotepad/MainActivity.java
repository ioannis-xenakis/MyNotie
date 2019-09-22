package com.example.fanatic_coder.mynotepad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fanatic_coder.mynotepad.db.NotesDAO;
import com.example.fanatic_coder.mynotepad.db.NotesDB;
import com.example.fanatic_coder.mynotepad.model.Note;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText note_body_text;
    private EditText note_title;
    private String oldNoteTitle;
    private String oldBodyText;
    private NotesDAO dao;
    private Note temp;
    public static final String NOTE_EXTRA_KEY = "note_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Commented for future reference.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        note_title = findViewById(R.id.noteTitle);
        note_body_text = findViewById(R.id.noteBodyText);
        dao = NotesDB.getInstance(this).notesDAO(); //Builds a database and references to it

        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_KEY, 0);
            temp = dao.getNoteById(id);
            note_title.setText(temp.getNoteTitle());
            note_body_text.setText(temp.getNoteBodyText());
        } else {
            temp = new Note();
        }

        //Listener for note_title field. Functionality for limiting writing not more than 2 lines in field. Every text beyond 2 lines is deleted.
        note_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (null != note_title.getLayout() && note_title.getLayout().getLineCount() > 2) {
                    note_title.getText().delete(note_title.getText().length() - 1, note_title.getText().length());
                }
            }
        });

        //Capturing initial note title text and body text when firstly opening a note.
        oldNoteTitle = note_title.getText().toString();
        oldBodyText = note_body_text.getText().toString();
    }

    //LineEditText class is for drawing lines under text, for the edittext(view) with id, noteBodyText, inside content_main.xml
    public static class LineEditText extends androidx.appcompat.widget.AppCompatEditText {
        public LineEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
                myRect = new Rect();
                myPaint = new Paint();
                myPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                myPaint.setColor(Color.BLACK);
        }

        private Rect myRect;
        private Paint myPaint;

        @Override
        protected  void onDraw(Canvas myCanvas) {
            int count = getBaseline();

            Rect recting = myRect;
            Paint paintOn = myPaint;
            int baseline = getLineBounds(0, recting);//first line
            for (int i = 0; i < count; i++) {
                myCanvas.drawLine(recting.left - 13, baseline + 1, recting.right + 13, baseline + 1, paintOn);
                baseline += getLineHeight();//continue to next line
            }

            super.onDraw(myCanvas);
        }
    }

    //clearTextButton method, clears the note text in note_body_text field
    public void clearTextButton(View view) {
        try {
            String clear_text = "";
            note_body_text = findViewById(R.id.noteBodyText);
            note_body_text.setText(clear_text);
            // Popup a window to user, saying: Text Cleared!
            Toast.makeText(this, "Text Cleared!", Toast.LENGTH_LONG).show();

        } catch (Exception e) { // If something gone wrong and get an error ...
            // Popup a window to user, saying: Clearing Note Text Failed!
            Toast.makeText(this, "Clearing Note Text Failed!", Toast.LENGTH_LONG).show();
            // Print everything about the error at Logcat/Default console window
            e.printStackTrace();
        }
    }

    //GoBack method is for the goBack button in content_main.xml
    public void GoBack(View view) {
        try {
            saveNote();

        } catch (Exception e) { // If something gone wrong, user will get an error ...
            // Popup a window to user, saying: Saving Note Failed!
            Toast.makeText(this, "Saving Note Failed!", Toast.LENGTH_LONG).show();
            // Print everything about the error at Logcat/Default console window
            e.printStackTrace();

            finish2();
        }
    }

    //saveNote is a method for saving note.
    private void saveNote() {
        // Get the text from the note_title field convert them all to String and assign them to noteTitle variable
        String noteTitle =  note_title.getText().toString();
        // Get the text from the note_body_text field convert them all to String and assign them to text variable
        String text = note_body_text.getText().toString();
        long date = new Date().getTime(); //Get Current date

        temp.setNoteTitle(noteTitle);
        temp.setNoteDate(date);
        temp.setNoteBodyText(text);

        //Controlling if user changed initial/old note title or body text, only then save the note, else do nothing and go back to activity_main_notes.xml
        if (!noteTitle.equals("") || !text.equals("") && !oldNoteTitle.equals(noteTitle) || !oldBodyText.equals(text)) {
            //if note doesn't exist and is new, create, else if it is no new and exists, update
            if (temp.getIsNewNote()) {
                //insert new note and save to database
                dao.insertNote(temp);
                //setting the IsNewNote to false, saying that note is no longer new and is old.
                temp.setIsNewNote(false);
            } else if (!temp.getIsNewNote()) {
                //else if it exists update note
                dao.updateNote(temp);
                //setting the IsNewNote to false, saying that note is no longer new and is old.
                temp.setIsNewNote(false);
            }

            //Popup a window to user, saying: Note Saved!
            Toast.makeText(this, "Note Saved!", Toast.LENGTH_LONG).show();

            //After saving, goes back to activity_main_notes.xml for the user to open a note he desires.
            finish2();
        }
        else {
            dao.deleteNote(temp);
            finish2();
        }
    }

    //Overrided finish() method so when user hits cancel button from the android screen Or goBack button, both will save Note or if not made any change just go back to content_main.xml
    @Override
    public void finish() {
        saveNote();
        super.finish();
    }

    //finish2() method should be used in this activity when not wanted to save note too.
    public void finish2() {
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
