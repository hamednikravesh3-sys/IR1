package com.nv.navigation.core.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun NvMapPreview(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF476B79),
                        Color(0xFF375866),
                        Color(0xFF203A47),
                        Color(0xFF152C38)
                    )
                )
            )
    ) {
        val w = size.width
        val h = size.height
        for (i in 0..7) {
            val y = h * (0.08f + i * 0.07f)
            drawLine(Color(0x3326A69A), Offset(0f, y), Offset(w, y + 80f), strokeWidth = 40f, cap = StrokeCap.Round)
        }
        val roads = listOf(
            Pair(Offset(w*.08f,h*.25f), Offset(w*.91f,h*.78f)),
            Pair(Offset(w*.12f,h*.81f), Offset(w*.80f,h*.14f)),
            Pair(Offset(w*.03f,h*.57f), Offset(w*.96f,h*.49f)),
            Pair(Offset(w*.22f,h*.04f), Offset(w*.66f,h*.96f)),
            Pair(Offset(w*.02f,h*.38f), Offset(w*.90f,h*.31f))
        )
        for ((a,b) in roads) {
            drawLine(Color(0xFF607D84), a, b, strokeWidth = 16f, cap = StrokeCap.Round)
            drawLine(Color(0xFF9AAEB3), a, b, strokeWidth = 2.5f, cap = StrokeCap.Round)
        }
        for (i in 0..18) {
            val x = (i % 6) * (w/6f) + 20f
            val y = (i / 6) * (h/5f) + h*.20f
            drawRect(color = Color(0x2248E0C2), topLeft = Offset(x, y), size = androidx.compose.ui.geometry.Size(44f + (i%3)*8, 30f + (i%4)*7))
        }
        val p = Path().apply {
            moveTo(w*.48f, h*.93f)
            cubicTo(w*.50f,h*.80f, w*.49f,h*.73f, w*.46f,h*.66f)
            cubicTo(w*.42f,h*.58f, w*.41f,h*.48f, w*.35f,h*.41f)
            cubicTo(w*.31f,h*.35f, w*.28f,h*.30f, w*.24f,h*.25f)
        }
        drawPath(p, Color(0x4418C8FF), style = Stroke(38f, cap = StrokeCap.Round))
        drawPath(p, Color(0xFF0077FF), style = Stroke(22f, cap = StrokeCap.Round))
        drawPath(p, Color(0xFF28E0FF), style = Stroke(8f, cap = StrokeCap.Round))
        drawLine(Color(0xFFFFC247), Offset(w*.60f,h*.41f), Offset(w*.76f,h*.33f), strokeWidth = 9f, cap = StrokeCap.Round)
        drawLine(Color(0xFFFF5268), Offset(w*.68f,h*.68f), Offset(w*.84f,h*.74f), strokeWidth = 9f, cap = StrokeCap.Round)
        val cx = w*.48f
        val cy = h*.82f
        drawCircle(Color(0x3318C8FF), 70f, Offset(cx,cy))
        drawCircle(Color(0x5518C8FF), 50f, Offset(cx,cy))
        val arrow = Path().apply {
            moveTo(cx,cy-38f)
            lineTo(cx-25f,cy+28f)
            lineTo(cx,cy+15f)
            lineTo(cx+25f,cy+28f)
            close()
        }
        drawPath(arrow, Color(0xFF0B6DFF))
        drawPath(arrow, Color.White, style = Stroke(3f))
    }
}
