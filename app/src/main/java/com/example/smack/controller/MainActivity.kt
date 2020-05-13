package com.example.smack.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smack.R
import com.example.smack.R.id
import com.example.smack.R.layout
import com.example.smack.R.string
import com.example.smack.services.AuthService
import com.example.smack.services.UserDataService
import com.example.smack.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import kotlinx.android.synthetic.main.nav_header_main.loginBtnNavHeader
import kotlinx.android.synthetic.main.nav_header_main.userEmailNavHeader
import kotlinx.android.synthetic.main.nav_header_main.userImageNavHeader
import kotlinx.android.synthetic.main.nav_header_main.userNameNavHeader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        val toolbar: Toolbar = findViewById(id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, string.navigation_drawer_open,
            string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGE)
        )
    }

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (AuthService.isLoggedIn) {
                println("UserDataService.name : " + UserDataService.name)
                println("UserDataService.email : " + UserDataService.email)

                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email

                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(resourceId)
                loginBtnNavHeader.text = "Logout"
                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginBtnNavClicked(view: View) {
        if (AuthService.isLoggedIn) {
            UserDataService.logout()
            userNameNavHeader.text = "Login"
            userEmailNavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginBtnNavHeader.text = "Login"

        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun addChannelClicked(view: View) {
        if (AuthService.isLoggedIn) {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)

            builder.setView(dialogView)
                .setPositiveButton("Add") { dialogInterface, i ->

                    val channelNameTextField = dialogView.findViewById<EditText>(R.id.addChannelNameTxt)
                    val channelDescTextField = dialogView.findViewById<EditText>(R.id.addChannelDescTxt)

                    val channelName = channelNameTextField.text.toString()
                    val channelDesc = channelDescTextField.text.toString()
                }
                .setNegativeButton("Cancel") { dialogInterface, i ->

                }
                .show()
        }

    }

    fun sendMessageBtnClicked(view: View) {

    }
}
