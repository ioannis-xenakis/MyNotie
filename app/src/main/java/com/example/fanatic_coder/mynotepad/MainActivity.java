package com.example.fanatic_coder.mynotepad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fanatic_coder.mynotepad.db.NotesDAO;
import com.example.fanatic_coder.mynotepad.db.NotesDB;
import com.example.fanatic_coder.mynotepad.model.Note;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText write_text;
    private NotesDAO dao;

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
        dao = NotesDB.getInstance(this).notesDAO(); //Builds a database and references to it

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
                myCanvas.drawLine(recting.left + 30, baseline + 1, recting.right - 30, baseline + 1, paintOn);
                baseline += getLineHeight();//continue to next line
            }

            super.onDraw(myCanvas);
        }
    }

    //newNoteButton method is called from onClick for the new note button inside content_main.xml
    public void newNoteButton(View view) {

        write_text = findViewById(R.id.Write_Note); //The actual EditText which you write notes
        String clear_text = "";
        FileOutputStream fos;
        DataOutputStream dos;
        boolean newFile;

        try {
            File f = this.getFilesDir();
            String s = f.getCanonicalPath();
            String FILE_NAME = "myNotesFile.txt";
            File file = new File(s + FILE_NAME);
            if(!file.exists()){
                newFile = file.createNewFile();
                if(newFile){
                    System.out.print("File Created Succesfully!");
                }
            }
            // create new note and then save that note.
            fos = new FileOutputStream(file);
            dos = new DataOutputStream(fos);
            dos.write(clear_text.getBytes());
            dos.writeChars("\n \n");
            write_text.setText(clear_text);
            Toast.makeText(this, "New Note Created Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Snackbar.make(view, "Note Creation Failed!", Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //saveNoteButton method is called from onClick for the save note button in content_main.xml
    public void saveNoteButton(View view) {

        try {
            write_text = findViewById(R.id.Write_Note);

            // Get the text from the write_text field convert them all to String and assign them to text variable
            String text = write_text.getText().toString();

            // If the text from write_text field is not empty, and has something in it, do this
            if (!text.isEmpty()) {
                long date = new Date().getTime(); //Get Current date
                Note note = new Note(text, date); //Create a note
                dao.insertNote(note); //Insert the new note to database

                // Popup a window to user, saying: Note Saved Successfully!
                Toast.makeText(this, "Note Saved Successfully!", Toast.LENGTH_LONG).show();

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
        //opens notes_layout.xml for the user to open a note he desires.
        startActivity(new Intent(MainActivity.this, MainNotesActivity.class));
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
