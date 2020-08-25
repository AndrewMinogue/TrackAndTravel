package assignment.trackandtravel.views.trackandtravel.base

import android.content.Intent

import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import assignment.trackandtravel.models.Location
import assignment.trackandtravel.models.RouteModel
import assignment.trackandtravel.views.trackandtravel.adminhomepage.AdminHomeView
import assignment.trackandtravel.views.trackandtravel.driver.DriverListView
import assignment.trackandtravel.views.trackandtravel.driver.route.DriverRouteView
import assignment.trackandtravel.views.trackandtravel.editlocation.EditLocationView
import assignment.trackandtravel.views.trackandtravel.trackandtravellist.MainDriverView
import assignment.trackandtravel.views.trackandtravel.loginadmin.LoginViewPassenger
import assignment.trackandtravel.views.trackandtravel.passenger.PassengerListView
import assignment.trackandtravel.views.trackandtravel.passenger.PassengerListView1
import assignment.trackandtravel.views.trackandtravel.passenger.route.PassengerRouteView
import assignment.trackandtravel.views.trackandtravel.routemapping.RouteMapView
import assignment.trackandtravel.views.trackandtravel.trackandtravellist.MainListView
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger


val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2


enum class VIEW {
    ROUTE, MAPS, LIST, LOGIN, DRIVERLIST, DRIVER,LOCATION,LIST1,ADMIN1,DRIVERROUTE,PASSENGERROUTE
}

open abstract class BaseView : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, MainDriverView::class.java)
        when (view) {
            VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.ROUTE -> intent = Intent(this,RouteMapView::class.java )
            VIEW.DRIVERROUTE -> intent = Intent(this, DriverRouteView::class.java )
            VIEW.LIST -> intent = Intent(this, PassengerListView1::class.java)
            VIEW.DRIVERLIST -> intent = Intent(this, DriverListView::class.java)
            VIEW.LOGIN -> intent = Intent(this, LoginViewPassenger::class.java)
            VIEW.DRIVER -> intent = Intent(this, MainDriverView::class.java)
            VIEW.LIST1 -> intent = Intent(this, MainListView::class.java)
            VIEW.ADMIN1 -> intent = Intent(this, AdminHomeView::class.java )
            VIEW.PASSENGERROUTE -> intent = Intent(this, PassengerRouteView::class.java )

        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar, upEnabled: Boolean) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            toolbar.title = "${title}: ${user.email}"
        }
    }


        override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showRoute(route: RouteModel) {}
    open fun showProgress() {}
    open fun hideProgress() {}
    open fun showLocation(location : Location) {}
    open fun showRoutes(route: List<RouteModel>) {}
}