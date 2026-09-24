package com.example.expensetracker.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.MaterialTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEditorDialog(
    expense: Expense? = null,
    onCancel: () -> Unit,
    onSave: (Expense) -> Unit,
) {

    var amount by remember(expense) { mutableStateOf(expense?.amount?.toString() ?: "") }
    var category by remember(expense) { mutableStateOf(expense?.category ?: "") }

    var selectedDate by remember(expense) {
        mutableStateOf(expense?.date ?: System.currentTimeMillis())
    }
    var showDatePicker by remember {
        mutableStateOf(false)
    }

    ModalBottomSheet(
        onDismissRequest = { onCancel() },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = if (expense == null) "Add Expense" else "Update Expense",
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
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),

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
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),

                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Choose Date")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                {
                    val amountValue = amount.toDoubleOrNull()

                    if (amountValue != null && category.isNotBlank()) {

                        val resultExpense = expense?.copy(
                            date = selectedDate,
                            amount = amountValue,
                            category = category
                        ) ?: Expense(
                            date = selectedDate,
                            amount = amountValue,
                            category = category
                        )

                        onSave(resultExpense)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (expense == null) "Save" else "Update")
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

