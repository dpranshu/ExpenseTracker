package com.example.expensetracker.ui.Screens

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.ViewModel.ExpenseViewModel
import com.example.expensetracker.data.RoomDatabase.CategorySummary
import kotlin.math.cos
import kotlin.math.sin

private val categoryColorPalette = listOf(
    Color(0xFF4CAF50), // Green
    Color(0xFF2196F3), // Blue
    Color(0xFFFF9800), // Orange
    Color(0xFF9C27B0), // Purple
    Color(0xFFF44336), // Red
    Color(0xFF00BCD4), // Cyan
    Color(0xFFFFE082), // Amber
    Color(0xFFE91E63), // Pink
    Color(0xFF795548), // Brown
    Color(0xFF607D8B)  // Blue Grey
)

fun getCategoryColor(category: String, index: Int): Color {
    return when (category.lowercase()) {
        "food" -> Color(0xFF4CAF50)
        "transport" -> Color(0xFF2196F3)
        "shopping" -> Color(0xFFFF9800)
        "entertainment" -> Color(0xFF9C27B0)
        "other" -> Color(0xFFF44336)
        else -> categoryColorPalette[index % categoryColorPalette.size]
    }
}

@Composable
fun ExpenseChart(
    viewModel: ExpenseViewModel,
    modifier: Modifier = Modifier
) {
    val categories by viewModel.categorySummary.collectAsStateWithLifecycle()
    ExpenseChart(categories = categories, modifier = modifier)
}

@Composable
fun ExpenseChart(
    categories: List<CategorySummary>,
    modifier: Modifier = Modifier
) {
    val totalAmount = categories.sumOf { it.totalAmount }.toFloat()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            // -------------------------
            // DONUT CHART
            // -------------------------

            Box(
                modifier = Modifier.size(160.dp),
                contentAlignment = Alignment.Center
            ) {

                Canvas(
                    modifier = Modifier.size(160.dp)
                ) {

                    val strokeWidth = 28.dp.toPx()
                    val arcOffset = Offset(strokeWidth / 2f, strokeWidth / 2f)
                    val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)

                    val textPaint = Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 12.sp.toPx()
                        textAlign = Paint.Align.CENTER
                        isAntiAlias = true
                        typeface = Typeface.DEFAULT_BOLD
                    }

                    fun drawPercentage(
                        startAngle: Float,
                        sweepAngle: Float,
                        percentage: Float
                    ) {
                        if (percentage < 3f) return

                        val middleAngle = startAngle + sweepAngle / 2f
                        val centerRadius = (size.minDimension - strokeWidth) / 2f
                        val angleInRadians = Math.toRadians(middleAngle.toDouble())

                        val x = size.width / 2f +
                                centerRadius * cos(angleInRadians).toFloat()

                        val y = size.height / 2f +
                                centerRadius * sin(angleInRadians).toFloat()

                        val textY = y - (textPaint.descent() + textPaint.ascent()) / 2f

                        drawContext.canvas.nativeCanvas.drawText(
                            "${percentage.toInt()}%",
                            x,
                            textY,
                            textPaint
                        )
                    }

                    if (categories.isEmpty() || totalAmount == 0f) {
                        drawArc(
                            color = Color.LightGray.copy(alpha = 0.4f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = arcOffset,
                            size = arcSize,
                            style = Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Butt
                            )
                        )
                    } else {
                        var startAngle = -90f
                        categories.forEachIndexed { index, item ->
                            val amount = item.totalAmount.toFloat()
                            if (amount > 0f) {
                                val sweepAngle = (amount / totalAmount) * 360f
                                val percentage = (amount / totalAmount) * 100f
                                val color = getCategoryColor(item.category, index)

                                drawArc(
                                    color = color,
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    topLeft = arcOffset,
                                    size = arcSize,
                                    style = Stroke(
                                        width = strokeWidth,
                                        cap = StrokeCap.Butt
                                    )
                                )

                                drawPercentage(
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    percentage = percentage
                                )

                                startAngle += sweepAngle
                            }
                        }
                    }
                }

                // -------------------------
                // CENTER OF DONUT
                // -------------------------

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Total",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "₹${totalAmount.toInt()}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(30.dp)
            )

            // -------------------------
            // CATEGORY LIST
            // -------------------------

            if (categories.isEmpty()) {
                Text(
                    text = "No expenses yet",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            } else {
                Column(
                    modifier = Modifier
                        .heightIn(max = 160.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    categories.forEachIndexed { index, item ->
                        ChartCategory(
                            color = getCategoryColor(item.category, index),
                            category = item.category,
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ChartCategory(
    color: Color,
    category: String,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        // Small colored circle
        Canvas(
            modifier = Modifier.size(10.dp)
        ) {
            drawCircle(
                color = color
            )
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = category,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExpenseChartPreview() {
    val sampleCategories = listOf(
        CategorySummary(category = "Food", transactionCount = 5, totalAmount = 3500.0),
        CategorySummary(category = "Transport", transactionCount = 3, totalAmount = 2000.0),
        CategorySummary(category = "Shopping", transactionCount = 2, totalAmount = 1500.0),
        CategorySummary(category = "Entertainment", transactionCount = 4, totalAmount = 1000.0),
        CategorySummary(category = "Other", transactionCount = 1, totalAmount = 500.0)
    )
    ExpenseChart(categories = sampleCategories)
}
