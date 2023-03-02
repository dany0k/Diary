package ru.vsu.cs.zmaev.diary.screens.main

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.wearable.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.vsu.cs.zmaev.diary.R
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import ru.vsu.cs.zmaev.diary.databinding.ActivityMainBinding
import ru.vsu.cs.zmaev.diary.screens.details.NoteDetailsActivity

class MainActivity : AppCompatActivity(), OnDataChangedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var toolbar: Toolbar
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    private lateinit var logger: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        fab = binding.fab
        toolbar = binding.toolbar
        recyclerView = binding.list
        logger = binding.logger

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

        binding.sendButton.setOnClickListener {
            val message = "Hello $num"
            sendData(message)
            num++
        }
    }

    // add data listener
    public override fun onResume() {
        super.onResume()
        Wearable.getDataClient(this).addListener(this)
    }

    //remove data listener
    public override fun onPause() {
        super.onPause()
        Wearable.getDataClient(this).removeListener(this)
    }

    fun logThis(newInfo: String?) {
        if (newInfo!!.compareTo("") != 0) {
            logger!!.append(
                """
                    
                    $newInfo
                    """.trimIndent()
            )
        }
    }

    private fun sendData(message: String) {
        val dataMap = PutDataMapRequest.create(dataPath)
        dataMap.dataMap.putString("message", message)
        val request = dataMap.asPutDataRequest()
        request.setUrgent()
        val dataItemTask = Wearable.getDataClient(this).putDataItem(request)
        dataItemTask
            .addOnSuccessListener { dataItem ->
                Log.d(
                    TAG,
                    "Sending message was successful: $dataItem"
                )
            }
            .addOnFailureListener { e ->
                Log.e(
                    TAG,
                    "Sending message failed: $e"
                )
            }
    }

    override fun onDataChanged(dataEventBuffer: DataEventBuffer) {
        Log.d(TAG, "onDataChanged: $dataEventBuffer")
        for (event in dataEventBuffer) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event.dataItem.uri.path
                if (dataPath == path) {
                    val dataMapItem = DataMapItem.fromDataItem(event.dataItem)
                    val message = dataMapItem.dataMap.getString("message")
                    Log.v(TAG, "Wear activity received message: $message")
                    logThis(message)
                } else {
                    Log.e(TAG, "Unrecognized path: $path")
                }
            } else if (event.type == DataEvent.TYPE_DELETED) {
                Log.v(TAG, "Data deleted : " + event.dataItem.toString())
            } else {
                Log.e(TAG, "Unknown data event Type = " + event.type)
            }
        }
    }

    companion object {
        var num = 0
        const val dataPath = "/data_path"
        const val TAG = "Mobile MainActivity"
    }
}