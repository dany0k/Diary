package ru.vsu.cs.zmaev.diary.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.vsu.cs.zmaev.diary.model.Note

@Database(entities = [ Note::class ], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

//    fun getInstance(context: Context) : AppDatabase {
//        if (INSTANCE == null) {
//            INSTANCE = Room.databaseBuilder(
//                context.applicationContext,
//                AppDatabase::class.java,
//                "app-db-name")
//                .allowMainThreadQueries()
//                .build()
//        }
//        return INSTANCE!!
//    }

//    companion object {
//        var INSTANCE : AppDatabase? = null
//    }
}