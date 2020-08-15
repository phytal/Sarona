package com.phytal.sarona.data.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.phytal.sarona.data.provider.LoginInformation


class FirestoreProviderImpl() : FirestoreProvider {
    private val uid = FirebaseAuth.getInstance().uid
    private val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
    override fun saveAccountToDatabase(user: DatabaseUser): Boolean {
        var success = false
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Saved user to Firebase!")
                success = true
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to save user to Firebase..")
            }
        return success
    }

    override fun deleteAccount(): Boolean {
        var success = false
        ref.removeValue()
            .addOnSuccessListener {
                Log.d(TAG, "User account successfully deleted.")
                success = true
            }
            .addOnFailureListener {
                Log.d(TAG, "Could not delete the user account!")
            }
        return success
    }

    override fun getAccount(): DatabaseUser {
        var user = DatabaseUser("", false, ArrayList())
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Failed to retrieve account with uid $uid from Firestore!")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(DatabaseUser::class.java)!!
            }
        })
        return user
    }

    override fun createNewAccount() : Boolean {
        var success = false
        if (!uid.isNullOrEmpty()) {
            val user = DatabaseUser(uid, false, ArrayList())
            ref.setValue(user)
                .addOnSuccessListener {
                    Log.d(TAG, "Saved user to Firebase!")
                    success = true
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to save user to Firebase..")
                }
        }
        return success
    }


    companion object{
        private const val TAG = "FIRESTORE_PROVIDER"
    }
}

data class DatabaseUser(
    val uid: String = "",
    var premium: Boolean = false,
    val accounts: ArrayList<LoginInformation> = ArrayList()
)