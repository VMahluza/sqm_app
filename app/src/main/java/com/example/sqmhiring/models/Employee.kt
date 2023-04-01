package com.example.sqmhiring.models

import androidx.room.*

@Entity(tableName = "Employee")
@TypeConverters(StringListConverter::class)
data class Employee(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val surname: String,
    val organization: String,
    val gender: String,
    val genres: List<String>
)

class StringListConverter {

    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun toString(list: List<String>): String {
        return list.joinToString(",")
    }
}