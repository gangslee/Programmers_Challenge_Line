package com.example.myapplication.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.aboutDB.PostData


class AddMemoAdapter(private val context: Context, data : ArrayList<PostData>) : RecyclerView.Adapter<AddMemoAdapter.ListItemViewHolder>(){

    private val pd = data

    interface MemoClick{
        fun onClick(position: Int)
    }
    var memoClick : MemoClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.memo_format, parent, false)
        return ListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pd.size
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val options = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(context)
            .load(pd[position].imgList.substring(1, pd[position].imgList.length-1).split(", ")[0])
            .apply(options)
            .into(holder.img as ImageView)
        holder.title?.text = pd[position].title
        holder.content?.text = pd[position].content
        holder.date?.text = pd[position].date
        if(memoClick != null){
            holder.itemView.setOnClickListener {
                memoClick?.onClick(position)
            }
        }

    }

    inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img : ImageView? = null
        var title : TextView? = null
        var content : TextView? = null
        var date : TextView? = null
        init {
            img = itemView.findViewById(R.id.memo_format_thumbnail) as ImageView
            title = itemView.findViewById(R.id.memo_format_title) as TextView
            content = itemView.findViewById(R.id.memo_format_content) as TextView
            date = itemView.findViewById(R.id.memo_format_date) as TextView
        }
    }
}
