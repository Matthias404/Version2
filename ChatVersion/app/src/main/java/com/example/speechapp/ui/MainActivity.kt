package com.example.speechapp.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech

import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.speechapp.R
import com.example.speechapp.data.Message
import com.example.speechapp.trainigsData.DocClassificationNaiveBayesTrainer
import com.example.speechapp.utils.BotResponse
import com.example.speechapp.utils.Constants
import com.example.speechapp.utils.Constants.OPEN_IMAGE
import com.example.speechapp.utils.Constants.OPEN_GOOGLE
import com.example.speechapp.utils.Constants.OPEN_SEARCH
import com.example.speechapp.utils.Constants.OPEN_WORKER
import com.example.speechapp.utils.Constants.RECEIVE_ID
import com.example.speechapp.utils.Constants.RECEIVE_IMAGE_ID
import com.example.speechapp.utils.Constants.RECEIVE_WORKER_IMAGE_ID
import com.example.speechapp.utils.Constants.SEND_ID
import com.example.speechapp.utils.Time
import kotlinx.coroutines.*


import kotlinx.android.synthetic.main.activity_main.*


import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter : MessagingAdapter
    private val botList = listOf("Jessy", "Rebecca", "Monika", "Jessi")
    public val img=""




    companion object {
        private const val REQUEST_CODE_STT = 1
    }


    private val textToSpeechEngine: TextToSpeech by lazy {
        TextToSpeech(this,
                TextToSpeech.OnInitListener { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        textToSpeechEngine.language = Locale.GERMAN
                    }
                })
    }

    val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DocClassificationNaiveBayesTrainer.main()
        recyclerView()

        clickEvents()

        val random =(0..3).random()
        costomMessage("Hallo ich bin ${botList[random]}, ich habe bemerkt das du auf unserem Gelände bist kann ich dir helfen einen Parklatz zu finden?")
    }

    private fun clickEvents(){
        btn_send.setOnClickListener{
            sendMessage()
        }
        sttIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )


        et_message.setOnClickListener{
            GlobalScope.launch {
                delay(100)
                withContext(Dispatchers.Main){
                    rv_messages.scrollToPosition(adapter.itemCount-1)
                }
            }
        }
    }

    private fun  recyclerView(){
        adapter = MessagingAdapter()
        rv_messages.adapter =adapter
        rv_messages.layoutManager =LinearLayoutManager(applicationContext)
    }

    private fun sendMessage(){
        val message = et_message.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()){
            et_message.setText("")
            adapter.insertMessage(Message(message, SEND_ID, timeStamp, img.toUri()))
            rv_messages.scrollToPosition(adapter.itemCount-1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String){
        val timeStamp = Time.timeStamp()

        GlobalScope.launch { delay(1000)
            withContext(Dispatchers.Main){
                val response =BotResponse.basicResponse(message)
                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp, img.toUri()))
                rv_messages.scrollToPosition(adapter.itemCount-1)
                var delayTime = 3000

                var st = StringTokenizer(response)
                if (st.countTokens()>5){
                    delayTime= delayTime+st.countTokens()*250
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeechEngine.speak(response, TextToSpeech.QUEUE_FLUSH, null, "tts1")
                } else {
                    textToSpeechEngine.speak(response, TextToSpeech.QUEUE_FLUSH, null)
                }
                when(response){
                    OPEN_GOOGLE ->{
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH->{
                        val site = Intent(Intent.ACTION_VIEW)
                        val serchTerm: String? =message.substringAfter("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$serchTerm")
                        startActivity(site)
                    }
                    OPEN_IMAGE ->{
                       try {
                            adapter.insertMessage(Message(response, RECEIVE_IMAGE_ID, timeStamp, img.toUri()))
                            rv_messages.scrollToPosition(adapter.itemCount-1)
                        } catch (e: Exception) {
                            // Camera is not available (in use or does not exist)
                            null // returns null if camera is unavailable
                        }

                    }
                    OPEN_WORKER ->{
                        try {
                            adapter.insertMessage(Message(response, RECEIVE_WORKER_IMAGE_ID, timeStamp, img.toUri()))
                            rv_messages.scrollToPosition(adapter.itemCount-1)
                        } catch (e: Exception) {
                            // Camera is not available (in use or does not exist)
                            null // returns null if camera is unavailable
                        }

                    }

                }
                delay(delayTime.toLong())
                sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Rede jetzt")

                try {
                    startActivityForResult(sttIntent, REQUEST_CODE_STT)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()

                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val timeStamp = Time.timeStamp()

        when (requestCode) {
            REQUEST_CODE_STT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    result?.let {
                        val recognizedText = it[0]

                        if (recognizedText.isNotEmpty()){
                            et_message.setText("")
                            adapter.insertMessage(Message(recognizedText, SEND_ID, timeStamp, img.toUri()))
                            rv_messages.scrollToPosition(adapter.itemCount-1)

                            botResponse(recognizedText)
                        }
                    }
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                rv_messages.scrollToPosition(adapter.itemCount-1)
            }
        }
    }


    private fun  costomMessage(message:String){
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                val timeStamp = Time.timeStamp()
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp,img.toUri()))
                rv_messages.scrollToPosition(adapter.itemCount-1)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeechEngine.speak(message, TextToSpeech.QUEUE_FLUSH, null, "tts1")
                } else {
                    textToSpeechEngine.speak(message, TextToSpeech.QUEUE_FLUSH, null)
                }
            }
            delay(5900)
            sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Rede jetzt")

            try {
                startActivityForResult(sttIntent, REQUEST_CODE_STT)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()

            }
        }
        textToSpeechEngine.speak(message, TextToSpeech.QUEUE_FLUSH, null)
    }
}