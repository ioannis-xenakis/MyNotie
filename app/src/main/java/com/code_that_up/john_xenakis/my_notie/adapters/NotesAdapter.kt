package com.code_that_up.john_xenakis.my_notie.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.code_that_up.john_xenakis.my_notie.R
import com.code_that_up.john_xenakis.my_notie.callbacks.MoreMenuButtonListener
import com.code_that_up.john_xenakis.my_notie.callbacks.NoteEventListener
import com.code_that_up.john_xenakis.my_notie.db.NotesDB
import com.code_that_up.john_xenakis.my_notie.model.Folder
import com.code_that_up.john_xenakis.my_notie.model.Note
import com.code_that_up.john_xenakis.my_notie.utils.NoteChangePayload
import com.code_that_up.john_xenakis.my_notie.utils.NoteCornerTreatment
import com.code_that_up.john_xenakis.my_notie.utils.NoteDiffCallback
import com.code_that_up.john_xenakis.my_notie.utils.NoteUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

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
 * <h2>NotesAdapter</h2> is the adapter and the *middle man* between the notes ui and the actual data to be set in notes.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see [https://github.com/ioannis-xenakis/MyNotie](https://github.com/ioannis-xenakis/MyNotie) This project, is uploaded at Github. Visit it if you want!
 */
class NotesAdapter(
    /**
     * The notes list, which is **only displayed** at the screen.
     */
    var notes: ArrayList<Note>,
    /**
     * The current state and **context** of the notes.
     */
    private val context: Context,
    /**
     * The listener for listening clicks, on the *more menu button* (three dots icon button), which is on every note, in notes list.
     */
    private var moreMenuButtonListener: MoreMenuButtonListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    /**
     * The notes list, which is displayed **regardless** of being at the screen.
     */
    @JvmField
    var notesFull: ArrayList<Note> = arrayListOf()

    /**
     * The list that have only the checked notes.
     */
    private var checkedNotes: ArrayList<Note> = arrayListOf()

    /**
     * The NoteEventListener for listening clicks, on a note, in notes list.
     */
    private var listener: NoteEventListener? = null

    /**
     * onCreateViewHolder gets called/runs, when a note is **created**.
     * @param parent the note/group that nests the buttons/content of the note.
     * @param viewType the type of note.
     * @return a new note holder, that holds new content/buttons in note.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.note_layout, parent, false)
        return NoteHolder(v)
    }

    /**
     * Binds the needed data onto the viewHolder/note and displays each note to the proper position.
     * @param holder The holder that holds the data of each note.
     * @param position The position of the note, in the RecyclerView/note list.
     * @param payloads Determines if any change/changes has been made in a note.
     */
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val noteHolder = holder as NoteHolder

        when (val latestPayload = payloads.lastOrNull()) {
            is NoteChangePayload.NoteTitle ->
                noteHolder.noteTitle.text = latestPayload.newNoteTitle

            is NoteChangePayload.NoteBodyText ->
                noteHolder.noteBodyText.text = latestPayload.newNoteBodyText

            is NoteChangePayload.NoteDateEdited ->
                noteHolder.noteDate.text = NoteUtils.dateFromLong(latestPayload.newNoteDateEdited!!)

            else -> onBindViewHolder(holder, position)
        }
    }

    /**
     * onBindViewHolder, binds the needed data or make actions to each button/element, on each note and displays each note to the proper position.
     * @param holder The holder which holds all the content/buttons, in each note.
     * @param position the position of each note, in notes list.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val noteHolder = holder as NoteHolder
        val note = getNote(position)
        //Entering text to each Text fields.
        noteHolder.noteTitle.text = note.noteTitle
        noteHolder.noteBodyText.text = note.noteBodyText
        noteHolder.noteDate.text = NoteUtils.dateFromLong(note.noteDate)

        //When clicking on note(note title, note body text, note date).
        noteHolder.itemView.setOnClickListener {
            listener!!.onNoteClick(
                note,
                noteHolder
            )
        }

        noteHolder.noteCardView.isChecked = note.isChecked

        //When long clicking(holding click) on note(note title, note body text, note date).
        noteHolder.itemView.setOnLongClickListener {
            listener!!.onNoteLongClick(note, noteHolder)
            true
        }

        noteHolder.moreMenu.setOnClickListener { view: View? ->
            moreMenuButtonListener.onMoreMenuButtonClick(
                note,
                view!!,
                noteHolder.bindingAdapterPosition
            )
        }

        hideNoteTitleIfEmpty(noteHolder)
        hideNoteBodyTextIfEmpty(noteHolder)
        addFolderChipsAtEachNote(noteHolder, note)

        setNoteShapeBackground(noteHolder.noteCardView)
    }

    /**
     * getNote, gets a **specific** note from notes list.
     * @param position the position of the note, in notes list.
     * @return the note, depending on its position.
     */
    fun getNote(position: Int): Note {
        return notes[position]
    }

    /**
     * getItemCount, gets and **counts** all the existing notes, from notes list.
     * @return the counted notes, from notes list.
     */
    override fun getItemCount(): Int {
        return notes.size
    }

    /**
     * setListener, sets all listeners for each note.
     * @param listener The NoteEventListener for listening clicks, on a note, in notes list.
     * @param moreMenuButtonListener The listener for listening clicks, on the *more menu button* (three dots icon button), which is on every note, in notes list.
     */
    fun setListener(listener: NoteEventListener?, moreMenuButtonListener: MoreMenuButtonListener) {
        this.listener = listener
        this.moreMenuButtonListener = moreMenuButtonListener
    }

    /**
     * NoteHolder class, holds all the content/buttons, in a note.
     */
    class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * Note title, that the user writes.<br></br>
         */
        var noteTitle: TextView

        /**
         * Note body text, or the main text of note, that the user writes.<br></br>
         */
        var noteBodyText: TextView

        /**
         * Note date is the date which the note has been created/last edited.
         */
        var noteDate: TextView

        /**
         * moreMenu button (three vertical dots icon), that displays the vertical dropdown menu when clicked.
         */
        var moreMenu: FloatingActionButton

        /**
         * Chip group that displays all folders(chips), in a note.
         */
        var folderChipGroup: ChipGroup

        /**
         * noteCardView is the note itself, that contains all the content/buttons.
         */
        @JvmField
        var noteCardView: MaterialCardView

        /**
         * The constructor with initializing note content/buttons, by their ids.
         */
        init {
            noteCardView = itemView.findViewById(R.id.note)
            noteTitle = itemView.findViewById(R.id.note_title)
            noteBodyText = itemView.findViewById(R.id.note_body_text)
            noteDate = itemView.findViewById(R.id.note_date)
            moreMenu = itemView.findViewById(R.id.more_menu_button)
            folderChipGroup = itemView.findViewById(R.id.folder_chip_group)
        }
    }

    /**
     * Sets the note shape.
     * @param noteCardView The cardview of note. Basically the note itself.
     */
    private fun setNoteShapeBackground(noteCardView: MaterialCardView) {
        noteCardView.shapeAppearanceModel = noteCardView.shapeAppearanceModel.toBuilder()
            .setBottomRightCorner(NoteCornerTreatment())
            .build()
    }

    /**
     * Initializes a new, clean, empty list, of the checked notes.
     */
    fun initCheckedNotes() {
        checkedNotes = ArrayList()
    }

    /**
     * getCheckedNotes, **gets** all the already **checked/selected** notes.
     * @return Checked/selected notes.
     */
    fun getCheckedNotes(): ArrayList<Note> {
        return checkedNotes
    }

    /**
     * Sets/replaces/copies the checked notes list.
     * @param listToCheckFrom The list that replaces/copies the checked notes list.
     */
    fun setCheckedNotes(listToCheckFrom: ArrayList<Note>) {
        checkedNotes = listToCheckFrom
    }

    /**
     * setAllCheckedNotes, **selects/checks** or **unselects/unchecks** all *displayed on screen* notes.
     * @param isChecked The checked/selected or unchecked/unselected state of notes.
     */
    fun setAllCheckedNotes(isChecked: Boolean) {
        for (note in notes) {
            checkOrUncheckNote(note, isChecked, 1)
            notifyItemChanged(notes.indexOf(note))
        }
    }

    /**
     * setAllCheckedNotesFull, **selects/checks** or **unselects/unchecks**
     * all notes regardless of whether is *displayed* or not on screen.
     * Checks or unchecks the full *unfiltered* notes list.
     * @param isChecked The boolean state of whether the notes should be checked or not.
     */
    fun setAllCheckedNotesFull(isChecked: Boolean) {
        for (note in notesFull) {
            checkOrUncheckNote(note, isChecked, 1)
            notifyItemChanged(notesFull.indexOf(note))
        }
    }

    /**
     * Checks or unchecks a note.
     * @param note The note to be checked or unchecked.
     * @param isChecked The boolean state of whether the note should be checked or not.
     * @param checkedMode Decides which code should be used for checking/unchecking the note.
     */
    fun checkOrUncheckNote(note: Note, isChecked: Boolean, checkedMode: Int) {
        when (checkedMode) {
            1 -> if (isChecked) {
                if (!checkedNotes.contains(note)) {
                    checkedNotes.add(note)
                }
                note.isChecked = true
            } else {
                checkedNotes.remove(note)
                note.isChecked = false
            }

            2 -> {
                if (isChecked) {
                    checkedNotes.add(note)
                } else {
                    checkedNotes.remove(note)
                }
            }

            else -> Log.d("MyNotie", "Checked mode doesn't exist. Choose 1 or 2.")
        }
    }

    /**
     * getFilter, gets the filter for filtering displayed notes, when the user searches notes.
     * @return the filter for displaying filtered notes.
     */
    override fun getFilter(): Filter {
        return notesFilter
    }

    /**
     * notesFilter, is the filter for filtering displayed notes, when user searches for notes.
     */
    private val notesFilter: Filter = object : Filter() {
        /**
         * performFiltering, performs the main mechanism for filtering displayed notes.
         * @param charSequence The text, typed by user.
         * @return the final filtered notes.
         */
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filteredNotesList = ArrayList<Note>()
            if (charSequence.isEmpty()) {
                filteredNotesList.addAll(notesFull)
            } else {
                val filterPattern =
                    charSequence.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (note in notesFull) {
                    if (note.noteTitle?.lowercase(Locale.getDefault())!!
                            .contains(filterPattern) || note.noteBodyText?.lowercase(
                            Locale.getDefault()
                        )!!.contains(filterPattern)
                    ) {
                        filteredNotesList.add(note)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredNotesList
            return results
        }

        /**
         * publishResults, **displays** the final filtered notes to screen.
         * @param charSequence the user typed text.
         * @param filterResults the final filtered notes.
         */
        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            updateNoteList(filterResults.values as ArrayList<Note>)
        }
    }

    /**
     * The constructor to need, when initializing/creating this *NotesAdapter* class file.
     */
    init {
        notesFull = ArrayList(notes)
    }

    /**
     * Updates/refreshes the *note list(RecyclerView)*.
     * @param newNoteList The new note list containing the new data, to refresh/update to.
     */
    fun updateNoteList(newNoteList: List<Note>) {
        val diffCallback = NoteDiffCallback(notes.toList(), newNoteList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        notes.clear()
        notes.addAll(newNoteList)
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * Updates/refreshes both the *note list* which is displayed on screen
     * and the *full unfiltered note list*
     * which some of its notes might not be displayed on screen.
     * @param newNoteList The new note list containing the new data, to refresh/update to.
     */
    fun updateNoteListAndNotesFull(newNoteList: List<Note>?) {
        val diffCallback = NoteDiffCallback(notes, newNoteList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        notes.clear()
        if (newNoteList != null) {
            notes.addAll(newNoteList)
        }
        notesFull.clear()
        notesFull = ArrayList(notes)
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * Updates/refreshes both the *note list* which is displayed on screen
     * and the *full unfiltered note list*
     * which some of its notes might not be displayed on screen.
     * @param newNoteList The new note list containing the new data, to refresh/update to.
     * @param oldFoldersFromNotes The old folders for each of the notes.
     * @param newFoldersFromNotes The new folders for each of the notes.
     */
    fun updateNoteListAndNotesFull(
        newNoteList: List<Note>,
        oldFoldersFromNotes: ArrayList<List<Folder>>,
        newFoldersFromNotes: ArrayList<List<Folder>>
    ) {
        val diffCallback = NoteDiffCallback(notes, newNoteList, oldFoldersFromNotes, newFoldersFromNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        notes.clear()
        notes.addAll(newNoteList)
        notesFull.clear()
        notesFull = ArrayList(notes)
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * hideNoteTitleIfEmpty, hides Note Title from the screen if it is empty
     * and takes less android device resources.
     * @param noteHolder The holder for all the views inside each note.
     */
    private fun hideNoteTitleIfEmpty(noteHolder: NoteHolder) {
        noteHolder.noteTitle.visibility = View.VISIBLE
        if (noteHolder.noteTitle.text.toString() == "") noteHolder.noteTitle.visibility = View.GONE
    }

    /**
     * hideNoteBodyTextIfEmpty, hides Note Body Text from the screen if it is empty
     * and takes less android device resources.
     * @param noteHolder The holder for all the views inside each note.
     */
    private fun hideNoteBodyTextIfEmpty(noteHolder: NoteHolder) {
        noteHolder.noteBodyText.visibility = View.VISIBLE
        if (noteHolder.noteBodyText.text.toString() == "") noteHolder.noteBodyText.visibility =
            View.GONE
    }

    /**
     * Add folder chips that is connected with each note.
     */
    private fun addFolderChipsAtEachNote(noteHolder: NoteHolder, note: Note) {
        val foldersFromNoteList =
            NotesDB.getInstance(context)!!.notesFoldersJoinDAO()!!.getFoldersFromNote(note.id)
        noteHolder.folderChipGroup.removeAllViews()
        for (folder in foldersFromNoteList) {
            noteHolder.folderChipGroup.addView(object : Chip(context) {
                init {
                    id = ViewCompat.generateViewId()
                    text = folder.folderName!!.trim { it <= ' ' }
                    contentDescription = "Folder chip"
                    isCheckable = false
                    isClickable = false
                }
            })
        }
    }
}