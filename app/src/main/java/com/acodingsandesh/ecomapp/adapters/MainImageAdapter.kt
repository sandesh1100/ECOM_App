package com.acodingsandesh.ecomapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.acodingsandesh.ecomapp.R
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainImageAdapter(private val imagesList: ArrayList<String>): RecyclerView.Adapter<MainImageAdapter.MainImageViewHolder>() {
    private val TAG = MainImageAdapter::class.java.name

    inner class MainImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ImageView>(R.id.itemImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_image, parent, false)
        return MainImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    override fun onBindViewHolder(holder: MainImageViewHolder, position: Int) {
        Log.d(TAG, "inside onBindViewHolder")
        CoroutineScope(Dispatchers.Main).launch {
            Log.d(TAG, "inside CoroutineScope")
            holder.imageView.layout(0, 0, 0, 0) //resizing images to 0
            Glide.with(holder.imageView.context)
                .load(imagesList[position])
                .placeholder(R.drawable.outline_hourglass_top_24)
                .into(holder.imageView)
        }
    }
}