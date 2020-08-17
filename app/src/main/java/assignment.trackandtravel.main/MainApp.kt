package assignment.trackandtravel.main

import android.app.Application
import assignment.trackandtravel.models.RouteStore
import assignment.trackandtravel.models.firebase.RouteFireStore

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    lateinit var routes: RouteStore

    override fun onCreate() {
        super.onCreate()
        routes = RouteFireStore(applicationContext)
        info("Application trackandtravel started")
    }
}