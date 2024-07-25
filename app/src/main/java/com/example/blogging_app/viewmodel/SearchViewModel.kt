package com.example.blogging_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blogging_app.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val usersRef = db.getReference("users")

    private var _users = MutableLiveData<List<UserModel>>()
    val users: LiveData<List<UserModel>> = _users

    init {
        fetchUsers { _users.value = it }
    }

    private fun fetchUsers(onResult: (List<UserModel>) -> Unit) {
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<UserModel>()
                for (threadSnapshot in snapshot.children) {
                    val thread = threadSnapshot.getValue(UserModel::class.java)
                    thread?.let { result.add(it) }
                }
                onResult(result)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }

    fun fetchUserFromThread(threadId: String, onResult: (UserModel?) -> Unit) {
        db.getReference("threads").child(threadId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    onResult(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error if needed
                }
            })
    }
}
