package com.example.expensetracker.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.data.RoomDatabase.Expense
import com.example.expensetracker.Utils.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEditorDialog(
    onCancel: () -> Unit,
    onSave: (Expense) -> Unit,
) {

    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    var selectedDate by remember {
        mutableStateOf(System.currentTimeMillis())
    }
    var showDatePicker by remember {
        mutableStateOf(false)
    }

    ModalBottomSheet(
        onDismissRequest = { onCancel() },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = "Add Expense",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Enter the details of your expense",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(7.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "e.g.500") },
                label = {
                    Text("Amount")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.DarkGray,
                    unfocusedBorderColor = Color.LightGray
                ),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                placeholder = { Text(text = "Food, Entertainment etc") },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Category")
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            //date

            OutlinedTextField(
                value = formatDate(selectedDate),
                onValueChange = { },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Date")
                },
                trailingIcon = {
                    Text(
                        text = "📅",
                        modifier = Modifier.padding(end = 12.dp)
                    )
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    showDatePicker = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Choose Date")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                {
                    val amountValue = amount.toDoubleOrNull()

                    if (amountValue != null && category.isNotBlank()) {

                        val expense = Expense(
                            date = selectedDate,
                            amount = amountValue,
                            category = category
                        )

                        onSave(expense)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }


        }
    }
    //date picker
    if (showDatePicker){
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
        )
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDate = it
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ){
            DatePicker(
                state = datePickerState
            )

        }
    }


}

