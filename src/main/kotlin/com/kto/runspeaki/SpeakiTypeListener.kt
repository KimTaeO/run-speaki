package com.kto.runspeaki

import com.intellij.util.messages.Topic

interface SpeakiTypeListener {
    fun onSpeakiTypeChanged()

    companion object {
        @Topic.AppLevel
        val TOPIC = Topic.create("Speaki Type Changed", SpeakiTypeListener::class.java)
    }
}