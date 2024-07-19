package com.example.blogging_app.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blogging_app.model.UserModel
import com.example.blogging_app.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AuthViewModel : ViewModel() {
     private val auth = FirebaseAuth.getInstance()
     private val db = FirebaseDatabase.getInstance()
     private val userRef = db.getReference("users")

     private val storageRef = FirebaseStorage.getInstance().reference

     private val _firebaseUser = MutableLiveData<FirebaseUser?>()
     val firebaseUser: LiveData<FirebaseUser?> = _firebaseUser

     private val _error = MutableLiveData<String>()
     val error: LiveData<String> = _error

     init {
          _firebaseUser.value = auth.currentUser
     }

     fun login(email: String, password: String,context: Context) {
          auth.signInWithEmailAndPassword(email, password)
               .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
//                         println("login successful:${auth.currentUser?.email}")
                         _firebaseUser.postValue(auth.currentUser)
                         getData(auth.currentUser!!.uid,context)
                    } else {
                         _error.postValue(task.exception!!.message)
                    }
               }
     }

     private fun getData(uid: String,context: Context) {
          userRef.child(uid).addListenerForSingleValueEvent( object :ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                    val userData=snapshot.getValue(UserModel::class.java)
                    SharedPref.storeData(userData!!.username,userData!!.email,userData!!.password,userData!!.imageUrl,context)
               }

               override fun onCancelled(error: DatabaseError) {

               }


          })       }

     fun register(
          email: String,
          password: String,
          username: String,
          imageUri: Uri,
          context: android.content.Context
     ) {
          auth.createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                         println("User created: ${auth.currentUser}")
                         _firebaseUser.postValue(auth.currentUser)
                         saveImage(email, password, username, imageUri, auth.currentUser?.uid, context)
                    } else {
                         println("User couldn't be created: ${task.exception?.message}")
                         _error.postValue(task.exception?.message)
                    }
               }
     }

     private fun saveImage(
          email: String,
          password: String,
          username: String,
          imageUri: Uri,
          uid: String?,
          context: android.content.Context
     ) {
          val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")
          val uploadTask = imageRef.putFile(imageUri)
          uploadTask.addOnCompleteListener { task ->
               imageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveData(email, password, username, uri.toString(), uid, context)
               }
          }
     }

     private fun saveData(
          email: String,
          password: String,
          username: String,
          imageUrl: String,
          uid: String?,
          context: android.content.Context
     ) {
          val userData = UserModel(email, password, username, imageUrl, uid!!)
          userRef.child(uid).setValue(userData)
               .addOnSuccessListener {
                    SharedPref.storeData(username, email, password, imageUrl, context)
               }
               .addOnFailureListener {
                    _error.postValue(it.message)
               }
     }

     fun logout() {
          auth.signOut()
          _firebaseUser.postValue(null)
     }
}
