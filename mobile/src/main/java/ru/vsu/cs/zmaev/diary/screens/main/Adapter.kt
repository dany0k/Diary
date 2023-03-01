package ru.vsu.cs.zmaev.diary.screens.main

import android.app.Activity
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import ru.vsu.cs.zmaev.diary.App
import ru.vsu.cs.zmaev.diary.R
import ru.vsu.cs.zmaev.diary.model.Note
import ru.vsu.cs.zmaev.diary.screens.details.NoteDetailsActivity

class Adapter : RecyclerView.Adapter<Adapter.NoteViewHolder>() {

    private var sortedList: SortedList<Note> = SortedList(Note::class.java, object : SortedListAdapterCallback<Note>(this) {
        override fun compare(o1: Note?, o2: Note?): Int {

            if (o2 != null && o1 != null) {
                if (!o2.done and o1.done) {
                    return 1
                }
                if (o2.done && !o1.done) {
                    return -1
                }
            }
            return (o2!!.timestamp - o1!!.timestamp).toInt()
        }

        override fun onChanged(position: Int, count: Int) {
            notifyItemRangeChanged(position, count)
        }

        override fun areContentsTheSame(oldItem: Note?, newItem: Note?): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(item1: Note?, item2: Note?): Boolean {
            return item1!!.uid == item2!!.uid
        }

        override fun onInserted(position: Int, count: Int) {
            notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            notifyItemRangeRemoved(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            notifyItemMoved(fromPosition, toPosition)
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note_list, parent, false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(sortedList.get(position))
    }

    override fun getItemCount(): Int {
        return sortedList.size()
    }

    fun setItems(notes: List<Note>) {
        sortedList.replaceAll(notes)
    }

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var noteText: TextView = itemView.findViewById(R.id.noteText)
        private var completed: CheckBox = itemView.findViewById(R.id.completedCheckbox)
        private var delete: View = itemView.findViewById(R.id.deleteImageView)

        private lateinit var note: Note
        private var silentUpdate : Boolean = false

        init {

            itemView.setOnClickListener {
                NoteDetailsActivity().start(itemView.context as Activity, note)
            }

            delete.setOnClickListener {
                App.getInstance().getNoteDao().delete(note)
            }

            completed.setOnCheckedChangeListener { _, isClicked ->
                if (!silentUpdate) {
                    note.done = isClicked
                    App.getInstance().getNoteDao().update(note)
                }
                updateStrokeOut()
            }
        }

        fun bind(note: Note) {
            this.note = note
            noteText.text = note.text
            updateStrokeOut()
            silentUpdate = true
            completed.isChecked = note.done
            silentUpdate = false
        }

        private fun updateStrokeOut() {
            if (note.done) {
                noteText.paintFlags = noteText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                noteText.paintFlags = noteText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }
}