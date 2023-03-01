package ru.vsu.cs.zmaev.diary

import android.app.Application
import androidx.room.Room
import ru.vsu.cs.zmaev.diary.data.AppDatabase
import ru.vsu.cs.zmaev.diary.data.NoteDao

class App : Application() {

    private lateinit var database : AppDatabase
    private lateinit var noteDao : NoteDao

    companion object {
        private lateinit var instance : App

        fun getInstance() : App {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(
            this.applicationContext,
            AppDatabase::class.java,
            "app-db-name")
            .allowMainThreadQueries()
            .build()

        noteDao = database.noteDao()
    }

    fun getNoteDao() : NoteDao {
        return this.noteDao
    }
}