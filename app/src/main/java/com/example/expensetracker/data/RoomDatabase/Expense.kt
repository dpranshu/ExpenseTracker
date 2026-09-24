package com.example.expensetracker.data.RoomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long,
//    val title: String,
    val amount: Double?,
    val category: String
)
