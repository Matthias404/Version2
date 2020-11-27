package com.example.speechapp.utils

import com.example.speechapp.utils.Constants.OPEN_GOOGLE
import com.example.speechapp.utils.Constants.OPEN_SEARCH
import java.lang.Exception

object BotResponse {
    fun basicResponse(_message:String):String{
        val random =(0..2).random()
        val message =_message.toLowerCase()


        return when{

            //Hello
            message.contains("hello")->{
                when(random){
                    0 -> "Hello there!"
                    1 -> "Sup!"
                    2 -> "Hi"
                    else ->"error"
                }
            }
            //How are you?
            message.contains("how are you?")->{
                when(random){
                    0 -> "Hello there!"
                    1 -> "Sup!"
                    2 -> "Hi"
                    else ->"error"
                }

            }

            message.contains("flip")&& message.contains("coin")->{
                var r = (0..1).random()
                val result= if(r==0) "Kopf" else "Zahl"
                "Ich ahbe eine Münze geworfen und es ist $result"
            }

            message.contains("solve") ->{
                val equation: String? = message.substringAfter("solve")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    answer.toString()
                }catch (e: Exception){
                    "Tut mir leid das weiß ich gerade nicht"
                }

             }

            //Gets the current time
            message.contains("time")&& message.contains("?") ->{
                Time.timeStamp()
            }


            message.contains("open")&& message.contains("google") ->{
               OPEN_GOOGLE
            }

            message.contains("search") ->{
                OPEN_SEARCH
            }

            else -> {
                when (random) {
                    0 -> "I don't unterstand..."
                    1 -> "Idk"
                    2 -> "Ich habe es nicht Verstanden"
                    else -> "error"
                }
            }
        }
    }
}