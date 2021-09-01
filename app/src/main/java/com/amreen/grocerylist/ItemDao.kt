package com.amreen.grocerylist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.sql.Array

@Dao
interface ItemDao {
    @Insert
    fun insertItem(itemEntity: ItemEntity)
    @Query("SELECT * FROM itemlist WHERE itemname=:itemname ")
    fun getResById(itemname:String):ItemEntity
    @Query("SELECT * FROM itemlist")
    fun getAll():List<ItemEntity>
    @Delete
    fun deleteItem(itemEntity: ItemEntity)
    @Query("DELETE FROM itemlist WHERE itemname in (:item)")
    fun deleteMultiple(item:ArrayList<String>)
    @Query("UPDATE itemlist SET `check`=:value WHERE itemname=:itemname")
    fun update(value:String,itemname: String)
    @Query("DELETE FROM itemlist")
    fun deleteAll()
}