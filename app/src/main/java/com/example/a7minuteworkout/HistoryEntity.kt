package com.example.a7minuteworkout

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historyTable")
data class HistoryEntity(
    @PrimaryKey
    val date:String
)
