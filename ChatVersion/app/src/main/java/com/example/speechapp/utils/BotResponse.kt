package com.example.speechapp.utils

import android.content.Intent
import com.example.speechapp.utils.Constants.OPEN_GOOGLE
import com.example.speechapp.utils.Constants.OPEN_SEARCH
import java.lang.Exception
import android.provider.MediaStore

import com.example.speechapp.utils.Constants.OPEN_IMAGE
import com.example.speechapp.utils.Constants.OPEN_WORKER
import com.example.speechapp.utils.Constants.RECEIVE_IMAGE_ID
import com.example.speechapp.utils.Constants.RECEIVE_WORKER_IMAGE_ID

object BotResponse {
    fun basicResponse(_message:String):String{
        val random =(0..2).random()
        val message =_message.toLowerCase()

        return when{

            //Hello
            message.contains("hallo")->{
                when(random){
                    0 -> "Hallo"
                    1 -> "Wie gehts?"
                    2 -> "Hi"
                    else ->"error"
                }
            }


            message.contains("parkplatz")->{
                when(random){
                    0 -> "Parkplatz 123 ist Frei, erbefindet sich von Ihnen Links. Soll ich mir den Parkplatz merken?"
                    1 -> "Ich habe noch viele Parkplätze im breich F frei. Sie sind dazu sehr nah an unserem West eingang. Soll ich mir deinen Parkplatz merken?"
                    2 -> "Gerade sind im B Parkbereich plätze Frei geworden. Wenn Sie an der nächsten Rechts abbiegen Fahren Sie direkt darauf zu. Soll ich mir den Parkplatz merken?"
                    else ->"error"
                }
            }

            message.contains("merk")->{
                when(random){
                    0 -> "Ok ich habe ihn notiert. Möchtest du wissen wo Einkaufswägen sind?"
                    1 -> "Ok ich werde ihn mir merken. Soll ich dir nun zeigen wo du Einkaufswägen findest?"
                    2 -> "gerne ich werde mir den Parkplatz merken. Soll ich dir zeigen wo du Einkaufswägen findest?"
                    else ->"error"
                }
            }

            message.contains("einkaufswagen")->{
                when(random){
                    0 -> "Die nächsten Einkaufswagen befinden sich von hier aus Links am Nord Eingang. Brauchst du hilfe dich im Laden zurechtzufinden?"
                    1 -> "Sie finden Links neben dem Hauteingang eina Auswahl an Einkauswägen sowie Taschen und Körbe. Brauchst du hilfe dich im Laden zurechtzufinden?"
                    2 -> "Sie finden Links neben dem Hauteingang sowie am Westeingang eine Auswahl an Einkauswägen sowie Taschen und Körbe. Brauchen Sie hilfe sich im Laden zurechtzufinden?"
                    else ->"error"
                }
            }

            message.contains("einkaufsliste")->{
                when(random){
                    0 -> "Ok ich habe es auf deine Einkauflsite gesetzt. Kann ich dir sonst irgendwie Helfen?"
                    1 -> "Ok ich habe es auf deine Einkauflsite gesetzt. Kann ich dir sonst irgendwie Helfen?"
                    2 -> "Ok ich habe es auf deine Einkauflsite gesetzt. Kann ich dir sonst irgendwie Helfen?"
                    else ->"error"
                }
            }
            message.contains("termin")->{
                when(random){
                    0 -> "Ok ich habe mit Herold einen Termin gemacht. Er wird die nächste viertelstunde für dich Bereit sein."
                    1 -> "Ok ich habe mit Herold einen Termin gemacht. Er wird die nächste viertelstunde für dich Bereit sein."
                    2 -> "Ok ich habe mit Herold einen Termin gemacht. Er wird die nächste viertelstunde für dich Bereit sein."
                    else ->"error"
                }
            }
            message.contains("liste")->{
                when(random){
                    0 -> "Du hast Tisch auf deiner einkaufsliste. Du findest Tisch in Regalreihe 7, Platz 45"
                    1 -> "Du hast Tisch auf deiner einkaufsliste. Du findest Tisch in Regalreihe 7, Platz 45"
                    2 -> "Du hast Tisch auf deiner einkaufsliste. Du findest Tisch in Regalreihe 7, Platz 45"
                    else ->"error"
                }
            }
            message.contains("lager")->{
                when(random){
                    0 -> "Unser lager befindet sich im Erdgeschoss. Folge einfach der Beschilderung"
                    1 -> "Unser lager befindet sich im Erdgeschoss. Folge einfach der Beschilderung"
                    2 -> "Unser lager befindet sich im Erdgeschoss. Folge einfach der Beschilderung"
                    else ->"error"
                }
            }

            message.contains("kasse")->{
                when(random){
                    0 -> "Unsere Kassen befinden sich im Erdgeschoss, Folgen Sie den Pfeilen richtung lager und laufen sie am Ende Links."
                    1 -> "Laufen Sie links die Treppe runter und folgen Sie dort den Pfeilen richtung Lager. Am ende biegen Sie dann links ab."
                    2 -> "Unsere Kassen befinden sich im Erdgeschoss, Folgen Sie den Pfeilen richtung lager und laufen sie am Ende Links."
                    else ->"error"
                }
            }
            //How are you?
            message.contains("wie geht es dir")->{
                when(random){
                    0 -> "Ich bin etwas Hungrig"
                    1 -> "Mir geht es gut."
                    2 -> "Ganz gut"
                    else ->"error"
                }

            }

            message.contains("geparkt")->{
                when(random){
                    0 -> "Dein Auto steht im Blog E auf der nummer 45"
                    1 -> "Dein Auto steht im Blog E auf der nummer 45"
                    2 -> "Dein Auto steht im Blog E auf der nummer 45"
                    else ->"error"
                }

            }

            message.contains("danke")->{
                when(random){
                    0 -> "Bitte, ich hoffe dich bald wieder in unserem laden Führen zu können"
                    1 -> "Bitte, ich hoffe dich bald wieder in unserem laden Führen zu können"
                    2 -> "Bitte, ich hoffe dich bald wieder in unserem laden Führen zu können"
                    else ->"error"
                }

            }


            message.contains("flip")&& message.contains("coin")->{
                var r = (0..1).random()
                val result= if(r==0) "Kopf" else "Zahl"
                "Ich habe eine Münze geworfen und es ist $result"
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

            message.contains("tisch") ->{
                OPEN_IMAGE
            }

            message.contains("mitarbeiter") ->{
               OPEN_WORKER
            }

            else -> {
                when (random) {
                    0 -> "Das habe ich nicht Verstanden"
                    1 -> "Das Weiß ich leider nicht"
                    2 -> "Ich habe es leider nicht Verstanden"
                    else -> "error"
                }
            }
        }
    }
}