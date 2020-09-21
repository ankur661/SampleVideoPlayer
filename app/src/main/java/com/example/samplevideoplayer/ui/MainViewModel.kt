package com.example.samplevideoplayer.ui

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.samplevideoplayer.data.DataManager
import com.example.samplevideoplayer.model.VideoInfo
import com.example.samplevideoplayer.utils.AppConstants
import com.example.samplevideoplayer.utils.AppUtils


class MainViewModel : ViewModel() {

    val videos: MutableLiveData<List<VideoInfo>> = MutableLiveData()
    val selectedVideo: MutableLiveData<Int> = MutableLiveData()
    val isSelectedVideoBookmarked: MutableLiveData<Boolean> = MutableLiveData()

    fun fetchMedia(context: Context?) {
        var video_list: ArrayList<VideoInfo> = ArrayList()
        val projection = arrayOf(
            MediaStore.Video.VideoColumns.DATA,
            MediaStore.Video.VideoColumns.DISPLAY_NAME,
            MediaStore.Video.VideoColumns.DURATION
        )

        val cursor: Cursor = context?.contentResolver?.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null
        )!!

        try {
            cursor.moveToFirst()
            do {
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                val path =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                val duration =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))

                video_list.add(VideoInfo(name, path, duration))
            } while (cursor.moveToNext())
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        videos.postValue(video_list)
    }

    fun checkIfSelectedVideoBookmarked(context: Context) {
        val videos = videos.value
        val selectedVideoInfo = videos?.get(selectedVideo.value!!)
        val pref = DataManager.getInstance(context)

        val isBookmarked = selectedVideoInfo?.let {
            AppUtils.isBookmarked(
                selectedVideoInfo?.path,
                pref.getList(AppConstants.KEY_BOOKMARKS)
            )
        }

        isSelectedVideoBookmarked.postValue(isBookmarked)
    }

    fun toggleBookmark(context: Context) {
        val videos = videos.value
        val selectedVideoInfo = videos?.get(selectedVideo.value!!)
        val pref = DataManager.getInstance(context)
        var bookmarks = pref.getList(AppConstants.KEY_BOOKMARKS)

        if (isSelectedVideoBookmarked.value!!) {
            val updatedList = ArrayList<String>()
            for (bookmark in bookmarks) {
                if (!bookmark.equals(selectedVideoInfo?.path)) {
                    updatedList.add(bookmark)
                }
            }
            pref.saveList(AppConstants.KEY_BOOKMARKS, updatedList)
            isSelectedVideoBookmarked.postValue(false)

        } else {
            bookmarks.add(selectedVideoInfo?.path!!)
            pref.saveList(AppConstants.KEY_BOOKMARKS, bookmarks)
            isSelectedVideoBookmarked.postValue(true)
        }
    }
}
