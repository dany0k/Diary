package ru.vsu.cs.zmaev.diary.screens.details

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import ru.vsu.cs.zmaev.diary.App
import ru.vsu.cs.zmaev.diary.R
import ru.vsu.cs.zmaev.diary.databinding.ActivityNoteDetailsBinding
import ru.vsu.cs.zmaev.diary.model.Note

class NoteDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailsBinding

    private lateinit var note: Note
    private lateinit var toolbar: Toolbar
    private lateinit var editText: EditText

    fun start(caller: Activity, note: Note?) {
        val intent = Intent(caller, NoteDetailsActivity::class.java)
        intent.putExtra(EXTRA_NOTE, note)
        caller.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityNoteDetailsBinding>(
            this,
            R.layout.activity_note_details)

        toolbar = binding.toolbar
        editText = binding.noteText

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        title = getString(R.string.note_details_title)
        if (intent.hasExtra(EXTRA_NOTE)) {
            note = intent.getParcelableExtra(EXTRA_NOTE)!!
            editText.setText(note.text)
        } else {
            note = Note()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_save -> {
                if (editText.text.isNotEmpty()) {
                    note.text = editText.text.toString()
                    note.done = false
                    note.timestamp = System.currentTimeMillis()
                    if (intent.hasExtra(EXTRA_NOTE)) {
                        App().getInstance().getNoteDao().update(note)
                    } else {
                        App().getInstance().getNoteDao().insert(note)
                    }
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_NOTE: String = "NoteDetailsActivity.EXTRA_NOTE"
    }
}