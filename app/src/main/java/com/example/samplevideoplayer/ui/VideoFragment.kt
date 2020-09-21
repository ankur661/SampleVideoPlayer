package com.example.samplevideoplayer.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.samplevideoplayer.R
import com.example.samplevideoplayer.data.DataManager
import com.example.samplevideoplayer.utils.AppConstants
import com.example.samplevideoplayer.utils.AppUtils
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import java.lang.Exception


class VideoFragment : Fragment() {

    private lateinit var player_view: PlayerView
    private lateinit var player: SimpleExoPlayer
    private var playWhenReady: Boolean = true
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private lateinit var btn_bookmark: ImageView

    companion object {
        fun newInstance() = VideoFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        initiliseViewModels()
        setUpObservers()
    }

    override fun onStart() {
        super.onStart()
        initialisePlayer()
        try {
            viewModel.checkIfSelectedVideoBookmarked(requireContext())
//            updateBookmarkIconForSelectedVideo(viewModel.selectedVideo.value!!)
            playSelectedVideo(viewModel.selectedVideo.value!!)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun updateBookmarkIconForSelectedVideo(position: Int) {
        val videos = viewModel.videos.value
        val selectedVideoInfo = videos?.get(position)
        val pref = DataManager.getInstance(requireContext())

        val isBookmarked: Boolean? = selectedVideoInfo?.let {
            AppUtils.isBookmarked(
                selectedVideoInfo?.path,
                pref.getList(AppConstants.KEY_BOOKMARKS)
            )
        }

        isBookmarked?.let {

        }

    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initiliseViewModels() {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private fun setUpObservers() {
        viewModel.selectedVideo.observe(viewLifecycleOwner, Observer { playSelectedVideo(it) })
        viewModel.isSelectedVideoBookmarked.observe(
            viewLifecycleOwner,
            Observer { updateBookmarkIcon(it) })
    }

    private fun setupView(view: View) {
        player_view = view.findViewById(R.id.player_view)
        btn_bookmark = view.findViewById(R.id.btn_bookmark)

        btn_bookmark.setOnClickListener {viewModel.toggleBookmark(requireContext())}
    }

    private fun updateBookmarkIcon(isBookmarked: Boolean) {
        if (isBookmarked) {
            btn_bookmark.setImageResource(R.drawable.btn_start_disabled)
        } else {
            btn_bookmark.setImageResource(R.drawable.btn_start_enabled)
        }
    }

    private fun playSelectedVideo(position: Int) {
        val videos = viewModel.videos.value
        val selectedVideoInfo = videos?.get(position)
        val uri = Uri.parse(selectedVideoInfo?.path)
        val mediaSource = buildMediaSource(uri)

        player.playWhenReady = playWhenReady
        player.seekTo(currentWindow, playbackPosition)
        mediaSource?.let { player.prepare(it, false, false) }
    }

    private fun initialisePlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        player_view.setPlayer(player)

    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
        val dataSourcefactory: DataSource.Factory =
            DefaultDataSourceFactory(context, "exoplayer-ankur")
        return ProgressiveMediaSource.Factory(dataSourcefactory).createMediaSource(uri)
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player.playWhenReady
            playbackPosition = player.currentPosition
            currentWindow = player.currentWindowIndex
            player.release()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        player_view.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        )
    }
}
