package com.example.samplevideoplayer.utils

object AppUtils {

    fun isBookmarked(path: String, bookmarksList: List<String>): Boolean =
        bookmarksList.contains(path)

}