package com.example.datastorage.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Note {
    @PrimaryKey
    var id: Long = 0
    var name: String = ""
}