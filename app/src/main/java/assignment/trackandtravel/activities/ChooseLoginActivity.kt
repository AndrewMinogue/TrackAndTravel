package assignment.trackandtravel.activities

import assignment.trackandtravel.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import assignment.trackandtravel.main.MainApp
import assignment.trackandtravel.views.trackandtravel.loginadmin.LoginView
import assignment.trackandtravel.views.trackandtravel.loginadmin.LoginViewDriver
import assignment.trackandtravel.views.trackandtravel.loginadmin.LoginViewPassenger
import kotlinx.android.synthetic.main.choose_login.*
import org.jetbrains.anko.startActivityForResult


class ChooseLoginActivity : Activity() {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_login)
        app = application as MainApp


        Admin1.setOnClickListener{
            startActivityForResult<LoginView>(0)
        }

        BusDriver.setOnClickListener{
            startActivityForResult<LoginViewDriver>(0)
        }

        passenger.setOnClickListener{
            startActivityForResult<LoginViewPassenger>(0)
        }

    }


}