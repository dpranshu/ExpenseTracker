package com.example.expensetracker.ui.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.copy
import com.example.expensetracker.data.RoomDatabase.Expense


@Composable
fun ExpenceItem(
    category: String,
    amount: Double,
    transcationCount: Int,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{
                showMenu = !showMenu
            },
        colors =  CardDefaults.cardColors(
            containerColor = if (showMenu){
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                Color.Transparent
            }
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,


        ) {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = category,
                    modifier = Modifier
                    .padding(bottom = 4.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${transcationCount} transaction",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,

                )
            }

                Text(
                    text = "₹${amount}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium

                )
        }
        //show option when showMenu is true
        AnimatedVisibility(
            visible = showMenu
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), /////////???
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                ) {
                    TextButton(
                        modifier = Modifier.weight(1f),
                        onClick = onEditClick,

                    ){
                        Text(
                            text = "Edit",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start)
                    }


                    TextButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Delete",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }



                }
            }
        }






    }


}