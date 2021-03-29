package com.example.fanatic_coder.mynotepad;

import com.example.fanatic_coder.mynotepad.adapters.NotesAdapter;
import com.example.fanatic_coder.mynotepad.callbacks.MoreMenuButtonListener;
import com.example.fanatic_coder.mynotepad.callbacks.NoteEventListener;
import com.example.fanatic_coder.mynotepad.db.NotesDAO;
import com.example.fanatic_coder.mynotepad.db.NotesDB;
import com.example.fanatic_coder.mynotepad.model.Note;
import com.example.fanatic_coder.mynotepad.utils.SpacesItemGrid;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.fanatic_coder.mynotepad.EditNoteActivity.NOTE_EXTRA_KEY;

/**
 * <h2>MyNotesActivity</h2> is the starting home page when <b>My Notie</b> starts
 * and is the page that displays all the existing notes.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see <a href="https://github.com/ioannis-xenakis/MyNotie">https://github.com/ioannis-xenakis/MyNotie</a> This project, is uploaded at Github. Visit it if you want!
 */
public class MyNotesActivity extends AppCompatActivity implements NoteEventListener, MoreMenuButtonListener {

    /**
     * Top bar for users, to search notes.
     */
    private MaterialToolbar searchTopBar;

    /**
     * The default top bar, which only displays the title name, of <i>My notes</i> page.
     */
    private MaterialToolbar pageTitleTopBar;

    /**
     * Top bar for users, to select notes, and act(for ex. delete) on them.
     */
    private MaterialToolbar selectNotesTopBar;

    /**
     * Dao needed for managing notes, in database.
     */
    private NotesDAO dao;

    /**
     * Adapter for notes, which works as an exchange between the user interface and actual data.
     */
    private NotesAdapter adapter;

    /**
     * The RecyclerView, or the <b>notes list</b> which displays all the existing notes, on <b>My notes</b> page.
     */
    private RecyclerView recyclerView;

    /**
     * The layoutManager <b>lays out</b> and manages all notes in an grid/order, in <i>notes list</i>, for <i>My notes</i> page.
     */
    private LinearLayoutManager layoutManager;

    /**
     * The group/layout for the side navigation drawer.
     */
    private DrawerLayout drawerLayout;

    /**
     * The app name.
     */
    public static final String TAG = "MyNotie";

