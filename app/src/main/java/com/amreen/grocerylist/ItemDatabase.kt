package com.amreen.grocerylist

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ItemEntity::class],version=1)
abstract class ItemDatabase: RoomDatabase() {
    abstract fun itemDao():ItemDao
}