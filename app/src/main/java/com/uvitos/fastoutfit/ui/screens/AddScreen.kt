package com.uvitos.fastoutfit.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uvitos.fastoutfit.R
import com.uvitos.fastoutfit.navigation.Categories
import com.uvitos.fastoutfit.ui.components.AppBackground
import com.uvitos.fastoutfit.ui.components.ImagePicker
import com.uvitos.fastoutfit.ui.theme.*
import com.uvitos.fastoutfit.ui.viewmodel.ClothingViewModel


@Composable
fun AddScreen(
    onHelpClick:      () -> Unit = {},
    onHomeClick:      () -> Unit = {},

    onOkayClick:      () -> Unit = {},
    onDeleteClick:    () -> Unit = {},
    viewModel: ClothingViewModel
) {
    AppBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        ) {
            AddTopBar(
                onHelpClick = onHelpClick,
                onHomeClick = onHomeClick
            )
            AdditionCard(
                onOkayClick = onOkayClick,
                onCancelClick = onDeleteClick,
                viewModel = viewModel
            )
        }

    }
}

// Top bar

@Composable
private fun AddTopBar(
    onHelpClick:    () -> Unit,
    onHomeClick:    () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        IconButton(
            onClick  = onHelpClick,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                painter            = painterResource(R.drawable.ic_help),
                contentDescription = "Help",
                tint               = TextPrimary,
                modifier           = Modifier.size(28.dp),
            )
        }

        IconButton(
            onClick  = onHomeClick,
            modifier = Modifier.align(Alignment.Center),
        ) {
            Icon(
                imageVector        = Icons.Filled.Home,
                contentDescription = "Home",
                tint               = GoldAccent,
                modifier           = Modifier.size(34.dp),
            )
        }


    }
}

//  Garment card + campos de texto
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdditionCard(
    onOkayClick: () -> Unit,
    onCancelClick: () -> Unit,
    viewModel: ClothingViewModel
) {

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var name by remember { mutableStateOf("") }

    val options = Categories.all
    var selected by remember { mutableStateOf(options[0]) }
    var expanded by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        // Placeholder card (se reemplazará con imagen real)
        ImagePicker(
                imageUri = imageUri,
                onImageSelected = {uri -> imageUri = uri}
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría") },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selected = option
                            expanded = false
                        }
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onCancelClick,
                modifier = Modifier.size(64.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_cancel),
                    contentDescription = "Delete",
                    tint = Blanco,
                    modifier = Modifier.size(26.dp),
                )
            }

            Spacer(modifier = Modifier.width(100.dp))

            IconButton(
                onClick = {
                    val currentUri = imageUri
                    if (name.isBlank()) return@IconButton
                    if (currentUri == null) return@IconButton

                    viewModel.saveClothingItem(
                        context = context,
                        imageUri = currentUri,
                        name = name,
                        category = selected
                    )
                    onOkayClick()
                },
                modifier = Modifier.size(64.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_ok),
                    contentDescription = "Favorite",
                    tint = Blanco,
                    modifier = Modifier.size(26.dp),
                )
            }
        }
    }
}



