package com.codeblooded.gamedb.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.EditText

import com.codeblooded.gamedb.R
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

import android.util.Log
import com.google.firebase.auth.FirebaseUser


class SignupActivity : AppCompatActivity() {

    lateinit var googleSignInButton : SignInButton
    lateinit var emailEditText : EditText
    lateinit var passwordEditText : EditText
    lateinit var loginButton : Button
    lateinit var signUpButton : Button

    val TAG = "SignUpActivity"

    lateinit var firebaseAuth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = FirebaseAuth.getInstance().currentUser
        updateUI(currentUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        googleSignInButton = findViewById(R.id.googleSignInButton)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        signUpButton = findViewById(R.id.signUpButton)


        loginButton.setOnClickListener {
            login(emailEditText.text.toString(),passwordEditText.text.toString())
        }

        signUpButton.setOnClickListener {
            signUp(emailEditText.text.toString(),passwordEditText.text.toString())
        }

        googleSignInButton.setOnClickListener {
            googleSignIn()
        }

        firebaseAuth = FirebaseAuth.getInstance()


    }

    fun login(email:String , password:String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,{ task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(this@SignupActivity.TAG, "signInWithEmail:success")
                        val user = firebaseAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(this@SignupActivity.TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@SignupActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // ...
                })

    }

    fun signUp(email:String , password:String){

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(this@SignupActivity.TAG, "createUserWithEmail:success")
                        val user = firebaseAuth.currentUser
                        Toast.makeText(this@SignupActivity, "Success.\nNow Login", Toast.LENGTH_SHORT).show()
                        //updateUI(user)
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.e(this@SignupActivity.TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this@SignupActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                })

    }

    fun googleSignIn(){

    }

    fun updateUI(user : FirebaseUser?){
        if(user != null){

        }
        else{

        }
    }



}
