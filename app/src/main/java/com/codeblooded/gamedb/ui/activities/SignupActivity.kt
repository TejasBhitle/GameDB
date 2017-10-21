package com.codeblooded.gamedb.ui.activities

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar

import com.codeblooded.gamedb.R
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth

import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseUser


class SignupActivity : AppCompatActivity() {

    lateinit var googleSignInButton : SignInButton
    lateinit var logoutButton : Button
    lateinit var emailEditText : EditText
    lateinit var passwordEditText : EditText
    lateinit var loginButton : Button
    lateinit var signUpButton : Button
    lateinit var emailTextView : TextView
    lateinit var progressDialog : ProgressDialog
    lateinit var loggedInView : LinearLayout
    lateinit var loggedOutView : LinearLayout
    var isLoggedIn : Boolean = false
    val TAG = "SignUpActivity"
    lateinit var firebaseAuth: FirebaseAuth


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = FirebaseAuth.getInstance().currentUser
        isLoggedIn = (currentUser != null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setTitle(R.string.account)
        progressDialog = ProgressDialog(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        googleSignInButton = findViewById(R.id.googleSignInButton)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        signUpButton = findViewById(R.id.signUpButton)
        logoutButton = findViewById(R.id.logoutButton)
        loggedInView = findViewById(R.id.loggedInView)
        loggedOutView = findViewById(R.id.loggedOutView)
        emailTextView = findViewById(R.id.emailTextView)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if(!(email.trim().equals(" ") && password.trim().equals("")))
                login(emailEditText.text.toString(),passwordEditText.text.toString())
            else
                Toast.makeText(this@SignupActivity,"Fill all fields",Toast.LENGTH_SHORT).show()
        }

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if(!(email.trim().equals(" ") && password.trim().equals("")))
                signUp(emailEditText.text.toString(),passwordEditText.text.toString())
            else
                Toast.makeText(this@SignupActivity,"Fill all fields",Toast.LENGTH_SHORT).show()
        }

        googleSignInButton.setOnClickListener {
            googleSignIn()
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            isLoggedIn = false
            onResume()

        }

        firebaseAuth = FirebaseAuth.getInstance()

    }

    override fun onResume() {
        super.onResume()
        if(isLoggedIn){
            loggedInView.visibility = View.VISIBLE
            loggedOutView.visibility = View.GONE
            val user = FirebaseAuth.getInstance().currentUser
            emailTextView.text = user?.email
        }
        else{
            loggedInView.visibility = View.GONE
            loggedOutView.visibility = View.VISIBLE
        }
    }

    fun login(email:String , password:String){
        progressDialog.setMessage("Logging in")
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,{ task ->
                    if (task.isSuccessful) {
                        progressDialog.cancel()
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(this@SignupActivity.TAG, "signInWithEmail:success")
                        isLoggedIn =true
                        onResume()
                    } else {
                        progressDialog.cancel()
                        // If sign in fails, display a message to the user.
                        Log.e(this@SignupActivity.TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@SignupActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        isLoggedIn = false
                        onResume()
                    }

                    // ...
                })

    }

    fun signUp(email:String , password:String){
        progressDialog.setMessage("Signing in")
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        progressDialog.cancel()
                        Log.e(this@SignupActivity.TAG, "createUserWithEmail:success")
                        val user = firebaseAuth.currentUser
                        Toast.makeText(this@SignupActivity, "Success.\nNow Login", Toast.LENGTH_SHORT).show()
                        login(email,password)
                        //updateUI(user)
                    }
                    else {
                        progressDialog.cancel()
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

    }



}
