package com.code_that_up.john_xenakis.my_notie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code_that_up.john_xenakis.my_notie.R;
import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO;
import com.code_that_up.john_xenakis.my_notie.db.NotesFoldersJoinDAO;
import com.code_that_up.john_xenakis.my_notie.model.Folder;
import com.code_that_up.john_xenakis.my_notie.model.Note;
import com.code_that_up.john_xenakis.my_notie.model.NoteFolderJoin;
import com.code_that_up.john_xenakis.my_notie.utils.OtherUtils;
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
 * <h2>FoldersAddToFoldersAdapter</h2> is the adapter and the <i>middle man</i> between the folders ui
 * and the actual data to be set to folders, in <i>Folders list(RecyclerView)</i>, from <i>Add To Folders Activity</i>.
 * <i>FoldersAddToFoldersAdapter</i>, is responsible for a RecyclerView(<i>Folders list</i>) and managing its data, in each folder.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see <a href="https://github.com/ioannis-xenakis/MyNotie">https://github.com/ioannis-xenakis/MyNotie</a> This project, is uploaded at Github. Visit it if you want!
 */
public class FoldersAddToFoldersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * The current state of the Activity(<i>AddToFoldersActivity</i>) that is associated with this RecyclerView Adapter.
     */
    private final Context context;

    /**
     * The folders list, which is displayed at screen.
     */
    private final ArrayList<Folder> folders;

    /**
     * The checked folders of the folders list.
     */
    private final List<Folder> checkedFolders;

    /**
     * The note to be added to folders.
     */
    private final Note note;

    /**
     * Dao(Data Access Object) needed for managing folders, in database.
     */
    private final FoldersDAO foldersDao;

    /**
     * Dao(Data Access Object) needed for managing the links between a note and a folder, in database.
     */
    private final NotesFoldersJoinDAO notesFoldersJoinDao;

    /**
     * The constructor needed, for initializing/creating this <i>FoldersAddToFoldersAdapter</i> class.
     * @param folders The folders list, displayed on screen.
     * @param context The <i>context/current state</i> of an Activity(<i>AddToFoldersActivity</i>), that is associated with this RecyclerView Adapter.
     */
    public FoldersAddToFoldersAdapter(ArrayList<Folder> folders, List<Folder> checkedFolders, Note note, Context context,
                                      FoldersDAO foldersDao, NotesFoldersJoinDAO notesFoldersJoinDao) {
        this.folders = folders;
        this.checkedFolders = checkedFolders;
        this.note = note;
        this.context = context;
        this.foldersDao = foldersDao;
        this.notesFoldersJoinDao = notesFoldersJoinDao;
    }

    /**
     * OnCreateViewHolder gets called/runs, when a folder is <i>created</i>.
     * @param parent The folder/group that nests the buttons/content of the folder.
     * @param viewType The type of a folder.
     * @return A new folder holder that holds content/buttons in a folder. A new folder with it's content/buttons(views).
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.folder_add_to_folders_layout, parent, false);
        return new FolderHolder(v, this, notesFoldersJoinDao);
    }

    /**
     * onBindViewHolder, binds the needed data or make actions to each button/element(view), on each folder
     * and displays each folder to the proper position.
     * @param holder The holder which holds all content/buttons(called views), in each folder.
     * @param position The position of each folder, in folders list.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final FolderHolder folderHolder = (FolderHolder) holder;
        final Folder folder = getFolder(position);
        if (folder != null) {
            folderHolder.folderName.setText(folder.getFolderName());

            //Load up the already checked folders when "Add To Folders Activity" loads up and check them.
            folderHolder.setData(note, folder);
            folderHolder.checkBoxAddToFolders.setChecked(folder.isChecked());
        }
    }

    /**
     * getFolder, gets a <i>specific</i> folder, from a <i>specific</i> position, in folders list.
     * @param position The <i>position</i>(integer) the folder is located in folders list.
     * @return The folder.
     */
    private Folder getFolder(int position) {
        return folders.get(position);
    }

    /**
     * Adds a folder, in folder list.
     * @param folder The folder to be added.
     */
    public void addFolder(Folder folder) {
        folders.add(folder);
    }

    /**
     * Gets folder position for a <i>specific</i> folder, in <i>folders list</i>.
     * @param folder The folder for getting its position.
     * @return The folders position.
     */
    public int getFolderPosition(Folder folder) {
        return folders.indexOf(folder);
    }

    /**
     * Adds the folder to be checked to checked folders list.
     * @param folder The folder to be checked.
     */
    public void addCheckedFolder(Folder folder) {
        checkedFolders.add(folder);
    }

    /**
     * Removes the checked folder to be unchecked, from checked folders list.
     * @param folder The checked folder.
     */
    public void removeCheckedFolder(Folder folder) {
        checkedFolders.remove(folder);
    }

    /**
     * Gets and returns the sum of all folders or how many the folders are, in the database/folders list,
     * displayed in <i>folders list</i> RecyclerView.
     * @return The sum number of all the folders in folders list.
     */
    @Override
    public int getItemCount() {
        return folders.size();
    }

    /**
     * FolderHolder, that holds all content/buttons(views), in a folder.
     */
    public static class FolderHolder extends RecyclerView.ViewHolder {
        /**
         * The folder name that user typed;
         */
        TextInputEditText folderName;

        /**
         * Checkbox adds/removes the note from the folder.
         */
        CheckBox checkBoxAddToFolders;

        /**
         * CancelRenamingFolder button, cancels renaming folder and reverts back the folder name to the previous name.
         */
        ImageButton cancelRenamingFolder;

        /**
         * AcceptRenamingFolder button, accepts and saves the folder.
         */
        ImageButton acceptRenamingFolder;

        /**
         * The adapter for the recyclerview(folders), in AddToFoldersActivity.
         */
        FoldersAddToFoldersAdapter adapter;

        /**
         * The note to be added in the folders.
         */
        Note note;

        /**
         * The folder that is in folders list(RecyclerView), from AddToFoldersActivity.
         */
        Folder folder;

        /**
         * The Data Object Access("Dao" for short) for having a connection between a note and a folder
         * and manipulating them(adding/deleting/updating/querying) in the database.
         */
        NotesFoldersJoinDAO notesFoldersJoinDao;

        /**
         * The constructor needed, for initializing/creating this <i>FolderHolder</i> class.
         * @param itemView The folder.
         * @param adapter The adapter for the recyclerview(folders), in AddToFoldersActivity.
         * @param notesFoldersJoinDao Dao(Data Access Object) needed for managing the links between a note and a folder, in database.
         */
        public FolderHolder(@NonNull View itemView, FoldersAddToFoldersAdapter adapter, NotesFoldersJoinDAO notesFoldersJoinDao) {
            super(itemView);
            checkBoxAddToFolders = itemView.findViewById(R.id.checkbox_add_to_folders);
            cancelRenamingFolder = itemView.findViewById(R.id.cancel_renaming_folder_button);
            acceptRenamingFolder = itemView.findViewById(R.id.accept_renaming_folder_button);
            folderName = itemView.findViewById(R.id.folder_name_edittext);
            this.notesFoldersJoinDao = notesFoldersJoinDao;
            this.adapter = adapter;

            /*
            Managing check states of "checkBox Add To Folders" and what should do when it is checked or not.
            Add note to folder when checkBox is checked.
            Remove note from folder when checkBox is unchecked.
             */
            checkBoxAddToFolders.setOnClickListener(v -> {
                boolean isChecked = checkBoxAddToFolders.isChecked();
                NoteFolderJoin noteFolderJoin = new NoteFolderJoin(note.getId(), folder.getId());
                if (isChecked) {
                    folder.setChecked(true);
                    adapter.addCheckedFolder(folder);
                    notesFoldersJoinDao.insertNoteFolderJoin(noteFolderJoin);
                } else {
                    folder.setChecked(false);
                    adapter.removeCheckedFolder(folder);
                    notesFoldersJoinDao.deleteNoteNoteFolderJoin(noteFolderJoin);
                }
            });

            /*
            Functionality when focusing on "Folder name" Edittext,
            showing "Accept renaming folder" and "Cancel renaming folder" buttons
            and hiding them when unfocused.
             */
            folderName.setOnFocusChangeListener((v, hasFocus) -> {
                showAcceptCancelButtons();

                if (!hasFocus) {
                    hideAcceptCancelButtons();
                }
            });

            /*
            Functionality for the Done key in keyboard, when keyboard is opened by user,
            for typing on "Folder name" Edittext.
            When Done key is pressed, it saves the folder with another name that the user named.
             */
            folderName.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveRenamedFolder(folder);
                    return true;
                }
                return false;
            });

            //When "Accept renaming folder" button is clicked, it saves the folder with another name that the user gave.
            acceptRenamingFolder.setOnClickListener(view -> saveRenamedFolder(folder));

            /*
            When "Cancel renaming folder" button is clicked, it cancels the already renamed folder
            that the user renamed with "Folder name" Edittext
            and returns to the previous existing folder name.
             */
            cancelRenamingFolder.setOnClickListener(view -> {
                folderName.setText(folder.getFolderName());
                folderName.clearFocus();
                OtherUtils.closeKeyboard(adapter.context, folderName);
            });
        }

        /**
         * Sets the needed data/objects for the <i>FolderHolder</i> class(this class) to
         * @param note The note to be added to folders.
         * @param folder The folder to add the note to.
         */
        public void setData(Note note, Folder folder) {
            this.note = note;
            this.folder = folder;
        }

        /**
         * Shows <i>Accept renaming folder</i> and <i>Cancel renaming folder</i> buttons
         * and hides <i>Checkbox add to folders</i>.
         */
        private void showAcceptCancelButtons() {
            acceptRenamingFolder.setVisibility(View.VISIBLE);
            cancelRenamingFolder.setVisibility(View.VISIBLE);
            checkBoxAddToFolders.setVisibility(View.GONE);
        }

        /**
         * Hides <i>Accept renaming folder</i> and <i>Cancel renaming folder</i> buttons
         * and shows <i>Checkbox add to folders</i>.
         */
        private void hideAcceptCancelButtons() {
            acceptRenamingFolder.setVisibility(View.GONE);
            cancelRenamingFolder.setVisibility(View.GONE);
            checkBoxAddToFolders.setVisibility(View.VISIBLE);
        }

        /**
         * saveRenamedFolder, saves the renamed folder,
         * renamed by the "Folder name" edittext,
         * the user used to rename the folder.
         * @param folder The folder to be renamed.
         */
        private void saveRenamedFolder(Folder folder) {
            folder.setFolderName(Objects.requireNonNull(folderName.getText()).toString());
            adapter.foldersDao.updateFolder(folder);
            folderName.clearFocus();
            OtherUtils.closeKeyboard(adapter.context, folderName);
        }
    }
}
