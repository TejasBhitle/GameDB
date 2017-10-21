package com.codeblooded.gamedb.ui.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import com.codeblooded.gamedb.OnSignUpCompleteListener
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.util.AuthHandler
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.Auth
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient






class SignupActivity : AppCompatActivity() {

    lateinit var googleSignInButton: SignInButton
    lateinit var logoutButton: Button
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: Button
    lateinit var signUpButton: Button
    lateinit var emailTextView: TextView
    lateinit var progressDialog: ProgressDialog
    lateinit var loggedInView: LinearLayout
    lateinit var loggedOutView: LinearLayout
    var isLoggedIn: Boolean = false
    val TAG = "SignUpActivity"
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var gso : GoogleSignInOptions
    lateinit var authHandler : AuthHandler

    val RC_SIGN_IN = 999


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
            if (!(email.trim().equals(" ") && password.trim().equals("")))
                authHandler.login(emailEditText.text.toString(), passwordEditText.text.toString())
            else
                Toast.makeText(this@SignupActivity, "Fill all fields", Toast.LENGTH_SHORT).show()
        }

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (!(email.trim().equals(" ") && password.trim().equals("")))
                authHandler.signUp(emailEditText.text.toString(), passwordEditText.text.toString())
            else
                Toast.makeText(this@SignupActivity, "Fill all fields", Toast.LENGTH_SHORT).show()
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


        authHandler = AuthHandler(firebaseAuth,this@SignupActivity,progressDialog)
        authHandler.onSignUpCompleteListener = object : OnSignUpCompleteListener {
            override fun onComplete(isLoggedIn : Boolean) {
                checkLoggedIn(isLoggedIn)
            }
        }
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
    }

    override fun onResume() {
        super.onResume()
        checkLoggedIn(isLoggedIn)
    }

    fun checkLoggedIn(isLoggedIn: Boolean){
        if (isLoggedIn) {
            loggedInView.visibility = View.VISIBLE
            loggedOutView.visibility = View.GONE
            val user = FirebaseAuth.getInstance().currentUser
            emailTextView.text = user?.email
        } else {
            loggedInView.visibility = View.GONE
            loggedOutView.visibility = View.VISIBLE
        }
    }

    private fun googleSignIn() {
        // Configure Google Sign In
        var mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, GoogleApiClient.OnConnectionFailedListener{})
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                if(account != null)
                    authHandler.firebaseAuthWithGoogle(account)
            } else {
                Log.e(TAG,"Sign in failed")

            }
        }
    }


}
