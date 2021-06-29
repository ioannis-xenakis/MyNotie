package com.code_that_up.john_xenakis.my_notie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;

public class AddOrManageFoldersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_manage_folders);
        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar_add_or_manage_folders);

        topAppBar.setNavigationOnClickListener(view -> finish());
    }

}