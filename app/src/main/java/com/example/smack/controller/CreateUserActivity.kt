package com.example.smack.controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smack.R.layout
import com.example.smack.services.AuthService
import com.example.smack.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.createAvatarColorBtn
import kotlinx.android.synthetic.main.activity_create_user.createAvatarImageView
import kotlinx.android.synthetic.main.activity_create_user.createEmailTxt
import kotlinx.android.synthetic.main.activity_create_user.createPasswordTxt
import kotlinx.android.synthetic.main.activity_create_user.createProgressBar
import kotlinx.android.synthetic.main.activity_create_user.createUserBtn
import kotlinx.android.synthetic.main.activity_create_user.createUserNameTxt
import java.util.Random

class CreateUserActivity : AppCompatActivity() {

    var createAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_create_user)
        createProgressBar.visibility = View.INVISIBLE
    }

    fun createUserBtnClicked(view: View) {
        enableProgressBar(true)
        val userName = createUserNameTxt.text.toString()
        val email = createEmailTxt.text.toString()
        val password = createPasswordTxt.text.toString()

        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.registerUser(this, email, password) { registerSuccess ->
                println("Registration status: $registerSuccess")
                if (registerSuccess) {
                    AuthService.loginUser(this, email, password) { loginSuccess ->
                        println("Login Status: $loginSuccess")
                        if (loginSuccess) {
                            AuthService.createUser(this, userName, email, createAvatar, avatarColor) { addUserSuccess ->
                                if (addUserSuccess) {
                                    println("Add User status : $addUserSuccess")

                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
                                    enableProgressBar(false)
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
                    errorToast()
                }
            }
        } else {
            inputValidation()
        }
    }

    fun inputValidation() {
        Toast.makeText(this, "Enter all inputs!", Toast.LENGTH_LONG).show()
        enableProgressBar(false)
    }
    fun errorToast() {
        Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_LONG).show()
        enableProgressBar(false)
    }

    fun enableProgressBar(enable: Boolean) {
        if (enable) {
            createProgressBar.visibility = View.VISIBLE
        } else {
            createProgressBar.visibility = View.INVISIBLE
        }
        createUserBtn.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        createAvatarColorBtn.isEnabled = !enable
    }

    fun generateColorBtnClicked(view: View) {
        val random = Random()
        val colorR = random.nextInt(255)
        val colorG = random.nextInt(255)
        val colorB = random.nextInt(255)
        createAvatarImageView.setBackgroundColor(Color.rgb(colorR, colorG, colorB))
        val savedR = colorR.toDouble() / 255
        val savedG = colorG.toDouble() / 255
        val savedB = colorB.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"
    }

    fun createAvatarImageClicked(view: View) {
        val random = Random()
        val avatarColor = random.nextInt(2)
        val avatarNumber = random.nextInt(28)
        if (avatarColor == 0) {
            createAvatar = "light$avatarNumber"
        } else {
            createAvatar = "dark$avatarNumber"
        }
        val resourceId = resources.getIdentifier(createAvatar, "drawable", packageName)
        createAvatarImageView.setImageResource(resourceId)
    }
}
