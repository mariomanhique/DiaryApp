package com.example.diaryapp.model

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.UUID

data class Diary(
    var id: String = UUID.randomUUID().toString(),
    var ownerId: String ="",
    var mood: String = Mood.Neutral.name,
    var title: String ="",
    var description: String ="",
    var imagesList: List<String> = listOf(),
    var date: Date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))




