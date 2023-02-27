package ru.vsu.cs.zmaev.diary

import android.app.Application
import androidx.room.Room
import ru.vsu.cs.zmaev.diary.data.AppDatabase
import ru.vsu.cs.zmaev.diary.data.NoteDao
import ru.vsu.cs.zmaev.diary.screens.main.MainActivity

class App : Application() {

    private lateinit var instance : App
    private lateinit var database : AppDatabase
    private lateinit var noteDao : NoteDao

    override fun onCreate() {
        super.onCreate()
        println("\n\n\n----------ABOBA__________")

        instance = this
        database = Room.databaseBuilder(
            this.applicationContext,
            AppDatabase::class.java,
            "app-db-name")
            .allowMainThreadQueries()
            .build()

        noteDao = database.noteDao()
    }

    fun getInstance() : App {
        return this.instance
    }

    fun getNoteDao() : NoteDao {
        return this.noteDao
    }
}