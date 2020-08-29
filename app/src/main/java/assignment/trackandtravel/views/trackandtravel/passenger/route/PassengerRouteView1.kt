package assignment.trackandtravel.views.trackandtravel.passenger.route

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import assignment.trackandtravel.R
import assignment.trackandtravel.main.MainApp
import assignment.trackandtravel.models.RouteModel
import assignment.trackandtravel.models.firebase.RouteFireStore
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import assignment.trackandtravel.views.trackandtravel.base.VIEW
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity__driver_route.*
import kotlinx.android.synthetic.main.activity_route.*
import kotlinx.android.synthetic.main.activity_track_list.toolbarAdd
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast


class PassengerRouteView1: BaseView(), AnkoLogger {

    lateinit var presenter: PassengerRoutePresenter1
    var route = RouteModel()
    lateinit var map: GoogleMap
    var fireStore: RouteFireStore? = null
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 5000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__driver_route)
        super.init(toolbarAdd1, true);

        presenter = initPresenter (PassengerRoutePresenter1(this)) as PassengerRoutePresenter1



        presenter.refresh()


        mapView1.getMapAsync {
            presenter.doConfigureMap(it)
        }

        mapView1.onCreate(savedInstanceState)
        mapView1.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
        }

        presenter.loadRoutes()

    }


    override fun onDestroy() {
        super.onDestroy()
        mapView1.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView1.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView1.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView1.onResume()
        presenter.doResartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView1.onSaveInstanceState(outState)
    }


    override fun showRoute(route: RouteModel) {
        BusNumber1.setText(route.busnumber)
        firststop1.setText(route.busstopstart)
        laststop1.setText(route.busstopend)
        route.favourite = false

        Glide.with(this).load(route.image).into(BusImage1)

        this.showLocation(route.location)
        this.showLocation(route.stop1)
        this.showLocation(route.stop2)
        this.showLocation(route.stop3)
        this.showLocation(route.stop4)
        this.showLocation(route.stop5)
        this.showLocation(route.stop6)
        this.showLocation(route.stop7)
        this.showLocation(route.stop8)
        this.showLocation(route.stop9)
        this.showLocation(route.stop10)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        if (presenter.edit) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }
}