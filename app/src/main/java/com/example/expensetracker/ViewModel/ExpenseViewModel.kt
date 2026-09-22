package com.example.expensetracker.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.Repository.ExpenseRepository
import com.example.expensetracker.data.RoomDatabase.Expense
import com.example.expensetracker.data.RoomDatabase.ExpenseDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = ExpenseDatabase.getDatabase(application).expenseDao()

    private val repository = ExpenseRepository(dao)

    val allExpense: StateFlow<List<Expense>> = repository.getAllExpenses()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.insert(expense)
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            repository.update(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.delete(expense)
        }
    }


}