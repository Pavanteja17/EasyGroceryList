package com.amreen.grocerylist

import android.content.Context
import android.os.AsyncTask
import android.os.FileObserver.DELETE
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import org.w3c.dom.Text
import java.sql.Array

class MasterAdapter(val context: Context, val orderList: List<ItemEntity>,val listener:removeList):
    RecyclerView.Adapter<MasterAdapter.MasterViewHolder>()  {
    interface removeList{
        fun removeItem(itemEntity: ItemEntity)
    }
    class MasterViewHolder(view: View): RecyclerView.ViewHolder(view){
        val itemname: TextView =view.findViewById(R.id.item_name)
        val quantity: TextView =view.findViewById(R.id.quantity)
        val delete:ImageButton=view.findViewById(R.id.img_button)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.primary_list_single_row,parent,false)
        return MasterViewHolder(
            view
        )

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: MasterViewHolder, position: Int) {
        var item=orderList[position]
        holder.itemname.text="${position+1}. "+item.itemname
        holder.quantity.text=item.quantity
        val itemEntity=ItemEntity(item.itemname,item.quantity,item.check)
        holder.delete.setOnClickListener {
            val checkFav = DBAsyncTask(
                context,
                itemEntity,
                2
            ).execute()
            listener.removeItem(itemEntity)
            Toast.makeText(context,"${item.itemname} deleted",Toast.LENGTH_SHORT).show()


        }

    }
    class DBAsyncTask(val context: Context, val itemEntity:ItemEntity, val mode:Int):
        AsyncTask<Void, Void, Boolean>(){
        val db= Room.databaseBuilder(context, ItemDatabase::class.java,"itemlist-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){
                1->{
                    val resta:ItemEntity?=db.itemDao().getResById(itemEntity.itemname)
                    db.close()
                    return resta!=null
                }
                2->{
                    db.itemDao().deleteItem(itemEntity)
                    db.close()
                    return true

                }
            }
            return false

        }


}
}
