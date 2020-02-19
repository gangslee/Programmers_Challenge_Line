package com.example.myapplication.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R


class AddImgAdapter(private val context: Context, data : ArrayList<String>) : RecyclerView.Adapter<AddImgAdapter.ListItemViewHolder>(){

    private val image = data

    interface BtClick{
        fun onClick(position: Int)
    }
    var btClick : BtClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.imgs_while_writing, parent, false)
        return ListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return image.size
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        if(position == 0) holder.icon?.visibility = View.VISIBLE
        val options = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(context).load(image[position]).apply(options).into(holder.img as ImageView)
        holder.bt?.setOnClickListener {
            btClick?.onClick(position)
        }
    }


    inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img : ImageView? = null
        var icon : ImageView? = null
        var bt : Button? = null
        init {
            img = itemView.findViewById(R.id.img_while_writing) as ImageView
            icon = itemView.findViewById(R.id.firstIcon) as ImageView
            bt = itemView.findViewById(R.id.img_delete_bt) as Button
        }
    }
}
