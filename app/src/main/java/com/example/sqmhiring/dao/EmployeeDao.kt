package com.example.sqmhiring.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sqmhiring.models.Employee


@Dao
interface EmployeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employee: Employee)

    @Query("SELECT * FROM Employee")
    suspend fun getAllEmployees(): List<Employee>

    @Query("SELECT * FROM Employee WHERE gender = 'Female' ORDER BY id LIMIT 3")
    suspend fun get3Females(): List<Employee>

    @Query("SELECT * FROM Employee WHERE gender = 'Male' ORDER BY id LIMIT 4")
    suspend fun get4Males(): List<Employee>


}
