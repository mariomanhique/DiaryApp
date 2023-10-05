package com.example.diaryapp.presentation.screens.write

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.diaryapp.R
import com.mariomanhique.util.model.Diary
import com.mariomanhique.util.model.Mood
import com.mariomanhique.ui.theme.DiaryAppTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.mariomanhique.ui.GalleryImage
import com.mariomanhique.ui.GalleryState
import com.mariomanhique.util.GalleryUploader
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WriteContent(
    modifier: Modifier = Modifier,
    galleryState: GalleryState,
    uiState: UiState,
    title: String,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDiarySaved: (Diary) -> Unit,
    description: String,
    paddingValues: PaddingValues,
    pagerState: PagerState,
    onImageSelect: (Uri) -> Unit,
    onImageClicked: (GalleryImage) -> Unit

){

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = scrollState.maxValue){
        scrollState.scrollTo(scrollState.maxValue)
    }
    Column(
        modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(bottom = 24.dp)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(state = scrollState)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalPager(
                state = pagerState,
                count = Mood.values().size
            ) {page->
                
                AsyncImage(
                    modifier = Modifier.size(120.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(Mood.values()[page].icon)
                        .crossfade(true).build(),
                        contentDescription =null )
            }

            Spacer(modifier = Modifier.height(20.dp))

            InputDiaryFields(
                value = title,
                focusDirection = FocusDirection.Down,
                focusManager = focusManager,
                placeHolder = stringResource(id = R.string.title),
                isSingleLine = true,
                maxLines = 1,
                onTextChanged = onTitleChanged
            )

            InputDiaryFields(
                value = description,
                focusManager = focusManager,
                scrollState =scrollState,
                placeHolder = stringResource(id = R.string.description),
                isSingleLine = false,
                maxLines = Int.MAX_VALUE,
                onTextChanged = onDescriptionChanged
            )
        }

        Column(
            verticalArrangement = Arrangement.Bottom
        ) {
            WriteScreenBottomContent(
                uiState = uiState,
                galleryState = galleryState,
                onDiarySaved = onDiarySaved,
                onImageSelect = onImageSelect,
                onImageClicked = onImageClicked,
                onAddClick = {focusManager.clearFocus()}
            )
        }


    }
}

@Composable
fun WriteScreenBottomContent(
    modifier: Modifier = Modifier,
    galleryState: GalleryState,
    uiState: UiState,
    onAddClick: () -> Unit,
    onImageSelect: (Uri) -> Unit,
    onImageClicked: (GalleryImage) -> Unit,
    onDiarySaved: (Diary) -> Unit
){

    val context = LocalContext.current

    Column{
        Spacer(modifier = Modifier.padding(12.dp))
        GalleryUploader(
            galleryState = galleryState,
            onAddClicked = onAddClick,
            onImageSelect = onImageSelect,
            onImageClicked = onImageClicked
        )
        Spacer(modifier = Modifier.padding(12.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = Shapes().small,
            onClick = {
                if(uiState.title.isNotEmpty() && uiState.description.isNotEmpty()){
                    onDiarySaved(
                        Diary().apply {
                            if (uiState.selectedDiaryId != null){
                                this.id = uiState.selectedDiaryId.toString()
                            }
                            this.title = uiState.title
                            this.description = uiState.description
                            this.imagesList = galleryState.images.map {
                                it.remoteImagePath
                            }
                        }
                    )
                }else{
                    Toast.makeText(context,"Fields can't be blank",Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(text = "Save diary")
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun WriteScreenBottomContentPreview(){
    DiaryAppTheme {
//        WriteScreenBottomContent(onDiarySaved = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDiaryFields(
    modifier: Modifier = Modifier,
    value: String,
    focusDirection: FocusDirection?=null,
    focusManager: FocusManager,
    scrollState: ScrollState?=null,
    maxLines: Int,
    placeHolder:String,
    isSingleLine: Boolean,
    onTextChanged: (String) -> Unit,
){

    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onTextChanged,
            placeholder = { Text(text = placeHolder)},
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Unspecified,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Unspecified,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38F)
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    if(focusDirection != null){
                        focusManager.moveFocus(focusDirection)
                    }else{
                        scope.launch {
                            scrollState?.animateScrollTo(Int.MAX_VALUE)
                        }
                        focusManager.clearFocus()
                    }
                }
            ),
            maxLines = maxLines,
            singleLine = isSingleLine
        )

    }

}

//@Preview(showBackground = true)
//@Composable
//fun InputDiaryFieldsPreview(){
//    DiaryAppTheme {
//        Column {
//            InputDiaryFields(
//                value = "",
//                onTextChanged = {},
//                maxLines = 1,
//                placeHolder = stringResource(id = R.string.title),
//                isSingleLine = false,
//            )
//
//            InputDiaryFields(
//                value = "",
//                onTextChanged = {},
//                maxLines = Int.MAX_VALUE,
//                placeHolder = stringResource(id = R.string.description),
//                isSingleLine = false,
//            )
//        }
//
//    }
//}