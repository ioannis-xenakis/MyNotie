package com.example.fanatic_coder.mynotepad;

import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


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

    }

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
            int height = getHeight();
            int lineHeight = getLineHeight();

            int count = height / lineHeight;

            Rect recting = myRect;
            Paint paintOn = myPaint;
            int baseline = getLineBounds(0, recting);//first line
            for (int i = 0; i < count; i++) {
                myCanvas.drawLine(recting.left, baseline + 1, recting.right, baseline + 1, paintOn);
                baseline += getLineHeight();//continue to next line
            }

            super.onDraw(myCanvas);
        }
    }

    public void newNoteButton(View view) {
        /*final String clear_text = "";
        final EditText write_text = findViewById(R.id.Write_Note); //The actual EditText which you write notes
        final Button new_note = findViewById(R.id.NewNote); //The button to create a new Note
        new_note.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                write_text.setText(clear_text);
            }
        }); */
        final EditText write_text = findViewById(R.id.Write_Note); //The actual EditText which you write notes
        String clear_text = "";
        FileOutputStream fos;
        DataOutputStream dos;
        try {
            File f = this.getFilesDir();
            String s = f.getCanonicalPath();
            String FILE_NAME = "myNotesFile.txt";
            File file = new File(s + FILE_NAME);
            if(!file.exists()){
                boolean newFile = file.createNewFile();
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

    public void saveNoteButton(View view) {
        final EditText write_text = findViewById(R.id.Write_Note);
        String str_write_text = write_text.getText().toString();
        FileOutputStream fos;
        DataOutputStream dos;

        try {
            File f = this.getFilesDir();
            String s = f.getCanonicalPath();
            String FILE_NAME = "myNotesFile.txt";
            File file = new File(s + FILE_NAME);
            if(!file.exists()){
                boolean newFile = file.createNewFile();
            }
            fos = new FileOutputStream(file);
            dos = new DataOutputStream(fos);
            dos.write(str_write_text.getBytes());
            dos.writeChars("\n \n");
            Toast.makeText(this, "Note Saved Succesfully!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Snackbar.make(view, "Saving Note Failed!", Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
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
