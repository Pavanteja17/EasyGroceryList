package com.amreen.grocerylist

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.room.Room
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_add_item.*
import java.lang.Exception

class AddItem : AppCompatActivity() {
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    lateinit var itemName:EditText
    lateinit var quantity:EditText
    lateinit var button: Button
    lateinit var mAdView : AdView
    lateinit var imgBt:ImageButton
    lateinit var imgB:ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
         val handler=Handler()
        handler.postDelayed({
            mAdView=findViewById(R.id.adView)
            MobileAds.initialize(this) {}

            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)

        },500)


        toolbar=findViewById(R.id.toolbar)
        imgBt=findViewById(R.id.img_bt)
        imgB=findViewById(R.id.imgimg)
        itemName=findViewById(R.id.et_name)
        quantity=findViewById(R.id.et_quantity)
        button=findViewById(R.id.bt_add)
        imgB.setOnClickListener {
            startVoiceRecognitionActivity(1235)
        }

        imgBt.setOnClickListener {
            startVoiceRecognitionActivity(1234)

        }
        bt_add.setOnClickListener {
            if (itemName.text.toString().length == 0) {
                Toast.makeText(this@AddItem, "Item name shouldn't be empty", Toast.LENGTH_SHORT)
                    .show()
            } else {

                it.hideKeyboard()
                val itemEntity = ItemEntity(itemName.text.toString(), quantity.text.toString(),"no")
                val checkPresent = DBAsyncTask(
                    applicationContext,
                    itemEntity,
                    1
                ).execute()
                val isPresent = checkPresent.get()
                if (isPresent) {
                    Toast.makeText(this@AddItem, "Item already present", Toast.LENGTH_SHORT).show()

                } else {
                    val async = DBAsyncTask(
                        applicationContext,
                        itemEntity,
                        2
                    ).execute()
                    itemName.text.clear()
                    quantity.text.clear()
                    bt_add.setText("ADD ANOTHER ITEM")
                    Toast.makeText(this@AddItem,"Item Added",Toast.LENGTH_SHORT).show()


                }

            }
        }

        setUpToolbar()
    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="New Item"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    fun View.hideKeyboard(){
        val inputManager=context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken,0)
    }
    

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
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
                    db.itemDao().insertItem(itemEntity)
                    db.close()
                    return true

                }
            }
            return false

        }

    }


    override fun onBackPressed() {
        val intent=Intent(this@AddItem,MainActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }

    override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }
    private fun startVoiceRecognitionActivity(requestCode:Int){
        val intent=Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Try saying milk")
        startActivityForResult(intent,requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try{
            var array= arrayListOf<String>()
            array=data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if(requestCode==1234){
                itemName.setText(array[0])
            }
            else{
                quantity.setText(array[0])
            }
        }
        catch (e:Exception){

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}

