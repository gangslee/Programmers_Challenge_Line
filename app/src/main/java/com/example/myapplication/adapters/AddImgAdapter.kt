package com.example.myapplication.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R


class AddImgAdapter(private val context: Context, data : ArrayList<String>) : RecyclerView.Adapter<AddImgAdapter.ListItemViewHolder>(){
    val Image = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.imgs_while_writing, parent, false)
        return ListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Image.size
    }


    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val options = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(context).load(Image[position]).apply(options).into(holder.img as ImageView)
    }

    inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img : ImageView? = null
        init {
            img = itemView.findViewById(R.id.img_while_writing) as ImageView
        }
    }
}
