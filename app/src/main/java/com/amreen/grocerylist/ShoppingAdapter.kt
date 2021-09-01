package com.amreen.grocerylist

import android.content.Context
import android.os.AsyncTask
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.shopping_list_single_row.view.*

class ShoppingAdapter(val context: Context, val orderList: ArrayList<ItemEntity>,val listener:OnItemClickListener):RecyclerView.Adapter<ShoppingAdapter.MasterViewHolder>() {
    var checkBoxStateArray=SparseBooleanArray()
    inner class MasterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemname: TextView = view.findViewById(R.id.item_name)
        val quantity: TextView = view.findViewById(R.id.quantity)
        val checkBox: CheckBox = view.findViewById(R.id.check_box)
        init {
            checkBox.setOnClickListener {
                if(!checkBoxStateArray.get(adapterPosition,false)){
                    checkBox.isChecked=true
                    checkBoxStateArray.put(adapterPosition,true)
                    listener.onAddItemClick(ItemEntity(itemname.text.toString(),quantity.text.toString(),""))
                }
                else{
                    checkBox.isChecked=false
                    checkBoxStateArray.put(adapterPosition,false)
                    listener.onRemoveItemClick(ItemEntity(itemname.text.toString(),quantity.text.toString(),""))
                }
            }
        }


    }

    interface OnItemClickListener {
        fun onAddItemClick(itemEntity: ItemEntity)
        fun onRemoveItemClick(itemEntity: ItemEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_list_single_row, parent, false)
        return MasterViewHolder(
            view
        )

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: MasterViewHolder, position: Int) {
        var item = orderList[position]
        holder.itemname.text = item.itemname
        holder.quantity.text = item.quantity
        val check = item.check
        if(!checkBoxStateArray.get(position,false)){
            holder.checkBox.isChecked=false
        }
        else{
            holder.checkBox.isChecked=true
        }

    }

    override fun onViewRecycled(@NonNull holder: MasterViewHolder) {
        holder.checkBox.setOnCheckedChangeListener(null)
        super.onViewRecycled(holder)
    }
}