    /**
     * onCreate gets called/run, when MyNotie first <b>loads/starts/gets created</b>.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);
        pageTitleTopBar = findViewById(R.id.top_app_bar_my_notes);

        setSupportActionBar(getAppBar());

        int smallestScreenWidth = getResources().getConfiguration().smallestScreenWidthDp;

        //For different screen orientations(Portrait or Landscape). Mobile phones only and smaller device screen sizes.
        recyclerView = findViewById(R.id.notes_list);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT && smallestScreenWidth < 600) {
            layoutManager = new LinearLayoutManager(this);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE && smallestScreenWidth < 600) {
            layoutManager = new GridLayoutManager(this, 2);
            noteSpacing(recyclerView, 2);
        }

        //For different screen orientations(Portrait or Landscape). Mostly for android tablets and bigger device screen sizes.
        if (orientation == Configuration.ORIENTATION_PORTRAIT && smallestScreenWidth >= 600) {
            layoutManager = new GridLayoutManager(this, 2);
            noteSpacing(recyclerView, 2);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE && smallestScreenWidth >= 600) {
            layoutManager = new GridLayoutManager(this, 3);
            noteSpacing(recyclerView, 3);
        }
        recyclerView.setLayoutManager(layoutManager);

        dao = NotesDB.getInstance(this).notesDAO();
        loadNotes();

        /*
          Note search functionality for search edittext on search top bar.
          Text changed listener runs when text is changed inside a Edittext, either the user changes it, or from a code functionality.
         */
        final EditText searchEdittext = findViewById(R.id.search_edittext);
        searchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(editable.toString());
            }
        });

        /*
          Close Search Top Bar button(X icon button), in Search Top Bar,
          which closes Search Top Bar.
         */
        selectNotesTopBar = findViewById(R.id.top_app_bar_select_notes);
        searchTopBar = findViewById(R.id.top_app_bar_search);
        searchTopBar.setNavigationOnClickListener(view -> {
            closeKeyboard();
            searchEdittext.setText("");
            if (adapter.getCheckedNotes().size() > 0) {
                showSelectNotesTopBar();
            } else {
                showPageTitleTopBar();
            }
        });

        /*
          Close Select Notes Top Bar button, in Select Notes Top Bar,
          which closes Select Notes Top Bar.
         */
        selectNotesTopBar.setNavigationOnClickListener(view -> {
            showPageTitleTopBar();
            adapter.setAllCheckedNotes(false);
            displaySelectedNotesCount();
        });

        //Add new note button, which adds/creates new note.
        FloatingActionButton add_new_note_button = findViewById(R.id.add_new_note);
        add_new_note_button.setOnClickListener(v -> {
            Intent editNoteActivityIntent = new Intent(getApplicationContext(), EditNoteActivity.class);
            startActivity(editNoteActivityIntent);
        });

        //Listens to scrolling at RecyclerView(notes list).
        recyclerView.addOnScrollListener(onScrollListener);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //Opens/Shows and Closes/Hides Side Navigation Drawer.
        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                getAppBar(),
                R.string.open_nav_drawer,
                R.string.close_nav_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //Gets the Header from Navigation Drawer(Navigation View).
        View headerView = navigationView.getHeaderView(0);
        //Gets the close button, from navigation Drawer.
        MaterialButton closeButton = headerView.findViewById(R.id.close_nav_drawer_button);
        //Listens to clicks on close button.
        closeButton.setOnClickListener(view -> {
            //Closes/hides Navigation Drawer from Right/End, to Left/Start.
            drawerLayout.closeDrawer(GravityCompat.START);
        });

    }

    /**
     * getAppBar, gets and returns either the Top Bar, or the Bottom App Bar, depending on the devices screen size.
     * @return the App Bar.
     */
    private Toolbar getAppBar() {
        int smallestScreenWidth = getResources().getConfiguration().smallestScreenWidthDp;
        Toolbar appBar;
        if (smallestScreenWidth < 600) {
            appBar = this.<BottomAppBar>findViewById(R.id.bottom_app_bar);
        } else {
            appBar = this.pageTitleTopBar;
        }
        return appBar;
    }

    /**
     * onScrollListener, Listens for scrolls when user scrolls, on <i>notes list</i>, from <i>My notes</i> Activity.
     */
    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (getResources().getConfiguration().smallestScreenWidthDp < 600) {
                showHideBottomAppBar();
            }
        }
    };

    /**
     * noteSpacing, is for spacing the notes, inside notes list(RecyclerView).
     */
    private void noteSpacing(RecyclerView recyclerView, int spanCount) {
        int noteSpacingInPixels = getResources().getDimensionPixelSize(R.dimen.note_between_horizontal_space);
        recyclerView.addItemDecoration(new SpacesItemGrid(noteSpacingInPixels, spanCount));
    }

    /**
     * closeKeyboard, closes the user's keyboard, if open.
     */
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * showSelectNotesTopBar, shows/displays the top bar that allows user, to select notes.
     */
    private void showSelectNotesTopBar() {
        searchTopBar = findViewById(R.id.top_app_bar_search);
        pageTitleTopBar = findViewById(R.id.top_app_bar_my_notes);
        selectNotesTopBar = findViewById(R.id.top_app_bar_select_notes);

        pageTitleTopBar.setVisibility(View.GONE);
        searchTopBar.setVisibility(View.GONE);
        selectNotesTopBar.setVisibility(View.VISIBLE);
    }

    /**
     * showPageTitleTopBar, shows/displays the default top bar,
     * that only displays the page/Activity title name.
     */
    private void showPageTitleTopBar() {
        searchTopBar = findViewById(R.id.top_app_bar_search);
        pageTitleTopBar = findViewById(R.id.top_app_bar_my_notes);
        selectNotesTopBar = findViewById(R.id.top_app_bar_select_notes);

        selectNotesTopBar.setVisibility(View.GONE);
        searchTopBar.setVisibility(View.GONE);
        pageTitleTopBar.setVisibility(View.VISIBLE);
    }

    /**
     * showSearchTopBar, shows/displays the top bar, that allows user to search notes.
     */
    private void showSearchTopBar() {
        searchTopBar = findViewById(R.id.top_app_bar_search);
        pageTitleTopBar = findViewById(R.id.top_app_bar_my_notes);
        selectNotesTopBar = findViewById(R.id.top_app_bar_select_notes);

        selectNotesTopBar.setVisibility(View.GONE);
        pageTitleTopBar.setVisibility(View.GONE);
        searchTopBar.setVisibility(View.VISIBLE);
    }

    /**
     * displaySelectedNotesCount, displays on the top bar when selecting notes, the number of the checked/selected notes,
     * the user has.
     */
    private void displaySelectedNotesCount() {
        selectNotesTopBar.setTitle(adapter.getCheckedNotes().size() + " notes selected.");
    }

    /**
     * selectNote, selects/checks an existing note to act on it(for ex. delete it).
     * @param note the note.
     * @param noteHolder the holder that holds content/buttons in a note.
     */
    private void selectNote(Note note, NotesAdapter.NoteHolder noteHolder) {
        note.setChecked(!note.isChecked());
        noteHolder.noteCardView.setChecked(note.isChecked());

        if (adapter.getCheckedNotes().size() > 0) {
            showSelectNotesTopBar();
        } else {
            showPageTitleTopBar();
        }
        displaySelectedNotesCount();
    }

    /**
     * onSelectAllNotesButtonClick, runs/is called when <i>select all notes</i> button is clicked,
     * from top bar.
     * <i>Select all notes</i> button allows user, to select all existing notes,
     * in <i>notes list</i>, in <i>My notes</i> Activity.
     * @param menuItem the <i>select all notes</i> button.
     */
    public void onSelectAllNotesButtonClick(MenuItem menuItem) {
        adapter.setAllCheckedNotes(adapter.getCheckedNotes().size() != adapter.notes.size());
        displaySelectedNotesCount();
    }

    /**
     * onDeleteSelectedNotesButtonClick, runs/is called,
     * when <i>delete selected notes</i> button is clicked, from top bar.
     * <i>Delete selected notes</i> button allows user to delete all the selected notes,
     * user have selected.
     * @param menuItem the menu item/button(<i>delete selected notes</i> button).
     */
    public void onDeleteSelectedNotesButtonClick(MenuItem menuItem) {
        ArrayList<Note> deletedNotes = new ArrayList<>(adapter.getCheckedNotes());
        for (Note note : adapter.getCheckedNotes()) {
            int position = adapter.notes.indexOf(note);
            dao.deleteNote(note);
            adapter.notes.remove(note);
            adapter.notesFull.remove(note);
            adapter.notifyItemRemoved(position);
        }

        displaySelectedNotesCount();
        Toast.makeText(MyNotesActivity.this,  deletedNotes.size() + " notes deleted!", Toast.LENGTH_LONG).show();
    }

    /**
     * onSearchButtonClick, runs/is called
     * when <i>search</i> button is clicked, from bottom app bar.
     * <i>Search</i> button, opens the <i>search notes top bar</i>,
     * that allows user to search existing notes.
     * @param menuItem the menu item/button(<i>search</i> button).
     */
    public void onSearchButtonClick(MenuItem menuItem) {
        showSearchTopBar();
    }

    /**
     * onSelectNotesButtonClick, runs/is called
     * when <i>select notes</i> button is clicked, from bottom app bar.
     * <i>Select notes</i> button, opens the <i>select notes top bar</i>,
     * that allows user to select notes.
     * @param item the menu item/button(<i>select notes</i> button).
     */
    public void onSelectNotesButtonClick(MenuItem item) {
        EditText searchEdittext = findViewById(R.id.search_edittext);
        searchEdittext.setText("");

        showSelectNotesTopBar();
    }

    /**
     * loadNotes, loads/displays/refreshes all the existing notes,
     * in <i>notes list</i>.
     */
    private void loadNotes() {
        List<Note> list = dao.getNotes();
        ArrayList<Note> notes = new ArrayList<>(list);
        this.adapter = new NotesAdapter(notes, this, this);
        this.adapter.setListener(this, this);
        this.recyclerView.setAdapter(adapter);
    }

    /**
     * showHideBottomAppBar, shows or hides bottom app bar,
     * (enables/disables auto-hide when scrolling on <i>notes list</i>),
     * depending on total number of notes on screen.
     */
    private void showHideBottomAppBar() {
        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        int visibleNotesCount = layoutManager.getChildCount();
        int totalNotesCount = layoutManager.getItemCount();
        if (visibleNotesCount < totalNotesCount) {
            bottomAppBar.setHideOnScroll(true);
        } else {
            bottomAppBar.setHideOnScroll(false);
            bottomAppBar.performShow();
        }
    }

    /**
     * openNote, opens an existing note, to edit, in EditNoteActivity.java
     */
    private void openNote(Note note) {
        Intent edit = new Intent(this, EditNoteActivity.class);
        edit.putExtra(NOTE_EXTRA_KEY, note.getId());
        startActivity(edit);
    }

    /**
     * onResume, runs/is called, when resuming the app(MyNotie),
     * while android device is on sleep, or opening/switching to/from the app.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadNotes(); //loads/reloads notes from database.
        displaySelectedNotesCount();
    }

    /**
     * onCreateOptionsMenu, runs/is called, when a menu is created.
     * @param menu the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_bottom_top_bar, menu);
        return true;
    }

    /**
     * onNoteClick, runs/is called, when a note is clicked.
     * @param note the note.
     * @param noteHolder the holder that holds content/buttons in a note.
     */
    @Override
    public void onNoteClick(Note note, NotesAdapter.NoteHolder noteHolder) {
        if (adapter.getCheckedNotes().size() > 0 || selectNotesTopBar.getVisibility() == View.VISIBLE)
            selectNote(note, noteHolder);
        else {
            openNote(note);
        }
    }

    /**
     * onNoteLongClick, runs/is called, when a note is long/hold clicked.
     * @param note the note.
     * @param noteHolder the holder that holds content/buttons in a note.
     */
    @Override
    public void onNoteLongClick(final Note note, final NotesAdapter.NoteHolder noteHolder) {
        selectNote(note, noteHolder);
    }

    /**
     * onMoreMenuButtonClick, runs/is called, when <i>more menu</i> button(three vertical dots button) is clicked,
     * revealing a dropdown menu. to act on the specific note, the button is on.
     * @param note the note.
     * @param view the more menu button(three vertical dots button) on note.
     * @param position the position, the note is at the notes list, on <i>My notes</i> page.
     */
    @Override
    public void onMoreMenuButtonClick(final Note note, View view, final int position) {
        PopupMenu notePopupMenu = new PopupMenu(view.getContext(), view);

        notePopupMenu.getMenuInflater().inflate(R.menu.menu_note_more, notePopupMenu.getMenu());
        notePopupMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.add_to_folder_button) {
                Log.d(TAG, "Add to folder button, clicked.");
                return true;
            } else if (itemId == R.id.delete_only_this_note_button) {
                Log.d(TAG, "Delete note button, clicked.");

                dao.deleteNote(note);
                adapter.notes.remove(note);
                adapter.notesFull.remove(note);
                adapter.notifyItemRemoved(position);
                if (note.isChecked())
                    displaySelectedNotesCount();

                if (adapter.getCheckedNotes().size() > 0) {
                    showSelectNotesTopBar();
                } else {
                    showPageTitleTopBar();
                    displaySelectedNotesCount();
                }

                Toast.makeText(MyNotesActivity.this, "Note deleted!", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Delete note button, finished.");
                return true;
            }
            return false;

        });
        notePopupMenu.show();

    }

}
