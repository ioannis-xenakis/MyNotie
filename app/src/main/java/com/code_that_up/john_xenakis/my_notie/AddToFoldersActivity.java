package com.code_that_up.john_xenakis.my_notie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.Toast;

import com.code_that_up.john_xenakis.my_notie.adapters.FoldersAddToFoldersAdapter;
import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO;
import com.code_that_up.john_xenakis.my_notie.db.NotesDAO;
import com.code_that_up.john_xenakis.my_notie.db.NotesDB;
import com.code_that_up.john_xenakis.my_notie.db.NotesFoldersJoinDAO;
import com.code_that_up.john_xenakis.my_notie.model.Folder;
import com.code_that_up.john_xenakis.my_notie.model.Note;
import com.code_that_up.john_xenakis.my_notie.utils.FolderUtils;
import com.code_that_up.john_xenakis.my_notie.utils.OtherUtils;
import com.code_that_up.john_xenakis.my_notie.utils.SpacesItemGrid;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
 * <h2>AddToFoldersActivity</h2> is the Activity for adding notes to folders.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see <a href="https://github.com/ioannis-xenakis/MyNotie">https://github.com/ioannis-xenakis/MyNotie</a> This project, is uploaded at Github. Visit it if you want!
 */
public class AddToFoldersActivity extends AppCompatActivity {

    /**
     * The id number of the note, that <i>identifies</i> the note, to be added to folders.
     */
    public static final String NOTE_EXTRA_KEY = "note_id";

    /**
     * Folders list displays all the existing folders, to the screen.
     */
    private RecyclerView foldersListRv;

    /**
     * The dao needed(Data Access Object), for managing folders, in database.
     */
    private FoldersDAO folderDao;

    /**
     * Adapter for folders, which works as an exchange between user interface and data.
     * It manages folders list(RecyclerView) and each folder.
     */
    private FoldersAddToFoldersAdapter foldersAdapter;

    /**
     * The note that will be added to folders.
     */
    private Note note;

