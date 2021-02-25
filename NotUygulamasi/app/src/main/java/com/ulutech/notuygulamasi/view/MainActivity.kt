package com.ulutech.notuygulamasi.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ulutech.notuygulamasi.R
import com.ulutech.notuygulamasi.adapter.NotRecyclerAdapter
import com.ulutech.notuygulamasi.model.Not
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_not_detayi.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NotRecyclerAdapter
    var notBaslikListesi = ArrayList<String>()
    var notIcerikListesi = ArrayList<String>()
    var notIdListesi = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar_main.title = "Notes"
        setSupportActionBar(toolbar_main)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotRecyclerAdapter(notBaslikListesi,notIcerikListesi,notIdListesi)
        recyclerView.adapter = adapter

        night_mode.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked){
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            }else{
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            }
        }
        sqlVerisiniAl()

    }
    fun ekle(view: View){
        val intent = Intent(this,NotDetayiActivity::class.java)
        startActivity(intent)
    }

    fun sqlVerisiniAl(){
        try {
            this.let {
                val database = it.openOrCreateDatabase("Notlar", Context.MODE_PRIVATE,null)
                val cursor = database.rawQuery("SELECT * FROM notlar",null)
                val notBaslikIndex = cursor.getColumnIndex("notBaslik")
                val notIcerikIndex = cursor.getColumnIndex("notIcerik")
                val notIdIndex = cursor.getColumnIndex("id")
                notBaslikListesi.clear()
                notIcerikListesi.clear()
                notIdListesi.clear()
                while (cursor.moveToNext()){
                    notIdListesi.add(cursor.getInt(notIdIndex))
                    notBaslikListesi.add(cursor.getString(notBaslikIndex))
                    notIcerikListesi.add(cursor.getString(notIcerikIndex))
                }
                adapter.notifyDataSetChanged()
                cursor.close()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


}