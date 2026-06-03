package com.uvitos.fastoutfit.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvitos.fastoutfit.R
import com.uvitos.fastoutfit.ui.components.AppBackground
import com.uvitos.fastoutfit.ui.components.GarmentPlaceholderCard
import com.uvitos.fastoutfit.ui.theme.*


@Composable
fun AddScreen(
    onHelpClick:      () -> Unit = {},
    onHomeClick:      () -> Unit = {},

    onOkayClick:      () -> Unit = {},
    onDeleteClick:    () -> Unit = {},
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
@Composable
private fun AdditionCard(

    onOkayClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        // Placeholder card (se reemplazará con imagen real)
        GarmentPlaceholderCard(
            cardSize = 150.dp,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick  = onOkayClick,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    painter        = painterResource(R.drawable.ic_ok),
                    contentDescription = "Favorite",
                    tint               = Negrito,
                    modifier           = Modifier.size(18.dp),
                )
            }

            IconButton(
                    onClick  = onCancelClick,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_cancel),
                    contentDescription = "Delete",
                    tint = Negrito,
                    modifier = Modifier.size(18.dp),
                )
            }
        }


    }
}


@Preview(
    name           = "Wardrobe Screen – Dark",
    showBackground = true,
    showSystemUi   = true,
    uiMode         = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun AddScreenPreview() {
    FastOutfitTheme(
        darkTheme    = true,
        dynamicColor = false,
    ) {
        AddScreen()
    }
}