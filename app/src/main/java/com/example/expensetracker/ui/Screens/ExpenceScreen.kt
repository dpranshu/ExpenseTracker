package com.example.expensetracker.ui.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.ViewModel.ExpenseViewModel
import com.example.expensetracker.data.RoomDatabase.Expense
import com.example.expensetracker.ui.theme.ourBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenceScreen(viewModel: ExpenseViewModel) {


    val categories by viewModel.categorySummary.collectAsStateWithLifecycle()
//    val expenses by viewModel.allExpense.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var expenseToEdit by remember { mutableStateOf<Expense?>(null) }

    // Category whose transactions we want to see
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    expenseToEdit = null
                    showEditDialog = true
                }, ///////////
                shape = CircleShape,
                containerColor = ourBlue,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(7.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "The Tracker",
                modifier = Modifier
                    .padding(top = 37.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExpenseChart(viewModel = viewModel)

            Spacer(modifier = Modifier.height(16.dp))

            if (categories.isEmpty()){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        "No expences added yet!",
                        color = Color.Gray
                    )
                }
            } else{

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        items = categories,
                        key = { it.category }
                    ){ category ->



                        ExpenceItem(
                            category = category.category,
                            transcationCount = category.transactionCount,
                            amount = category.totalAmount,

                            onEditClick = {
                                selectedCategory = category.category
                            },
                            onDeleteClick = { viewModel.deleteCategory(category.category) }
                        )
                    }
                }
            }


        }
    }

    //see transactions
    if (selectedCategory != null) {

        CategoryTransactionsDialog(
            category = selectedCategory!!,
            viewModel = viewModel,

            onDismiss = {
                selectedCategory = null
            },

            onTransactionClick = { expense ->

                // THIS is the important part.
                // We select the actual Expense from Room,
                // including its real ID.
                expenseToEdit = expense

                // Close transaction list
                selectedCategory = null

                // Open editor
                showEditDialog = true
            }
        )
    }

    if(showEditDialog){

        ExpenseEditorDialog(
            expense = expenseToEdit,

            onCancel = {
                showEditDialog = false
                expenseToEdit = null
                       },

            onSave = { expense ->
                if (expense.id == 0) {
                    viewModel.addExpense(expense)
                } else {
                    // Existing transaction.
                    // Because the ID is preserved,
                    // Room updates ONLY this transaction.
                    viewModel.updateExpense(expense)
                }
                showEditDialog = false
                expenseToEdit = null
            }
        )
    }
}

/*
 * Displays the transactions inside one category.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTransactionsDialog(
    category: String,
    viewModel: ExpenseViewModel,
    onDismiss: () -> Unit,
    onTransactionClick: (Expense) -> Unit
) {

    val expenses by viewModel
        .getExpensesByCategory(category)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 30.dp)
        ) {

            Text(
                text = category,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (expenses.isEmpty()) {

                Text(
                    text = "No transactions found.",
                    color = Color.Gray
                )

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        items = expenses,
                        key = { it.id }
                    ) { expense ->

                        TransactionItem(
                            expense = expense,
                            onClick = {
                                onTransactionClick(expense)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(
    expense: Expense,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {

            Text(
                text = "₹${expense.amount ?: 0.0}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = formatTransactionDate(expense.date),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Text(
            text = "Edit",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}


private fun formatTransactionDate(date: Long): String {

    val formatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    )

    return formatter.format(Date(date))
}