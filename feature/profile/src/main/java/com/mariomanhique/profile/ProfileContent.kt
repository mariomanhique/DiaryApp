package com.mariomanhique.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mariomanhique.ui.theme.DiaryAppTheme

@Composable
fun ProfileContent(
    imageProfile: String,
    username: String,
    onValueChanged: (String) -> Unit,
    onSelectImage: (Uri) -> Unit,
    onProfileSaved: () -> Unit,
    onDeleteClicked: (Boolean) -> Unit,
    onLogoutClicked: (Boolean) -> Unit
){
    ProfileCardInfo(
        imageProfile = imageProfile,
        username = username,
        onSelectImage = onSelectImage,
        onProfileSaved = onProfileSaved,
        onValueChanged = onValueChanged,
        onDeleteClicked = onDeleteClicked,
        onLogoutClicked = onLogoutClicked

    )
}

@Composable
fun ProfileCardInfo(
    modifier: Modifier = Modifier,
    imageProfile: String?,
    username: String,
    onValueChanged: (String) -> Unit,
    onSelectImage: (Uri) -> Unit,
    onDeleteClicked: (Boolean) -> Unit,
    onLogoutClicked: (Boolean) -> Unit,
    onProfileSaved: () -> Unit,
){

    var user by remember {
        mutableStateOf(username)
    }

    val scrollState = rememberScrollState()
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        UserDetailsCard(
          imageProfile = imageProfile,
          username = username,
            onSelectImage = onSelectImage,
        )

        Spacer(modifier = Modifier.size(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputField(
                modifier = Modifier.weight(3F),
                value = username,
                onTextChanged = {
                    onValueChanged(it)
                },
                placeHolder = R.string.placeholder)


            Row(
                modifier
                    .size(50.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
//                    modifier = Modifier.size(100.dp),
                    onClick = {
//                    onProfileSaved()
                }) {
                    Icon(
                        modifier = Modifier
                            .size(30.dp),
                        imageVector = Icons.Default.Check,
                        contentDescription ="",
                        tint = Color.Green
                    )
                }

            }


        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(
            Modifier
                .height(2.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ClickableText(
                title = R.string.deleteAll,
                imageVector =Icons.Rounded.Delete,
                onTextClicked = onDeleteClicked

            )
            ClickableText(
                title = R.string.logout,
                imageVector =Icons.Rounded.ExitToApp,
                onTextClicked = onLogoutClicked
            )
        }

    }
}

@Composable
fun ClickableText(
    @StringRes title: Int,
    imageVector: ImageVector,
    onTextClicked: (Boolean) -> Unit
){
    Row(
        modifier = Modifier.clickable {
            onTextClicked(true)
        }
    ) {
        Text(
            text = stringResource(id = title),
            color = Color.Red
        )
        Icon(
            imageVector = imageVector,
            contentDescription = "")
    }

}

@Composable
fun UserDetailsCard(
    imageProfile: String?,
    username: String,
    onSelectImage: (Uri) -> Unit,
    ){
    Box(modifier = Modifier,
        contentAlignment = Alignment.BottomEnd
    ){

        var imageUri by remember {
            mutableStateOf(imageProfile)
        }

        val multiplePhotoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
        ) { image ->
            if (image != null) {
                onSelectImage(image)
            }
        }

        AsyncImage(
           modifier = Modifier
               .clip(CircleShape)
               .size(100.dp)
               .border(
                   width = 1.dp,
                   brush = Brush.linearGradient(colors = listOf(Color.Red, Color.Cyan)),
                   shape = CircleShape
               ),
           model = ImageRequest.Builder(LocalContext.current)
               .data(imageUri)
               .crossfade(true)
               .build(),
           contentDescription = "",
           contentScale = ContentScale.Crop
       )

        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .padding(1.dp)
                .clickable {
                    multiplePhotoPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
        ){
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(colors = listOf(Color.Red, Color.Cyan)),
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .padding(5.dp),
            ){

                Icon(
                    modifier = Modifier
                        //                        .size(20.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center),
                    imageVector = Icons.Default.Edit,
                    contentDescription = ""
                )

            }
        }

    }


    Text(text = username,
        modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 24.dp)
    )

}


@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    onTextChanged: (String) -> Unit,
    @StringRes placeHolder: Int
){

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange ={
            onTextChanged(it)
        },
        singleLine = true,
        placeholder = {
            Text(text = stringResource(id = placeHolder))
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Unspecified,
            unfocusedIndicatorColor = Color.LightGray,
            disabledIndicatorColor = Color.Unspecified,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38F)
        ),
        shape = CircleShape.copy(all = CornerSize(10.dp)),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ProfileCardInfoPreview(
){
    DiaryAppTheme {
        ProfileCardInfo(
            imageProfile = "",
            username = "",
            onValueChanged = {},
            onSelectImage = {

            },
            onProfileSaved = {},
            onDeleteClicked = {},
            onLogoutClicked = {}
            )
    }
}



