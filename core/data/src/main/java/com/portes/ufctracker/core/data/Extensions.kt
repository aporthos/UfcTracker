package com.portes.ufctracker.core.data

import android.content.res.AssetManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

inline fun <reified T> AssetManager.convertToList(name: String, moshi: Moshi): List<T>? {
    val eventsList = open(name).bufferedReader().use { it.readText() }
    val listType = Types.newParameterizedType(List::class.java, T::class.java)
    val adapter: JsonAdapter<List<T>> = moshi.adapter(listType)
    return adapter.fromJson(eventsList)
}

inline fun <reified T> AssetManager.convertToObject(name: String, moshi: Moshi): T? {
    val eventsList = open(name).bufferedReader().use { it.readText() }
    val adapter: JsonAdapter<T> = moshi.adapter(T::class.java)
    return adapter.fromJson(eventsList)
}
