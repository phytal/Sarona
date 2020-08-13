package com.phytal.sarona.data.firebase

import android.util.Log
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.phytal.sarona.data.Account


class FirestoreProviderImpl : FirestoreProvider {
    private val ref = FirebaseDatabase.getInstance().getReference("accounts")

    override fun createAccount(email: String, password: String): Boolean {
        val mAuth = FirebaseAuth.getInstance()
        var success = false
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "createUserWithEmail:success")
                    val user = mAuth.currentUser
                    //updateUI(user)
                    success = true
                } else {
                    // If sign in fails, return failed
                    //Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    success =  false
                    //updateUI(null)
                }
            }
        return success
    }

    override fun saveAccount(account: Account): Boolean {
//        val ref = FirebaseDatabase.getInstance().getReference("accounts")
//        if (ref.child())
        val userAccount = ref.child(account.id)
        var success = false
        val userId = ref.push().key!!
        ref.child(userId).setValue(account).addOnCompleteListener{
            success = true
        }
        return success
    }
    fun getUserData(uId: String) {
        val db = FirebaseDatabase.getInstance()
            ref.child("accounts").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.value
                }

            })
    }

    private fun getUserProfile() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
        }
    }
    private fun getProviderData() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId

                // UID specific to the provider
                val uid = profile.uid

                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
            }
        }
    }

    private fun updateProfile(account: Account) {
        // [START update_profile]
        val user = FirebaseAuth.getInstance().currentUser

        val profileUpdates =
            UserProfileChangeRequest.Builder().setDisplayName(account.fullName).build()

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
    }

    private fun updateEmail() {
        val user = FirebaseAuth.getInstance().currentUser

        user!!.updateEmail("user@example.com")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                }
            }
    }

    private fun updatePassword(password: String) {
        val user = FirebaseAuth.getInstance().currentUser

        user!!.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User password updated.")
                }
            }
    }

    private fun sendEmailVerification() {
        val user = FirebaseAuth.getInstance().currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }

    private fun sendEmailVerificationWithContinueUrl() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!

        val url = "http://www.example.com/verify?uid=" + user.uid
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl(url)
            .setIOSBundleId("com.example.ios")
            // The default for this is populated with the current android package name.
            .setAndroidPackageName("com.example.android", false, null)
            .build()

        user.sendEmailVerification(actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }

        auth.setLanguageCode("fr")
        // To apply the default app language instead of explicitly setting it.
        auth.useAppLanguage()
    }

    private fun sendPasswordReset(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }

    private fun deleteUser() {
        val user = FirebaseAuth.getInstance().currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }
    }

    private fun reauthenticate() {
        val user = FirebaseAuth.getInstance().currentUser!!

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        val credential = EmailAuthProvider
            .getCredential("user@example.com", "password1234")

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
            .addOnCompleteListener { Log.d(TAG, "User re-authenticated.") }
    }

    private fun getGoogleCredentials(googleIdToken: String) {
        // [START auth_google_cred]
        val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
        // [END auth_google_cred]
    }


    private fun getEmailCredentials(email: String, password: String) {
        // [START auth_email_cred]
        val credential = EmailAuthProvider.getCredential(email, password)
        // [END auth_email_cred]
    }


    companion object{
        private const val TAG = "FIREBASE_PROVIDER"
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}