package com.amreen.grocerylist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "itemlist")
data class ItemEntity(
    @PrimaryKey val itemname:String,
    @ColumnInfo(name = "quantity") val quantity:String,
    @ColumnInfo(name="check") val check:String
)