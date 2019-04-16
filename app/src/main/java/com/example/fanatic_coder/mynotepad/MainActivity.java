package com.example.fanatic_coder.mynotepad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
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

    private EditText write_text;
    private NotesDAO dao;
    private Note temp;
    public static final String NOTE_EXTRA_KEY = "note_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        write_text = findViewById(R.id.Write_Note);
        dao = NotesDB.getInstance(this).notesDAO(); //Builds a database and references to it

        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_KEY, 0);
            temp = dao.getNoteById(id);
            write_text.setText(temp.getWriteText());
        } else {
            temp = new Note();
        }

    }

    //LineEditText class is for drawing lines under text, for the edittext(view) with id, Write_Note, inside content_main.xml
    public static class LineEditText extends android.support.v7.widget.AppCompatEditText {
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

    //clearTextButton method, clears the note text in write_text field
    public void clearTextButton(View view) {
        try {
            String clear_text = "";
            write_text = findViewById(R.id.Write_Note);
            write_text.setText(clear_text);
            // Popup a window to user, saying: Text Cleared!
            Toast.makeText(this, "Text Cleared!", Toast.LENGTH_LONG).show();

        } catch (Exception e) { // If something gone wrong and get an error ...
            // Popup a window to user, saying: Clearing Note Text Failed!
            Toast.makeText(this, "Clearing Note Text Failed!", Toast.LENGTH_LONG).show();
            // Print everything about the error at Logcat/Default console window
            e.printStackTrace();
        }
    }

    //saveNoteButton method is called from onClick for the save note button in content_main.xml
    public void saveNoteButton(View view) {

        try {
            // Get the text from the write_text field convert them all to String and assign them to text variable
            String text = write_text.getText().toString();

            // If the text from write_text field is not empty, and has something in it, do this
            if (!text.isEmpty()) {
                long date = new Date().getTime(); //Get Current date

                temp.setNoteDate(date);
                temp.setWriteText(text);

                //if note doesn't exist and is new, create, else if it is no new and exists, update
                if (temp.getIsNewNote()) {
                    //insert new note and save to database
                    dao.insertNote(temp);
                    //setting the IsNewNote to false, saying that note is no longer new and is old.
                    temp.setIsNewNote(false);
                } else if (!temp.getIsNewNote()) {
                    dao.updateNote(temp); //else if it exists update note
                    temp.setIsNewNote(false);
                }

                //Popup a window to user, saying: Note Saved!
                Toast.makeText(this, "Note Saved!", Toast.LENGTH_LONG).show();

                finish();
            }
            else {
                Toast.makeText(this, "Your note is empty! Please write something to be saved!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) { // If something gone wrong and get an error ...
            // Popup a window to user, saying: Saving Note Failed!
            Toast.makeText(this, "Saving Note Failed!", Toast.LENGTH_LONG).show();
            // Print everything about the error at Logcat/Default console window
            e.printStackTrace();

        }
    }

    //openNoteButton method is for the openNote button in content_main.xml
    public void openNoteButton(View view) {
        //goes back to notes_layout.xml for the user to open a note he desires.
        finish();
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
