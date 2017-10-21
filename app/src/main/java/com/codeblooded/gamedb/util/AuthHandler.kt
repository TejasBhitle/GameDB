package com.codeblooded.gamedb.util

import android.app.Activity
import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast
import com.codeblooded.gamedb.OnSignUpCompleteListener
import com.codeblooded.gamedb.R
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnCompleteListener


/**
 * Created by tejas on 10/21/17.
 */
class AuthHandler(val firebaseAuth: FirebaseAuth,
                  val activity: Activity,
                  val progressDialog: ProgressDialog) {


    val TAG = "AuthHandler"
    var isLoggedIn : Boolean = false
    lateinit var onSignUpCompleteListener: OnSignUpCompleteListener

    init {

    }

    fun login(email: String, password: String) {
        progressDialog.setMessage("Logging in")
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, { task ->
                    if (task.isSuccessful) {
                        progressDialog.cancel()
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(TAG, "signInWithEmail:success")
                        isLoggedIn = true

                        onSignUpCompleteListener.onComplete(isLoggedIn)
                    } else {
                        progressDialog.cancel()
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        isLoggedIn = false
                    }
                })

    }

    fun signUp(email: String, password: String) {
        progressDialog.setMessage("Signing in")
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        progressDialog.cancel()
                        Log.e(TAG, "createUserWithEmail:success")
                        Toast.makeText(activity, "Success.\nNow Login", Toast.LENGTH_SHORT).show()
                        login(email, password)

                    } else {
                        progressDialog.cancel()
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()

                    }
                })


    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.e(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success")
                            val user = firebaseAuth.currentUser
                            isLoggedIn = true
                            onSignUpCompleteListener.onComplete(isLoggedIn)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithCredential:failure", task.exception)
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()

                        }

                    }
                })
    }
}