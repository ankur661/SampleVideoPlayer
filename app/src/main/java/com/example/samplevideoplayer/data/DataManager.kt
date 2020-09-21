package com.example.samplevideoplayer.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


class DataManager {
    companion object {
        private lateinit var pref: SharedPreferences
        private lateinit var dataManager: DataManager

        fun getInstance(context: Context): DataManager {
            if (!:: dataManager.isInitialized || dataManager == null) {
                dataManager = DataManager(context)
            }
            return dataManager
        }
    }

    private constructor(context: Context) {
        pref = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }


    fun saveList(key: String, list: ArrayList<String>) {
        var set = HashSet<String>()
        set.addAll(list)
        pref.edit().putStringSet(key, set).commit()
    }

    fun getList(key: String): ArrayList<String> {
        var set = pref.getStringSet(key, null)

        var list = ArrayList<String>()
        set?.let { list.addAll(it) }

        return list
    }

}