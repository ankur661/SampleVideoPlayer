package com.example.samplevideoplayer.ui

import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.samplevideoplayer.binding.VideoItem
import com.example.samplevideoplayer.databinding.ListItemBinding
import com.example.samplevideoplayer.listeners.AdapterVideoItemClickListener
import com.example.samplevideoplayer.model.VideoInfo

class VideosAdapter(private val videos: ArrayList<VideoInfo>) :
    RecyclerView.Adapter<VideosAdapter.MyViewHolder>() {

    lateinit var mClickListener: AdapterVideoItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val listItemBinding =
            ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(listItemBinding)
    }


    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            val videoItem = VideoItem(
                ThumbnailUtils.createVideoThumbnail(
                    videos.get(position).path, MediaStore.Video.Thumbnails.MINI_KIND
                ), videos.get(position).videoName
            )

            holder.listItemBinding.item = videoItem
            holder.listItemBinding.executePendingBindings()
            holder.itemView.setOnClickListener({
                mClickListener.onItemClicked(position)
            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    fun setData(videos: ArrayList<VideoInfo>) {
        this.videos.clear()
        this.videos.addAll(videos)
        notifyDataSetChanged()
    }

    fun setItemClickListener(clickLstener: AdapterVideoItemClickListener) {
        this.mClickListener = clickLstener
    }

    class MyViewHolder(val listItemBinding: ListItemBinding) :
        RecyclerView.ViewHolder(listItemBinding.root) {

    }
}