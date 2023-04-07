package com.example.moneymind.ui.Extras

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import java.io.Serializable

@Throws(JsonIOException::class)
fun Serializable.toJson(): String {
    val gson = GsonBuilder().create()
    return gson.toJson(this)
}

@Throws(JsonSyntaxException::class)
fun <T> String.to(type: Class<T>): T where T : Serializable {
    val gson = GsonBuilder().create()
    return gson.fromJson(this, type)
}

@Throws(JsonIOException::class)
fun SharedPreferences.Editor.putSerializable(key: String, o: Serializable?) = apply {
    putString(key, o?.toJson())
}

@Throws(JsonSyntaxException::class)
fun <T> SharedPreferences.getSerializable(key: String, type: Class<T>): T? where T : Serializable {
    return getString(key, null)?.to(type)
}