package com.example.samplevideoplayer.binding

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

data class VideoItem(val bitmap: Bitmap, val name: String) {

    companion object {
        @BindingAdapter("android:loadImageBitmap")
        @JvmStatic
        public fun loadImage(imageView: ImageView, bitmap: Bitmap) =
            imageView.setImageBitmap(bitmap)
    }

}