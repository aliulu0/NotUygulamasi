package com.ulutech.notuygulamasi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ulutech.notuygulamasi.R
import com.ulutech.notuygulamasi.model.Not
import com.ulutech.notuygulamasi.view.NotDetayiActivity
import kotlinx.android.synthetic.main.recycler_row.view.*

class NotRecyclerAdapter(val notBaslikListesi : List<String>,val notIcerikListesi : List<String>, val notIdListesi: List<Int> ) : RecyclerView.Adapter<NotRecyclerAdapter.NotVH>() {
    inner class NotVH(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row,parent,false)
        return NotVH(view)
    }

    override fun onBindViewHolder(holder: NotVH, position: Int) {
        holder.itemView.recycler_baslik.text = notBaslikListesi[position]
        holder.itemView.recycler_not.text = notIcerikListesi[position]
        holder.itemView.card_view.setOnClickListener {
            val intent = Intent(holder.itemView.context,NotDetayiActivity::class.java)
            intent.putExtra("id",notIdListesi.get(position))
            intent.putExtra("baslik",notBaslikListesi.get(position))
            intent.putExtra("icerik",notIcerikListesi.get(position))
            intent.putExtra("bilgi","adapter")
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return notIdListesi.size
    }
}