package com.code_that_up.john_xenakis.my_notie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code_that_up.john_xenakis.my_notie.R;
import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO;
import com.code_that_up.john_xenakis.my_notie.model.Folder;
import com.code_that_up.john_xenakis.my_notie.utils.OtherUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
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
 * <h2>FoldersManageFoldersAdapter</h2> is the adapter and the <i>middle man</i> between the folders ui
 * and the actual data to be set to folders, in folders list, from <i>Add Or Manage Folders Activity</i>.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see <a href="https://github.com/ioannis-xenakis/MyNotie">https://github.com/ioannis-xenakis/MyNotie</a> This project, is uploaded at Github. Visit it if you want!
 */
public class FoldersManageFoldersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * The folders list, which is displayed at screen.
     */
    private final ArrayList<Folder> folders;

    /**
     * The current state and <b>context</b> of the folders.
     */
    private final Context context;

    /**
     * Dao needed, for managing folders, in database.
     */
    private final FoldersDAO foldersDao;

    /**
     * The constructor to need, when initializing/creating this <i>FoldersManageFoldersAdapter</i> class.
     * @param folders The folders list.
     * @param context The context/current state of folders.
     * @param foldersDao The dao needed, for managing folders, in database.
     */
    public FoldersManageFoldersAdapter(ArrayList<Folder> folders, Context context, FoldersDAO foldersDao) {
        this.folders = folders;
        this.context = context;
        this.foldersDao = foldersDao;
    }

    /**
     * onCreateViewHolder, gets called/runs, when a folder is <b>created</b>.
     * @param parent The folder/group that nests the buttons/content of the folder.
     * @param viewType The type of a folder.
     * @return A new folder holder, that holds new content/buttons, in folder.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.folder_manage_folders_layout, parent, false);
        return new FolderHolder(v);
    }

    /**
     * onBindViewHolder, binds the needed data or make actions to each button/element, on each folder
     * and displays each folder to the proper position.
     * @param holder The holder which holds all content/buttons, in each folder.
     * @param position The position of each folder, in folders list.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final FolderHolder folderHolder = (FolderHolder) holder;
        final Folder folder = getFolder(position);
        if (folder != null) {
            folderHolder.folderName.setText(folder.getFolderName());

            /*
            Functionality when focusing on "Folder name" Edittext,
            showing "Accept renaming folder" and "Cancel renaming folder" buttons when focused
            and hiding when unfocused.
             */
            folderHolder.folderName.setOnFocusChangeListener((v, hasFocus) -> {
                showAcceptCancelButtons(folderHolder);

                if (!hasFocus) {
                    hideAcceptCancelButtons(folderHolder);
                }
            });

            /*
            Functionality for the Done key in keyboard, when keyboard is opened by user, for typing on "Folder name" Edittext.
            When Done key is clicked, it saves the folder with another name that the user named.
             */
            folderHolder.folderName.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveRenamedFolder(folder, folderHolder);
                    return true;
                }
                return false;
            });

            //Functionality for the "Delete folder" button, that when clicked, deletes the folder with the button that is associated with.
            folderHolder.deleteFolder.setOnClickListener(view -> {
                foldersDao.deleteFolder(folder);
                folders.remove(folder);
                notifyItemRemoved(folderHolder.getBindingAdapterPosition());
            });

            //When "Accept renaming folder" button is clicked, it saves the folder with another name that the user named.
            folderHolder.acceptRenamingFolder.setOnClickListener(view -> saveRenamedFolder(folder, folderHolder));

            /*
            When "Cancel renaming folder" button is clicked, it cancels the already renamed folder
            that the user renamed with "Folder name" Edittext
            and returns to the previous existing folder name.
             */
            folderHolder.cancelRenamingFolder.setOnClickListener(view -> {
                folderHolder.folderName.setText(folder.getFolderName());
                folderHolder.folderName.clearFocus();
                OtherUtils.closeKeyboard(context, folderHolder.folderName);
            });
        }
    }

    /**
     * getItemCount, gets and <b>counts</b> all the existing folders, from folders list.
     * @return The counted folders, from folders list.
     */
    @Override
    public int getItemCount() {
        return folders.size();
    }

    /**
     * Gets folder position, in folders list.
     * @param folder The folder for getting its position.
     * @return The folder position.
     */
    public int getFolderPosition(Folder folder) {
        return folders.indexOf(folder);
    }

    /**
     * Adds a folder, in folders list.
     * @param folder The folder to be added.
     */
    public void addFolder(Folder folder) {
        folders.add(folder);
    }

    /**
     * Shows <i>Accept renaming folder</i> and <i>Cancel renaming folder</i> buttons
     * and hides <i>Delete folder button</i>.
     * @param folderHolder The holder which holds all content/buttons, in a folder.
     */
    private void showAcceptCancelButtons(FolderHolder folderHolder) {
        folderHolder.acceptRenamingFolder.setVisibility(View.VISIBLE);
        folderHolder.cancelRenamingFolder.setVisibility(View.VISIBLE);
        folderHolder.deleteFolder.setVisibility(View.GONE);
    }

    /**
     * Hides <i>Accept renaming folder</i> and <i>Cancel renaming folder</i> buttons
     * and shows <i>Delete folder button</i>.
     * @param folderHolder The holder which holds all content/buttons, in a folder.
     */
    private void hideAcceptCancelButtons(FolderHolder folderHolder) {
        folderHolder.acceptRenamingFolder.setVisibility(View.GONE);
        folderHolder.cancelRenamingFolder.setVisibility(View.GONE);
        folderHolder.deleteFolder.setVisibility(View.VISIBLE);
    }

    /**
     * saveRenamedFolder, saves the renamed folder,
     * renamed by the "Folder name" edittext,
     * the user used to rename the folder.
     * @param folder The folder to be renamed.
     * @param folderHolder The holder of the folder, that holds all the buttons and edittext.
     */
    private void saveRenamedFolder(Folder folder, FolderHolder folderHolder) {
        folder.setFolderName(Objects.requireNonNull(folderHolder.folderName.getText()).toString());
        foldersDao.updateFolder(folder);
        folderHolder.folderName.clearFocus();
        OtherUtils.closeKeyboard(context, folderHolder.folderName);
    }

    /**
     * getFolder, gets a <b>specific</b> folder, from folder list.
     * @param position The position of the folder, in folders list.
     * @return The folder, depending on its position.
     */
    private Folder getFolder(int position) {
        return folders.get(position);
    }

    /**
     * FolderHolder class that holds all content/buttons, in a folder.
     */
    public static class FolderHolder extends RecyclerView.ViewHolder {
        /**
         * The folder name text, that the user writes.
         */
        TextInputEditText folderName;

        /**
         * deleteFolder button, deletes the folder.
         */
        ImageButton deleteFolder;

        /**
         * cancelRenamingFolder button, cancels renaming folder.
         */
        ImageButton cancelRenamingFolder;

        /**
         * acceptRenamingFolder button, accepts and renames folder.
         */
        ImageButton acceptRenamingFolder;

        /**
         * The constructor with initializing folder content/buttons, by their ids.
         * @param itemView The folder.
         */
        public FolderHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folder_name_edittext);
            deleteFolder = itemView.findViewById(R.id.delete_folder_button);
            cancelRenamingFolder = itemView.findViewById(R.id.cancel_renaming_folder_button);
            acceptRenamingFolder = itemView.findViewById(R.id.accept_renaming_folder_button);
        }
    }
}
