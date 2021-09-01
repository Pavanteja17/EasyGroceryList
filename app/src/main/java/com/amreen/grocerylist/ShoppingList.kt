package com.amreen.grocerylist

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds


class ShoppingList : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    var itemList= arrayListOf<ItemEntity>()
    lateinit var adapter: ShoppingAdapter
    lateinit var bt:Button
    var idList= arrayListOf<String>()
    var flag=0
    lateinit var mAdView:AdView
    var mInterstitialAd:InterstitialAd?=null
    lateinit var ntg_to_show:RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_shopping_list, container, false)
        recyclerView=view.findViewById(R.id.recyclerList)
        bt=view.findViewById(R.id.bt_end)
        prepareAd()
        layoutManager=LinearLayoutManager(activity as Context)
        ntg_to_show=view.findViewById(R.id.ntg_to_show)
        ntg_to_show.visibility=View.GONE
        itemList=RetrieveItems(activity as Context).execute().get() as ArrayList<ItemEntity>
        openThing()

        bt.setOnClickListener {
            if(idList.isEmpty()){
                Toast.makeText(activity as Context,"No items selected",Toast.LENGTH_SHORT).show()
            }
            else {
                val dialog=AlertDialog.Builder(activity as Context).setCancelable(false)
                dialog.setTitle("Confirmation")
                dialog.setMessage("Are you sure you want to delete ${idList.size} items")
                dialog.setPositiveButton("YES"){_,_->
                    val success = DeleteItems(activity as Context, idList).execute().get()
                    Toast.makeText(
                        activity as Context,
                        "${idList.size} items shopped",
                        Toast.LENGTH_LONG
                    ).show()
                    idList.clear()
                    flag = 1
                    openThing()
                }
                dialog.setNegativeButton("NO"){_,_->
                    if (mInterstitialAd?.isLoaded == true) {
                        mInterstitialAd?.show()
                        prepareAd()
                    }
                }
                dialog.create()
                dialog.show()
            }



        }
        return view
    }
    class RetrieveItems(val context: Context) : AsyncTask<Void, Void, List<ItemEntity>>() {

        override fun doInBackground(vararg p0: Void?): List<ItemEntity> {
            val db =
                Room.databaseBuilder(context, ItemDatabase::class.java, "itemlist-db").build()
            val s=db.itemDao().getAll()
            db.close()

            return s
        }

    }
    class DeleteItems(val context: Context,val list: ArrayList<String>) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg p0: Void?): Boolean {
            val db =
                Room.databaseBuilder(context, ItemDatabase::class.java, "itemlist-db").build()
            db.itemDao().deleteMultiple(list)
            db.close()

            return true
        }

    }
    fun openThing(){
        itemList=RetrieveItems(activity as Context).execute().get() as ArrayList<ItemEntity>
        if(itemList.isEmpty() && flag==0){
            ntg_to_show.visibility=View.VISIBLE
            bt.visibility=View.GONE
        }
        if(itemList.isEmpty() && flag==1){
            val intent= Intent(activity as Context,LastActivity::class.java)
            startActivity(intent)
        }
        adapter=ShoppingAdapter(activity as Context,itemList,object :ShoppingAdapter.OnItemClickListener{
            override fun onAddItemClick(itemEntity: ItemEntity) {
                idList.add(itemEntity.itemname)


            }

            override fun onRemoveItemClick(itemEntity: ItemEntity) {
                idList.remove(itemEntity.itemname)
            }
        })
        recyclerView.adapter=adapter
        recyclerView.layoutManager=layoutManager
    }
    fun prepareAd(){
        mInterstitialAd = InterstitialAd(activity as Context)
        mInterstitialAd?.adUnitId = "ca-app-pub-6873376364307728/4095823451"
        mInterstitialAd?.loadAd(AdRequest.Builder().build())
    }

    override fun onDestroy() {
        mInterstitialAd=null
        super.onDestroy()
    }


}