    /**
     * onCreate gets called/run, when this <i>Activity</i>(<i>AddToFoldersActivity</i>) first <b>loads/starts/gets created</b>.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_folders);
        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar_add_to_folders);
        final ImageButton add_new_folder_button = findViewById(R.id.add_new_folder_button);
        TextInputEditText name_new_folder_text = findViewById(R.id.name_new_folder_text_input_edittext);
        final ImageButton reject_new_folder_button = findViewById(R.id.reject_new_folder_button);
        final ImageButton accept_new_folder_button = findViewById(R.id.accept_new_folder_button);
        foldersListRv = findViewById(R.id.folder_list_add_to_folders);
        folderDao = NotesDB.getInstance(this).foldersDAO();
        int orientation = getResources().getConfiguration().orientation;
        int smallestScreenWidth = getResources().getConfiguration().smallestScreenWidthDp;
        NotesDAO notesDAO = NotesDB.getInstance(this).notesDAO();

        //Get the note from "My Notes Activity", to be added to folders.
        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_KEY, 0);
            note = notesDAO.getNoteById(id);
        } else {
            Log.e("MyNotie", "Note id is not found/is null.");
        }

        //For different screen orientations(Portrait or Landscape). Mobile phones only and smaller device screen sizes.
        if (orientation == Configuration.ORIENTATION_PORTRAIT && smallestScreenWidth < 600) {
            folderSpacing(1);
            foldersListRv.setLayoutManager(new LinearLayoutManager(this));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE && smallestScreenWidth < 600) {
            folderSpacing(2);
            foldersListRv.setLayoutManager(new GridLayoutManager(this, 2));
        }

        //For different screen orientations(Portrait or Landscape). Mostly for tablets and bigger device screen sizes.
        if (orientation == Configuration.ORIENTATION_PORTRAIT && smallestScreenWidth >= 600) {
            folderSpacing(2);
            foldersListRv.setLayoutManager(new GridLayoutManager(this, 2));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE && smallestScreenWidth >= 600) {
            folderSpacing(3);
            foldersListRv.setLayoutManager(new GridLayoutManager(this, 3));
        }

        loadFolders();

        //Functionality for "Add new folder button" when clicked on it.
        add_new_folder_button.setOnClickListener(view -> {
            name_new_folder_text.requestFocus();
            OtherUtils.openKeyboard(this);
        });

        accept_new_folder_button.setOnClickListener(view -> saveNewFolder(name_new_folder_text));

        /*
        Functionality for the Done key, in keyboard, when keyboard is opened by user,
        for typing on <i>Name new folder text</i> Edittext.
         */
        name_new_folder_text.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveNewFolder(name_new_folder_text);
                return true;
            }
            return false;
        });

        //Functionality for "Reject new folder button" when clicked on it.
        reject_new_folder_button.setOnClickListener(view -> {
            name_new_folder_text.setText("");
            name_new_folder_text.clearFocus();
        });

        /*
        Functionality for showing "Accept new folder" and "Reject new folder" buttons,
        when user types in "Name new folder text" Edittext
        and hides these buttons if "Name new folder text" Edittext doesn't have text in it.
         */
        name_new_folder_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().equals("")) {
                    hideAcceptRejectNewFolderButtons(add_new_folder_button, reject_new_folder_button, accept_new_folder_button);
                } else {
                    showAcceptRejectNewFolderButtons(add_new_folder_button, reject_new_folder_button, accept_new_folder_button);
                }
            }
        });

        /*
        Clicking functionality for Navigation button, on "Top app bar".
        If user clicks on Navigation button, he goes back to the previous Activity,
        the user were before this Activity.
        */
        topAppBar.setNavigationOnClickListener(view -> finish());
    }

    /**
     * saveNewFolder, saves and adds the new folder, to the database and folders list.
     * @param name_new_folder_text The Material Edittext "Name new folder text" for user to type his new folder name.
     */
    private void saveNewFolder(TextInputEditText name_new_folder_text) {
        try {
            OtherUtils.closeKeyboard(this);
            Folder folder = new Folder();
            FolderUtils.increaseFolderIdByOne(folderDao, folder);
            folder.setFolderName(Objects.requireNonNull(name_new_folder_text.getText()).toString());
            folderDao.insertFolder(folder);
            this.foldersAdapter.addFolder(folder);
            this.foldersAdapter.notifyItemInserted(foldersAdapter.getFolderPosition(folder));
            name_new_folder_text.setText("");
            name_new_folder_text.clearFocus();
            Toast.makeText(getApplicationContext(), "New folder created!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Creating new folder failed!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * folderSpacing is for spacing the folders, inside folders list(RecyclerView).
     * @param spanCount The number of columns.
     */
    private void folderSpacing(int spanCount) {
        int folderSpacingInPixels = getResources().getDimensionPixelSize(R.dimen.folder_horizontal_space_add_to_folders);
        this.foldersListRv.addItemDecoration(new SpacesItemGrid(folderSpacingInPixels, spanCount));
    }

    /**
     * Loads/displays/refreshes all folders,
     * in <i>folders list</i>.
     */
    private void loadFolders () {
        NotesFoldersJoinDAO notesFoldersJoinDao = NotesDB.getInstance(this).notesFoldersJoinDAO();
        List<Folder> folderList = folderDao.getAllFolders();
        List<Folder> checkedFolderList = notesFoldersJoinDao.getFoldersFromNote(note.getId());
        ArrayList<Folder> folders = new ArrayList<>(folderList);
        checkAlreadyCheckedFolders(checkedFolderList, folderList);
        this.foldersAdapter = new FoldersAddToFoldersAdapter(folders, checkedFolderList, note, this, folderDao, notesFoldersJoinDao);
        this.foldersListRv.setAdapter(foldersAdapter);
    }

    /**
     * Checks the already checked state of checkbox in a folder and loads all checked folders.
     * @param checkedFolderList The folder list that contains the checked folders.
     * @param folderList The list of all folders that exist.
     */
    private void checkAlreadyCheckedFolders(List<Folder> checkedFolderList, List<Folder> folderList) {
        for (Folder checkedFolder : checkedFolderList) {
            for (Folder folder : folderList) {
                if (checkedFolder.getId() == folder.getId()) {
                    folder.setChecked(true);
                }
            }
        }
    }

    /**
     * hideAcceptRejectNewFolderButtons, hides the "Accept new folder" and "Reject new folder" buttons,
     * and hides the "Add new folder button".
     * @param add_new_folder_button The "Add new folder" button.
     * @param reject_new_folder_button The "Reject new folder" button.
     * @param accept_new_folder_button The "Accept new folder" button.
     */
    private void hideAcceptRejectNewFolderButtons(ImageButton add_new_folder_button,
                                                  ImageButton reject_new_folder_button,
                                                  ImageButton accept_new_folder_button) {
        add_new_folder_button.setVisibility(View.VISIBLE);
        reject_new_folder_button.setVisibility(View.GONE);
        accept_new_folder_button.setVisibility(View.GONE);
    }

    /**
     * showAcceptRejectNewFolderButtons, shows the "Accept new folder" and "Reject new folder" buttons
     * and hides the "Add new folder" button.
     * @param add_new_folder_button The "Add new folder" button.
     * @param reject_new_folder_button The "Reject new folder" button.
     * @param accept_new_folder_button The "Accept new folder" button.
     */
    private void showAcceptRejectNewFolderButtons(ImageButton add_new_folder_button,
                                                  ImageButton reject_new_folder_button,
                                                  ImageButton accept_new_folder_button) {
        add_new_folder_button.setVisibility(View.GONE);
        reject_new_folder_button.setVisibility(View.VISIBLE);
        accept_new_folder_button.setVisibility(View.VISIBLE);
    }
}