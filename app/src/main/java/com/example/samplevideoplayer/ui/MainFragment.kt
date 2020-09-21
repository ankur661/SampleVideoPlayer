package com.example.samplevideoplayer.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.SnapHelper
import com.example.samplevideoplayer.R
import com.example.samplevideoplayer.listeners.AdapterVideoItemClickListener
import com.example.samplevideoplayer.listeners.FragmentVideoClickListener
import com.example.samplevideoplayer.model.VideoInfo

class MainFragment : Fragment(), AdapterVideoItemClickListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var videoAdapter: Adapter<VideosAdapter.MyViewHolder>
    private lateinit var list_videos: RecyclerView
    private lateinit var mListener: FragmentVideoClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        initiliseViewModels()
        setUpObservers()

        viewModel.fetchMedia(context);
    }

    private fun initiliseViewModels() {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private fun setUpObservers() {
        viewModel.videos.observe(viewLifecycleOwner, Observer { processAndShowVideos(it) })
    }

    private fun setupView(view: View) {
        list_videos = view.findViewById(R.id.list_videos)
    }

    public fun setOnVideoClickedListener(listener: FragmentVideoClickListener) {
        mListener = listener
    }

    private fun processAndShowVideos(list: List<VideoInfo>) {
        videoAdapter = VideosAdapter(list as ArrayList<VideoInfo>)
        (videoAdapter as VideosAdapter).setItemClickListener(this)

        list_videos.layoutManager = LinearLayoutManager(activity)
        list_videos.adapter = videoAdapter

        val snapHelper: SnapHelper = LinearSnapHelper()
        list_videos.onFlingListener = null
        snapHelper.attachToRecyclerView(list_videos)

    }

    override fun onItemClicked(position: Int) {
        viewModel.selectedVideo.postValue(position)
        mListener.onVideoClicked()
    }

}
