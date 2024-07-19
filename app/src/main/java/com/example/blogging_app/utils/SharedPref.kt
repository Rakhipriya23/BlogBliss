package com.example.blogging_app.utils

import android.content.Context.MODE_PRIVATE
import android.content.Context


object SharedPref {
    fun storeData(
        username: String,
        email: String,
       password:String,
        imageUri: String,
        context: Context
    ) {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putString("imageUri", imageUri)
        editor.apply()
    }

    fun getUserName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("username", "") ?: ""
    }
    fun getEmail(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("email", "") ?: ""
    }
    fun getPassword(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("password", "") ?: ""
    }
    fun getImage(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("imageUri", "") ?: ""
    }


}
