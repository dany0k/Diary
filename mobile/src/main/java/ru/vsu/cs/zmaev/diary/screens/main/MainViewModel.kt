package ru.vsu.cs.zmaev.diary.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.vsu.cs.zmaev.diary.App
import ru.vsu.cs.zmaev.diary.model.Note

class MainViewModel : ViewModel() {
    val noteLiveData: LiveData<List<Note>> = App.getInstance().getNoteDao().getAllLiveData()
}