package com.mariomanhique.util

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mariomanhique.ui.theme.Elevation
import com.mariomanhique.util.model.Diary
import com.mariomanhique.util.model.Mood
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DateHeader(
    localDate: LocalDate
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = String.format("%02d", localDate.dayOfMonth),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = fontFamily(
                    fontName = "Chakra Petch",
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Normal
                )
            )

            Text(
                text = localDate.dayOfWeek.toString().take(3),
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                fontFamily = fontFamily(
                    fontName = "Chakra Petch",
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Normal
                )
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(horizontalAlignment = Alignment.Start) {

            Text(
                text = localDate.month.toString()
                        .lowercase()
                        .replaceFirstChar {
                            it.titlecase()
                        },
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = fontFamily(
                    fontName = "Chakra Petch",
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Normal
                )
            )

            Text(
                text = localDate.year.toString(),
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                fontFamily = fontFamily(
                    fontName = "Chakra Petch",
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Normal
                )
            )

        }
    }
}

@Composable
fun DiaryHolder(
    modifier: Modifier = Modifier,
    diary: Diary,
    onClick: (String) -> Unit){

    var localDensity = LocalDensity.current
    var context = LocalContext.current
    var componentHeight by remember { mutableStateOf(0.dp) }
    var galleryOpened by rememberSaveable { mutableStateOf(false) }
    var galleryLoading by remember { mutableStateOf(false) }
    val downloadedImages = remember { mutableStateListOf<Uri>() }

    LaunchedEffect(key1 = galleryOpened) {
        if (galleryOpened && downloadedImages.isEmpty()) {
            galleryLoading = true
            fetchImagesFromFirebase(
                remoteImagePaths = diary.imagesList,
                onImageDownload = { image ->
                    downloadedImages.add(image)
                },
                onImageDownloadFailed = {
                    Toast.makeText(
                        context,
                        "Images not uploaded yet." +
                                "Wait a little bit, or try uploading again.",
                        Toast.LENGTH_SHORT
                    ).show()
                    galleryLoading = false
                    galleryOpened = false
                },
                onReadyToDisplay = {
                    galleryLoading = false
                    galleryOpened = true
                }
            )
        }
    }

    Row(modifier = modifier
        .clickable(
            indication = null,
            interactionSource = remember {
                MutableInteractionSource()
            }
        ) {
        onClick(diary.id)
    }){
        
        Spacer(modifier = Modifier.width(14.dp))
        Surface(
            modifier = Modifier
                .width(2.dp)
                .height(componentHeight + 14.dp),
            tonalElevation = Elevation.Level1) {}

        Spacer(modifier = Modifier.width(20.dp))

        Surface(modifier = Modifier
            .clip(
                shape = Shapes().medium.copy(all = CornerSize(8.dp))
            )
            .onGloballyPositioned {
                //This will be used to calculate the height of the line as well
                componentHeight = with(localDensity) { it.size.height.toDp() }
            },
        tonalElevation = Elevation.Level1
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                DiaryHeader(moodName =diary.mood ,time =diary.date.toInstant())
                Text(text = diary.description,
                    style = TextStyle(
                        textAlign = TextAlign.Justify,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontFamily = fontFamily(
                            fontName = "Chakra Petch",
                            fontWeight = FontWeight.Light,
                            fontStyle = FontStyle.Normal
                        )
                    ),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(14.dp)
                )

                if(diary.imagesList.isNotEmpty()){
                    ShowGalleryButton(
                        galleryOpened = galleryOpened,
                        galleryLoading = false) {
                        galleryOpened = !galleryOpened
                    }
                }

                AnimatedVisibility(
                    visible = galleryOpened && !galleryLoading,
                    enter = fadeIn() + expandVertically (
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Gallery(images =downloadedImages)
                    }
                }
            }
        }

    }
}

@Composable
fun ShowGalleryButton(
    galleryOpened: Boolean,
    galleryLoading: Boolean,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            text = if (galleryOpened)
                if (galleryLoading) "Loading" else "Hide Gallery"
            else "Show Gallery",
            style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)
        )
    }
}
@Composable
fun DiaryHeader(moodName: String, time: Instant){
    val mood by remember { mutableStateOf(Mood.valueOf(moodName)) }
    val formatter = remember {
        DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(mood.containerColor)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = mood.icon),
                contentDescription = "Mood Icon",
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = mood.name,
                color = mood.contentColor,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )
        }
        Text(
            text = formatter.format(time),
            color = mood.contentColor,
            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        )
    }
}