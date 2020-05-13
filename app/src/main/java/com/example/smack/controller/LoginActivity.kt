package com.example.smack.controller

import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.smack.R.layout
import com.example.smack.services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.createAvatarColorBtn
import kotlinx.android.synthetic.main.activity_create_user.createAvatarImageView
import kotlinx.android.synthetic.main.activity_create_user.createProgressBar
import kotlinx.android.synthetic.main.activity_create_user.createUserBtn
import kotlinx.android.synthetic.main.activity_login.loginEmailTxt
import kotlinx.android.synthetic.main.activity_login.loginLoginBtn
import kotlinx.android.synthetic.main.activity_login.loginPasswordTxt
import kotlinx.android.synthetic.main.activity_login.loginProgressBar
import kotlinx.android.synthetic.main.activity_login.loginRegisterBtn

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login)
        loginProgressBar.visibility = View.INVISIBLE
    }

    fun loginBtnClicked(view: View) {
        enableProgressBar(true)
        val email = loginEmailTxt.text.toString()
        val password = loginPasswordTxt.text.toString()
        hideKeyboard()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(this, email, password) { loginResponse ->
                loginProgressBar.visibility = View.INVISIBLE
                if (loginResponse) {
                    AuthService.findUserByEmail(this) { findUserResponse ->
                        if (findUserResponse) {
                            finish()
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_LONG).show()
            enableProgressBar(false)
        }

    }

    fun registerBtnClicked(view: View) {
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_LONG).show()
        enableProgressBar(false)
    }

    fun enableProgressBar(enable: Boolean) {
        if (enable) {
            loginProgressBar.visibility = View.VISIBLE
        } else {
            loginProgressBar.visibility = View.INVISIBLE
        }
        loginRegisterBtn.isEnabled = !enable
        loginLoginBtn.isEnabled = !enable

    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}
