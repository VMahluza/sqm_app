package com.example.sqmhiring.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sqmhiring.dao.EmployeeDao
import com.example.sqmhiring.models.Employee

@Database(entities = [Employee::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}

object DatabaseManager {
    private var database: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (database == null) {
            database = Room.databaseBuilder(context, AppDatabase::class.java, "sqm_db")
                .fallbackToDestructiveMigration()
                .build()
        }
        return database as AppDatabase
    }
}
