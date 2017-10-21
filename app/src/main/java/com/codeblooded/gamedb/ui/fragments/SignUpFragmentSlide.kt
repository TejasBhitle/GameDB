package com.codeblooded.gamedb.ui.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.util.AuthHandler
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.heinrichreimersoftware.materialintro.app.SlideFragment

/**
 * Created by tejas on 10/21/17.
 */
class SignUpFragmentSlide : SlideFragment() {

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

    lateinit var authHandler: AuthHandler

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_slide_signup,container,false)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(activity)
        authHandler = AuthHandler(
                firebaseAuth = firebaseAuth,
                activity = activity,
                progressDialog = progressDialog
        )

        return view
    }




}