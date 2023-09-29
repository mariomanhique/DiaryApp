package com.example.diaryapp.model

import androidx.compose.ui.graphics.Color
import com.example.diaryapp.icons.DiaryIcons
import com.example.diaryapp.ui.theme.AngryColor
import com.example.diaryapp.ui.theme.AwfulColor
import com.example.diaryapp.ui.theme.BoredColor
import com.example.diaryapp.ui.theme.CalmColor
import com.example.diaryapp.ui.theme.DepressedColor
import com.example.diaryapp.ui.theme.DisappointedColor
import com.example.diaryapp.ui.theme.HappyColor
import com.example.diaryapp.ui.theme.HumorousColor
import com.example.diaryapp.ui.theme.LonelyColor
import com.example.diaryapp.ui.theme.MysteriousColor
import com.example.diaryapp.ui.theme.NeutralColor
import com.example.diaryapp.ui.theme.RomanticColor
import com.example.diaryapp.ui.theme.ShamefulColor
import com.example.diaryapp.ui.theme.SurprisedColor
import com.example.diaryapp.ui.theme.SuspiciousColor
import com.example.diaryapp.ui.theme.TenseColor

enum class Mood(
    val icon: Int,
    val contentColor: Color,
    val containerColor: Color

) {

    Neutral(
        icon = DiaryIcons.Neutral,
        contentColor = Color.Black,
        containerColor = NeutralColor
    ),
    Happy(
    icon = DiaryIcons.Happy,
    contentColor = Color.Black,
    containerColor = HappyColor
    ),
    Angry(
    icon = DiaryIcons.Angry,
    contentColor = Color.White,
    containerColor = AngryColor
    ),
    Bored(
    icon = DiaryIcons.Bored,
    contentColor = Color.Black,
    containerColor = BoredColor
    ),
    Calm(
    icon = DiaryIcons.Calm,
    contentColor = Color.Black,
    containerColor = CalmColor
    ),
    Depressed(
    icon = DiaryIcons.Depressed,
    contentColor = Color.Black,
    containerColor = DepressedColor
    ),
    Disappointed(
    icon = DiaryIcons.Disappointed,
    contentColor = Color.White,
    containerColor = DisappointedColor
    ),
    Humorous(
    icon = DiaryIcons.Humorous,
    contentColor = Color.Black,
    containerColor = HumorousColor
    ),
    Lonely(
    icon = DiaryIcons.Lonely,
    contentColor = Color.White,
    containerColor = LonelyColor
    ),
    Mysterious(
    icon = DiaryIcons.Mysterious,
    contentColor = Color.Black,
    containerColor = MysteriousColor
    ),
    Romantic(
    icon = DiaryIcons.Romantic,
    contentColor = Color.White,
    containerColor = RomanticColor
    ),
    Shameful(
    icon = DiaryIcons.Shameful,
    contentColor = Color.White,
    containerColor = ShamefulColor
    ),
    Awful(
    icon = DiaryIcons.Awful,
    contentColor = Color.Black,
    containerColor = AwfulColor
    ),
    Surprised(
    icon = DiaryIcons.Surprised,
    contentColor = Color.Black,
    containerColor = SurprisedColor
    ),
    Suspicious(
    icon = DiaryIcons.Suspicious,
    contentColor = Color.Black,
    containerColor = SuspiciousColor
    ),
    Tense(
    icon = DiaryIcons.Tense,
    contentColor = Color.Black,
    containerColor = TenseColor
    )
}