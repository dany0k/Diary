package ru.vsu.cs.zmaev.diary

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.wearable.*
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import ru.vsu.cs.zmaev.diary.databinding.ActivityMainBinding

class MainActivity : WearableActivity(), OnDataChangedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mTextView: TextView
    private lateinit var sendButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mTextView = binding.text
        sendButton = binding.wearableSendButton

        sendButton.setOnClickListener {
            val message = "Hello from wearable!, $num"
            sendData(message, dataPath, this)
            num++
        }

        setAmbientEnabled()
    }

   public override fun onResume() {
        super.onResume()
        Wearable.getDataClient(this).addListener(this)
    }

    public override fun onPause() {
        super.onPause()
        Wearable.getDataClient(this).removeListener(this)
    }

    override fun onDataChanged(dataEventBuffer: DataEventBuffer) {
        Log.d(
            TAG,
            "onDataChanged: $dataEventBuffer"
        )
        for (event in dataEventBuffer) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event.dataItem.uri.path
                if (dataPath == path) {
                    val dataMapItem = DataMapItem.fromDataItem(event.dataItem)
                    val message = dataMapItem.dataMap.getString("message")
                    Log.v(
                        TAG,
                        "Wear activity received message: $message"
                    )
                    // Display message in UI
                    mTextView.text = message
                } else {
                    Log.e(
                        TAG,
                        "Unrecognized path: $path"
                    )
                }
            } else if (event.type == DataEvent.TYPE_DELETED) {
                Log.v(TAG, "Data deleted : " + event.dataItem.toString())
            } else {
                Log.e(TAG, "Unknown data event Type = " + event.type)
            }
        }
    }

    companion object {

        fun sendData(message: String, dataPath: String, context: Context) {
            val dataMap = PutDataMapRequest.create(dataPath)
            dataMap.dataMap.putString("message", message)
            val request = dataMap.asPutDataRequest()
            request.setUrgent()
            val dataItemTask = Wearable.getDataClient(context).putDataItem(request)
            dataItemTask
                .addOnSuccessListener { dataItem ->
                    Log.d(
                        ContentValues.TAG,
                        "Sending message was successful: $dataItem"
                    )
                }
                .addOnFailureListener { e ->
                    Log.e(
                        ContentValues.TAG,
                        "Sending message failed: $e"
                    )
                }
        }

        var num = 0
        private const val TAG = "Wear MainActivity"
        private const val dataPath = "/data_path"

    }
}