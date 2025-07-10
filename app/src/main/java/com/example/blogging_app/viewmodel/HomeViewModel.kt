package com.example.blogging_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blogging_app.model.ThreadModel
import com.example.blogging_app.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel : ViewModel() {
     private val db = FirebaseDatabase.getInstance()
     private val threadRef = db.getReference("posts")

     // LiveData to observe threads and users
     private val _threadsAndUsers = MutableLiveData<List<Pair<ThreadModel, UserModel>>>()
     val threadsAndUsers: LiveData<List<Pair<ThreadModel, UserModel>>> get() = _threadsAndUsers

     init {
          fetchThreadsAndUsers()
     }

     private fun fetchThreadsAndUsers() {
          threadRef.addValueEventListener(object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                    val result = mutableListOf<Pair<ThreadModel, UserModel>>()
                    val threadCount = snapshot.childrenCount

                    var fetchedThreads = 0
                    for (threadSnapshot in snapshot.children) {
                         val thread = threadSnapshot.getValue(ThreadModel::class.java)
                         thread?.let {
                              fetchUserFromThread(it) { user ->
                                   result.add(it to user)
                                   fetchedThreads++
                                   if (fetchedThreads == threadCount.toInt()) {
                                        _threadsAndUsers.postValue(result)
                                        Log.d("HomeViewModel", "Number of threads fetched: $fetchedThreads")
                                   }
                              }
                         }
                    }
               }

               override fun onCancelled(error: DatabaseError) {
                    Log.e("HomeViewModel", "Error fetching threads: ${error.message}")
               }
          })
     }

     private fun fetchUserFromThread(thread: ThreadModel, onResult: (UserModel) -> Unit) {
          db.getReference("users").child(thread.userId)
               .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                         val user = snapshot.getValue(UserModel::class.java)
                         user?.let {
                              Log.d("HomeViewModel", "Fetched user: ${it.username} for thread: ${thread.title}")
                              onResult(it)
                         }
                    }

                    override fun onCancelled(error: DatabaseError) {
                         Log.e("HomeViewModel", "Error fetching user: ${error.message}")
                    }
               })
     }
}

