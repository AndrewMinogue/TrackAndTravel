package assignment.trackandtravel.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import assignment.trackandtravel.R
import assignment.trackandtravel.main.MainApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.settings.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class SettingsActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        app = application as MainApp
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        info("Hillfort Activity started..")


        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            emailT.text = "${user.email}"
        }

    }
}