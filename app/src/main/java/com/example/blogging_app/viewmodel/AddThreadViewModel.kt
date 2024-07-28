package com.example.blogging_app.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blogging_app.model.ThreadModel
import com.example.blogging_app.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddThreadViewModel : ViewModel() {

     private val db = FirebaseDatabase.getInstance()
     private val userRef = db.getReference("posts")
     private val storageRef = FirebaseStorage.getInstance().reference

     private val _userData = MutableLiveData<UserModel>()
     val userData: LiveData<UserModel> = _userData
     private val _error = MutableLiveData<String>()
     val error: LiveData<String> = _error

     private val _isPosted = MutableLiveData<Boolean>()
     val isPosted: LiveData<Boolean> = _isPosted

      fun saveImage(
          title: String,
          description: String,
          userId: String,
          imageUri: Uri
     ) {
          val imageRef = storageRef.child("posts/${UUID.randomUUID()}.jpg")
          val uploadTask = imageRef.putFile(imageUri)
          uploadTask.addOnCompleteListener { task ->
               imageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveData(title, description, userId, uri.toString())
               }
          }
     }

      fun saveData(
          title: String,
          description: String,
          userId: String,
          image: String
     ) {
          val threadData = ThreadModel(
               title = title,
               description = description,
               image = image,
               userId = userId,
               timeStamp = System.currentTimeMillis().toString()
          )

          userRef.child(userRef.push().key!!).setValue(threadData)
               .addOnSuccessListener {
                    _isPosted.postValue(true)
               }
               .addOnFailureListener {
                    _isPosted.postValue(false)
               }
     }

     internal fun fetchUserData(uid: String) {
          userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    _userData.postValue(user!!)
               }

               override fun onCancelled(error: DatabaseError) {
                    _error.postValue(error.message)
               }
          })
     }

     fun postThread(title: String, description: String, imageUri: Uri?, userId: String) {
          if (imageUri != null) {
               saveImage(title, description, userId, imageUri)
          } else {
               saveData(title, description, userId, "")
          }
     }
     fun clearInputs() {
          _isPosted.value = false
     }
}
