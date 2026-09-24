package com.example.expensetracker.data.RoomDatabase

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(
    entities = [Expense::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = ExpenseDatabase.DeleteTitleMigration::class)
    ]
)
abstract class ExpenseDatabase: RoomDatabase() {

    @DeleteColumn(tableName = "expenses", columnName = "title")
    class DeleteTitleMigration : AutoMigrationSpec


    abstract fun expenseDao(): ExpenseDao

    companion object{
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getDatabase(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_database"
                ).fallbackToDestructiveMigration(true)
                    .build()

                INSTANCE = instance

                instance

            }
        }

    }

}