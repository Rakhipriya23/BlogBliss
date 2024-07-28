package com.example.blogging_app.model

import android.provider.ContactsContract.CommonDataKinds.Email

data class UserModel(
    val email: String ="",
    val password:String="",
    val username:String="",
    val imageUrl: String="",
    val uid:String=""
)

