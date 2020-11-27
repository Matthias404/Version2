package com.example.speechapp.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.speechapp.R
import com.example.speechapp.data.Message
import com.example.speechapp.utils.BotResponse
import com.example.speechapp.utils.Constants.OPEN_GOOGLE
import com.example.speechapp.utils.Constants.OPEN_SEARCH
import com.example.speechapp.utils.Time
import kotlinx.coroutines.*

import com.example.speechapp.utils.Constants.RECEIVE_ID
import com.example.speechapp.utils.Constants.SEND_ID
import kotlinx.android.synthetic.main.activity_main.*
import opennlp.tools.doccat.DoccatModel
import opennlp.tools.tokenize.TokenizerModel
import java.io.FileInputStream


class MainActivity : AppCompatActivity() {

    private lateinit var adapter : MessagingAdapter
    private val botList = listOf("Peter", "Jürgen", "Ralf", "Jessi")

    //val tokenModel = TokenizerModel(FileInputStream(""))
    //var categorizerModel = DoccatModel(FileInputStream(""));


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setModels()
        recyclerView()

        clickEvents()

        val random =(0..3).random()
        costomMessage("Hallo ich bin ${botList[random]}, wie kann ich helfen?")
    }

    private fun setModels(){}


    private fun clickEvents(){
        btn_send.setOnClickListener{
            sendMessage()
        }

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
            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            rv_messages.scrollToPosition(adapter.itemCount-1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String){
        val timeStamp = Time.timeStamp()

        GlobalScope.launch { delay(1000)
            withContext(Dispatchers.Main){
                val response =BotResponse.basicResponse(message)
                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))
                rv_messages.scrollToPosition(adapter.itemCount-1)

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
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))
                rv_messages.scrollToPosition(adapter.itemCount-1)
            }
        }
    }
}