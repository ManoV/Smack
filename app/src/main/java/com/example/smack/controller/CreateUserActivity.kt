package com.example.smack.controller

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.smack.R.layout
import com.example.smack.services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.createAvatarImageView
import kotlinx.android.synthetic.main.activity_create_user.createEmailTxt
import kotlinx.android.synthetic.main.activity_create_user.createPasswordTxt
import java.util.Random

class CreateUserActivity : AppCompatActivity() {

    var createAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_create_user)
    }

    fun createUserBtnClicked(view: View) {
        val email = createEmailTxt.text.toString()
        val password = createPasswordTxt.text.toString()
        AuthService.registerUser(this, email, password) { registerSuccess ->
            println("Registration status: $registerSuccess")
            if (registerSuccess) {
                AuthService.loginUser(this, email, password) { loginSuccess ->
                    if (loginSuccess) {
                        println(" Login User: ${AuthService.userEmail}")
                        println(" Login Token: ${AuthService.authToken}")
                    }
                }
            }
        }
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
