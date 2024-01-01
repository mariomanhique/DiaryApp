import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.diaryapp.model.Mood
import com.example.diaryapp.presentation.screens.write.WriteScreen
import com.example.diaryapp.presentation.screens.write.WriteViewModel
import com.example.diaryapp.util.Constants
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

const val write_navigation_route = "write_navigation_route"

fun NavController.navigateToWrite(navOptions: NavOptions?= null){
    this.navigate(write_navigation_route, navOptions)
}

sealed class Screen(val route: String){
    data object Write:Screen(route = "write_navigation_route?${Constants.WRITE_SCREEN_ARG_KEY}=" +
            "{${Constants.WRITE_SCREEN_ARG_KEY}}") {//The argument that we will define is optional.
    fun passDiaryId(diaryId:String)=
        "write_navigation_route?${Constants.WRITE_SCREEN_ARG_KEY}=$diaryId"


    }
}

@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.writeRoute(
    onBackPressed: () -> Unit,
    paddingValues: PaddingValues
){
    composable(
        route= Screen.Write.route,
        arguments = listOf(navArgument(name = Constants.WRITE_SCREEN_ARG_KEY){
            type = NavType.StringType
            nullable = true
        })
    ){

        val writeViewModel: WriteViewModel = hiltViewModel()
        val uiState = writeViewModel.uiState
        val context = LocalContext.current
        val pagerState = rememberPagerState()
        val galleryState = writeViewModel.galleryState
        val pageNumber by remember {
            derivedStateOf{
                pagerState.currentPage
            }
        }

        LaunchedEffect(
            key1 = uiState,
            block = {
                Log.d("Selected Diary", "writeRoute: ${uiState.selectedDiaryId} ")
            })

        WriteScreen(
            uiState = uiState,
            galleryState = galleryState,
            onBackPressed = onBackPressed,
            pagerState = pagerState,
            onTitleChanged = {title->
                writeViewModel.setTitle(title)
            },
            onDescriptionChanged = {description->
                writeViewModel.setDescription(description)
            },
            onDiarySaved = {diary->
                writeViewModel.upsertDiary(
                    diary = diary.apply {
                        mood = Mood.values()[pageNumber].name
                    },
                    onSuccess = onBackPressed,
                    onError = {
                        onBackPressed()
                    }
                )
            },
            moodName = { Mood.values()[pageNumber].name},
            onDateTimeUpdated = {
                writeViewModel.updateDateTime(zonedDateTime = it)
            },
            onDeleteConfirmed = {
                onBackPressed()
                writeViewModel.deleteDiary(
                    onSuccess = {
                        Toast.makeText(context,"Deleted", Toast.LENGTH_SHORT).show()
                    },
                    onError = {
                        Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onImageSelect = {imageUri->
                val type = context.contentResolver.getType(imageUri)?.split("/")?.last() ?: "jpg"
                Log.d("addImagetype", "$imageUri")
                writeViewModel.addImage(
                    image = imageUri,
                    imageType = type
                )
            },
            onImageDeleteClicked = {
                galleryState.removeImage(it)
            },
        )
    }
}