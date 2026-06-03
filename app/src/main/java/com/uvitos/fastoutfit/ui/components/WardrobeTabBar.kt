package com.uvitos.fastoutfit.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvitos.fastoutfit.ui.screens.ClothingCategory
import com.uvitos.fastoutfit.ui.theme.*

@Composable
fun WardrobeTabBar(
    selected: ClothingCategory,
    onSelect: (ClothingCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ClothingCategory.entries.forEach { category ->
            val isActive = category == selected
            DrawerTab(
                label    = category.label,
                isActive = isActive,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onSelect(category) },
            )
        }
    }
}

//  Cajón individual

@Composable
private fun DrawerTab(
    label:    String,
    isActive: Boolean,
    modifier: Modifier = Modifier,
) {
    // Colores según estado
    val outerBorder  = if (isActive) GoldAccent          else Color(0xFF3A4560)
    val outerFill    = if (isActive) Color(0xFF2A3347)    else Color(0xFF232B3A)
    val innerFill    = if (isActive) Color(0xFF252D3E)    else Color(0xFF1E2535)
    val innerBorder  = if (isActive) GoldAccent.copy(alpha = 0.6f) else Color(0xFF2E3A50)
    val handleStroke = if (isActive) GoldAccent           else Color(0xFF4A5568)
    val handleFill   = if (isActive) Color(0xFF1E2535)    else Color(0xFF1A2030)
    val knobColor    = if (isActive) GoldAccent.copy(alpha = 0.5f) else Color(0xFF4A5568).copy(alpha = 0.4f)
    val labelColor   = if (isActive) GoldAccent           else TextSecondary
    val indicatorColor = GoldAccent

    Box(modifier = modifier.padding(horizontal = 3.dp, vertical = 6.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val outerRadius = 10f
            val innerPadding = 8f
            val innerRadius  = 6f

            // Marco exterior
            drawRoundRect(
                color       = outerFill,
                size        = Size(w, h),
                cornerRadius = CornerRadius(outerRadius),
            )
            drawRoundRect(
                color        = outerBorder,
                size         = Size(w, h),
                cornerRadius = CornerRadius(outerRadius),
                style        = Stroke(width = if (isActive) 1.5f else 1f),
            )

            // Indicador activo (barra dorada arriba)
            if (isActive) {
                drawRoundRect(
                    color        = indicatorColor,
                    topLeft      = Offset(w * 0.25f, 0f),
                    size         = Size(w * 0.5f, 4f),
                    cornerRadius = CornerRadius(2f),
                )
            }

            // Panel interior
            drawRoundRect(
                color        = innerFill,
                topLeft      = Offset(innerPadding, innerPadding + 4f),
                size         = Size(w - innerPadding * 2, h - innerPadding * 2 - 4f),
                cornerRadius = CornerRadius(innerRadius),
            )
            drawRoundRect(
                color        = innerBorder,
                topLeft      = Offset(innerPadding, innerPadding + 4f),
                size         = Size(w - innerPadding * 2, h - innerPadding * 2 - 4f),
                cornerRadius = CornerRadius(innerRadius),
                style        = Stroke(width = 0.8f),
            )

            //  Tirador (manija)
            val handleW      = w * 0.45f
            val handleH      = 12f
            val handleLeft   = (w - handleW) / 2f
            val handleTop    = h * 0.62f

            drawRoundRect(
                color        = handleFill,
                topLeft      = Offset(handleLeft, handleTop),
                size         = Size(handleW, handleH),
                cornerRadius = CornerRadius(handleH / 2f),
            )
            drawRoundRect(
                color        = handleStroke,
                topLeft      = Offset(handleLeft, handleTop),
                size         = Size(handleW, handleH),
                cornerRadius = CornerRadius(handleH / 2f),
                style        = Stroke(width = if (isActive) 1.5f else 1f),
            )

            //  circulo central de la manija
            drawCircle(
                color  = knobColor,
                radius = 4f,
                center = Offset(w / 2f, handleTop + handleH / 2f),
            )

            // Etiqueta ─
            drawContext.canvas.nativeCanvas.drawText(
                label,
                w / 2f,
                h * 0.42f,
                android.graphics.Paint().apply {
                    color       = labelColor.toArgb()
                    textSize    = 28f
                    textAlign   = android.graphics.Paint.Align.CENTER
                    isFakeBoldText = isActive
                    letterSpacing  = 0.12f
                    isAntiAlias    = true
                },
            )
        }
    }
}