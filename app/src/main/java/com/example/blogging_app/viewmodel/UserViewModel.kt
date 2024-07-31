package com.example.blogging_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blogging_app.model.ThreadModel
import com.example.blogging_app.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val threadRef = db.getReference("posts")
    val userRef = db.getReference("users")

    private val _threads = MutableLiveData<List<ThreadModel>>(emptyList())
    val threads: LiveData<List<ThreadModel>> get() = _threads

    private val _users = MutableLiveData<UserModel>()
    val users: LiveData<UserModel> get() = _users

    // Fetch the user
    fun fetchUser(uid: String) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                user?.let { _users.postValue(it) }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // Fetch the user's own posts
    fun fetchPost(uid: String) {
        threadRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val threadList = snapshot.children.mapNotNull {
                    val thread = it.getValue(ThreadModel::class.java)
                    thread?.copy(id = it.key ?: "")
                }
                _threads.postValue(threadList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // Remove a thread from the list
    fun removeThread(thread: ThreadModel) {
        _threads.value = _threads.value?.filter { it.id != thread.id }
    }
}