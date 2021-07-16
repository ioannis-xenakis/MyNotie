package com.code_that_up.john_xenakis.my_notie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class AddOrManageFoldersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_manage_folders);
        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar_add_or_manage_folders);
        TextInputEditText add_new_folder_text = findViewById(R.id.add_new_folder_text_input_edittext);
        final ImageButton add_new_folder_button = findViewById(R.id.add_new_folder_button);
        final ImageButton reject_new_folder_button = findViewById(R.id.reject_new_folder_button);
        final ImageButton accept_new_folder_button = findViewById(R.id.accept_new_folder_button);

        /*
        Functionality for showing "Accept new folder" and "Reject new folder" buttons,
        when user types in "Add new folder" text field
        and hides these buttons, if "Add new folder" text field doesn't have text in it.
         */
        add_new_folder_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().equals("")) {
                    hideAcceptRejectNewFolderButtons(add_new_folder_button,
                            reject_new_folder_button,
                            accept_new_folder_button);
                } else {
                    showAcceptRejectNewFolderButtons(add_new_folder_button,
                            reject_new_folder_button,
                            accept_new_folder_button);
                }
            }
        });

        topAppBar.setNavigationOnClickListener(view -> finish());
    }

    /**
     * hideAcceptRejectNewFolderButtons, hides the "Accept new folder" and "Reject new folder" buttons,
     * and shows the "Add new folder" button.
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
     * showAcceptRejectNewFolderButtons, shows the "Accept new folder" and "Reject new folder" buttons,
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