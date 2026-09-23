package com.example.expensetracker.data.RoomDatabase

data class CategorySummary(
    val category: String,
    val transactionCount: Int,
    val totalAmount: Double
)