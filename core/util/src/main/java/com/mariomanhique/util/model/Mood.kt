package com.mariomanhique.util.model

import androidx.compose.ui.graphics.Color
import com.mariomanhique.util.icons.DiaryIcons
import com.mariomanhique.ui.theme.AngryColor
import com.mariomanhique.ui.theme.AwfulColor
import com.mariomanhique.ui.theme.BoredColor
import com.mariomanhique.ui.theme.CalmColor
import com.mariomanhique.ui.theme.DepressedColor
import com.mariomanhique.ui.theme.DisappointedColor
import com.mariomanhique.ui.theme.HappyColor
import com.mariomanhique.ui.theme.HumorousColor
import com.mariomanhique.ui.theme.LonelyColor
import com.mariomanhique.ui.theme.MysteriousColor
import com.mariomanhique.ui.theme.NeutralColor
import com.mariomanhique.ui.theme.RomanticColor
import com.mariomanhique.ui.theme.ShamefulColor
import com.mariomanhique.ui.theme.SurprisedColor
import com.mariomanhique.ui.theme.SuspiciousColor
import com.mariomanhique.ui.theme.TenseColor

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