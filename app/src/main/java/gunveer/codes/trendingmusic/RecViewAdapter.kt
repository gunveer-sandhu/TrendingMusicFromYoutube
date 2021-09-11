package gunveer.codes.trendingmusic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecViewAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<RecViewAdapter.RecViewHolder>() {

    private var dataSet: ArrayList<Videos> = ArrayList()

     inner class RecViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvChannel: TextView = itemView.findViewById(R.id.tvChannel)
         init {
             itemView.setOnClickListener{
                 itemView.setOnClickListener(this)
             }
         }
         override fun onClick(v: View?) {
             val position = adapterPosition
             if(position != RecyclerView.NO_POSITION){
                 listener.onItemClick(position)
             }
         }
     }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rec_view_layout, parent, false)
        return RecViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecViewHolder, position: Int) {
        Glide.with(holder.imageView.context).load(dataSet[position].thumbnail).into(holder.imageView)
        holder.tvTitle.text = dataSet[position].videoTitle
        holder.tvChannel.text = dataSet[position].channelTitle
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun updateList(videos: java.util.ArrayList<Videos>) {
        dataSet.clear()
        dataSet.addAll(videos)
        notifyDataSetChanged()
    }
}