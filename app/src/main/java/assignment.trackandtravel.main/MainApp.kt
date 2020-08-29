package assignment.trackandtravel.main

import android.app.Application
import assignment.trackandtravel.models.AdminStore
import assignment.trackandtravel.models.RouteStore
import assignment.trackandtravel.models.UserStore
import assignment.trackandtravel.models.firebase.RouteFireStore

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    lateinit var routes: RouteStore
    lateinit var users: UserStore
    lateinit var admins: AdminStore

    override fun onCreate() {
        super.onCreate()
        routes = RouteFireStore(applicationContext)
        users = RouteFireStore(applicationContext)
        admins = RouteFireStore(applicationContext)

        info("Application trackandtravel started")
    }
}