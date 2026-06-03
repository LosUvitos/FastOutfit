package com.uvitos.fastoutfit.ui.screens

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.uvitos.fastoutfit.ui.components.ImagePicker

@Composable
fun addScreenTest() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    ImagePicker(
        imageUri = imageUri,
        onImageSelected = {uri -> imageUri = uri}
        )
}