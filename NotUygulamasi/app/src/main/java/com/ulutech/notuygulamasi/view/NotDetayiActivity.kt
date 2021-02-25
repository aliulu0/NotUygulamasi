package com.ulutech.notuygulamasi.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color.red
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ulutech.notuygulamasi.R
import com.ulutech.notuygulamasi.model.Not
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_not_detayi.*
import kotlinx.android.synthetic.main.recycler_row.*
import kotlinx.android.synthetic.main.renk_sec.view.*
import java.lang.Exception

class NotDetayiActivity : AppCompatActivity() {
    private var gelenId:Int? = 0
    private var gelenBaslik:String? = null
    private var gelenIcerik:String? =null
    private var gelenBilgi :String? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_not_detayi)


        toolbar_detay.title = ""
        setSupportActionBar(toolbar_detay)


        gelenId = intent.getIntExtra("id",-1)
        gelenBaslik = intent.getStringExtra("baslik")
        gelenIcerik = intent.getStringExtra("icerik")
        gelenBilgi = intent.getStringExtra("bilgi")

        if (gelenBilgi.equals("adapter")){
            getNot(gelenId)
        }
        imageViewBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detay_toolbar_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Silme")
        alert.setMessage("${gelenBaslik} isimli notunuzu silmek istediğinize emin misiniz")
        alert.setPositiveButton("Evet"){dialogInterface,i ->
            if (gelenBilgi.equals("adapter")){
                if (item.itemId == R.id.delete) {

                    val database = this.openOrCreateDatabase("Notlar", MODE_PRIVATE, null)
                    try {
                        val sqlString = "DELETE FROM notlar WHERE id = ?"
                        val statement = database.compileStatement(sqlString)
                        statement.bindString(1,gelenId.toString())
                        statement.execute()
                    }
                    catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        alert.setNegativeButton("Hayır"){dialogInterface,i ->

        }
        alert.create().show()



        return super.onOptionsItemSelected(item)
    }
    fun kaydet(view: View){
        //Veritabani Isleri
        view.context?.let {

            if (gelenBilgi.equals("adapter")) {
                //güncellenecek
                guncelle(gelenId)
            }
            else {
                //eklenecek
                    val baslik = baslik_text.text.toString()
                val icerik = icerik_text.text.toString()
                ekle(baslik, icerik)

            }
            baslik_text.setText("")
            icerik_text.setText("")
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun guncelle(id:Int?){
        try {
            val database = this.openOrCreateDatabase("Notlar", MODE_PRIVATE,null)
            val sqlString = "UPDATE notlar SET notBaslik = ?,notIcerik = ?  WHERE id = ?"
            val statement = database.compileStatement(sqlString)
            statement.bindString(1,baslik_text.text.toString())
            statement.bindString(2,icerik_text.text.toString())
            statement.bindString(3,id.toString())
            statement.execute()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun ekle(baslik:String?,icerik:String?){
        try {
            val database = this.openOrCreateDatabase("Notlar", MODE_PRIVATE,null)
            database.execSQL("CREATE TABLE IF NOT EXISTS notlar(id INTEGER PRIMARY KEY AUTOINCREMENT,notBaslik VARCHAR,notIcerik TEXT)")
            val sqlString = "INSERT INTO notlar(notBaslik,notIcerik) VALUES(?,?)"
            val statement = database.compileStatement(sqlString)
            statement.bindString(1,baslik)
            statement.bindString(2,icerik)
            statement.execute()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun getNot(id: Int?){
        try {
            this.let {
                val database = it.openOrCreateDatabase("Notlar", Context.MODE_PRIVATE,null)

                val cursor = database.rawQuery("SELECT * FROM notlar WHERE id = ?", arrayOf(gelenId.toString()))
                val notBaslikIndex = cursor.getColumnIndex("notBaslik")
                val notIcerikIndex = cursor.getColumnIndex("notIcerik")
                while (cursor.moveToNext()){
                    baslik_text.setText(cursor.getString(notBaslikIndex))
                    icerik_text.setText(cursor.getString(notIcerikIndex))
                }
                cursor.close()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

}
