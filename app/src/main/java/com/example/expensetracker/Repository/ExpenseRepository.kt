package com.example.expensetracker.Repository

import com.example.expensetracker.data.RoomDatabase.CategorySummary
import com.example.expensetracker.data.RoomDatabase.Expense
import com.example.expensetracker.data.RoomDatabase.ExpenseDao
import kotlinx.coroutines.flow.Flow


class ExpenseRepository(
    private val dao: ExpenseDao
) {
    fun getAllExpenses(): Flow<List<Expense>> {
        return dao.getAllExpenses()
    }
    fun getCategorySummary(): Flow<List<CategorySummary>> {
        return dao.getCategorySummary()
    }
    suspend fun insert(expense: Expense) {
        dao.insert(expense)
    }
    suspend fun update(expense: Expense) {
        dao.update(expense)
    }
    suspend fun delete(expense: Expense) {
        dao.delete(expense)
    }
    
}