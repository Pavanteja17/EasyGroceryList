package com.amreen.grocerylist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.room.Room
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson

class MasterList : Fragment() {
    lateinit var add:FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LayoutManager
    var itemList= arrayListOf<ItemEntity>()
    lateinit var adapter: MasterAdapter
    lateinit var ntg_toShow:RelativeLayout
    var mInterstitialAd:InterstitialAd?=null
    lateinit var mAdView:AdView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_master_list, container, false)
        add=view.findViewById(R.id.fab)
        recyclerView=view.findViewById(R.id.recyclerList)
        setHasOptionsMenu(true)
        ntg_toShow=view.findViewById(R.id.ntg_to_show)
        val handler= Handler()
        handler.postDelayed({
            mAdView=view.findViewById(R.id.adView)
            MobileAds.initialize(activity as Context) {}

            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)

        },500)
        ntg_toShow.visibility=View.GONE
        prepareAd()

        add.setOnClickListener {
            val intent=Intent(activity as Context,AddItem::class.java)
            startActivity(intent)

            /*val sendIntent=Intent().apply {
                action=Intent.ACTION_SEND_MULTIPLE
                putExtra(Intent.EXTRA_TEXT,listp)
                type="text/json"
            }
            val shareIntent=Intent.createChooser(sendIntent,null)
            startActivity(shareIntent)*/
        }
        layoutManager=LinearLayoutManager(activity as Context)
        itemList=RetrieveItems(
            activity as Context
        ).execute().get() as ArrayList<ItemEntity>
        if(itemList.isEmpty()){
            ntg_toShow.visibility=View.VISIBLE

        }
        else{
            if(activity!=null)
            {
                 adapter=
                    MasterAdapter(
                        activity as Context,
                        itemList,object :MasterAdapter.removeList {
                            override fun removeItem(itemEntity: ItemEntity) {
                                itemList.remove(itemEntity)
                                adapter.notifyDataSetChanged()
                                if(itemList.isEmpty()){
                                    ntg_toShow.visibility=View.VISIBLE
                                }
                            }
                        }
                    )
                recyclerView.adapter=adapter
                recyclerView.layoutManager=layoutManager
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
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.all_delete,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==R.id.delete){
            DeleteItems(activity as Context).execute()
            recyclerView.visibility=View.GONE
            ntg_toShow.visibility=View.VISIBLE
        }
        if(id==R.id.ad){
            if(mInterstitialAd?.isLoaded==true){
                mInterstitialAd?.show()
                prepareAd()
            }
            else{
                Toast.makeText(activity as Context,"Ad not loaded..! please click again ",Toast.LENGTH_SHORT).show()
                prepareAd()
            }
        }
        if(id==R.id.share){
            if(itemList.isEmpty()){
                Toast.makeText(activity as Context,"Your shopping list empty",Toast.LENGTH_SHORT).show()
            }
            else {


                Toast.makeText(
                    activity as Context,
                    "Opening App's...please wait",
                    Toast.LENGTH_SHORT
                ).show()
                var listp = "Grocery " +
                        "items to be bought for me...\n" + "\n"
                for (i in 0 until itemList.size) {
                    val name = itemList[i]
                    if (name.quantity.length == 0) {
                        listp = listp + name.itemname + "\n"
                    } else {
                        listp = listp + name.itemname + "---> " + name.quantity + "\n"
                    }
                }
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND_MULTIPLE
                    putExtra(Intent.EXTRA_TEXT, listp)
                    type = "text/json"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }


        }
        return super.onOptionsItemSelected(item)
    }
    class DeleteItems(val context: Context) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg p0: Void?): Boolean {
            val db =
                Room.databaseBuilder(context, ItemDatabase::class.java, "itemlist-db").build()
            db.itemDao().deleteAll()
            db.close()

            return true
        }

    }
    fun prepareAd(){
        mInterstitialAd = InterstitialAd(activity as Context)
        mInterstitialAd?.adUnitId = "ca-app-pub-6873376364307728/7280196474"
        mInterstitialAd?.loadAd(AdRequest.Builder().build())
    }

    override fun onDestroy() {
        mAdView.destroy()
        mInterstitialAd=null
        super.onDestroy()
    }

}