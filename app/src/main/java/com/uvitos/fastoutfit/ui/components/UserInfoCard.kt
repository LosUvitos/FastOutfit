package com.uvitos.fastoutfit.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvitos.fastoutfit.ui.theme.GoldAccent
import com.uvitos.fastoutfit.ui.theme.InputBackground
import com.uvitos.fastoutfit.ui.theme.InputText
import com.uvitos.fastoutfit.ui.theme.TextPrimary
import com.uvitos.fastoutfit.ui.theme.TextSecondary

@Composable
fun UserInfoCard(
    title: String,
    value: String,
    actionIcon: ImageVector,
    onActionClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 2.dp,
            color = GoldAccent
        ),
        colors = CardDefaults.cardColors(
            containerColor = InputBackground
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = value,
                    color = InputText,
                    fontSize = 16.sp
                )
            }

            IconButton(
                onClick = onActionClick
            ) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = null,
                    tint = TextPrimary
                )
            }
        }
    }
}

