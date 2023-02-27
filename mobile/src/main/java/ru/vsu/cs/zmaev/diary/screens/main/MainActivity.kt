package ru.vsu.cs.zmaev.diary.screens.main

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.vsu.cs.zmaev.diary.App
import ru.vsu.cs.zmaev.diary.R
import ru.vsu.cs.zmaev.diary.databinding.ActivityMainBinding
import ru.vsu.cs.zmaev.diary.screens.details.NoteDetailsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var toolbar: Toolbar
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("UNUSED_VARIABLE")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        fab = binding.fab
        toolbar = binding.toolbar
        recyclerView = binding.list
        val linearLayoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            NoteDetailsActivity().start(this, null)
        }

        val adapter = Adapter()
        recyclerView.adapter = adapter

        Application().onCreate()

        val mainViewModel: MainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.noteLiveData.observe(this) { notes ->
            adapter.setItems(notes)
        }
    }
}
