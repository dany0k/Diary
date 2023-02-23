package ru.vsu.cs.zmaev.diary

import android.app.Activity
import android.os.Bundle
import ru.vsu.cs.zmaev.diary.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}