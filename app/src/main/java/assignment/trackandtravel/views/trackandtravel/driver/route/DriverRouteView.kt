package assignment.trackandtravel.views.trackandtravel.driver.route

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import assignment.trackandtravel.R
import assignment.trackandtravel.models.RouteModel
import assignment.trackandtravel.models.firebase.RouteFireStore
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity__driver_route.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast



class DriverRouteView: BaseView(), AnkoLogger {

    lateinit var presenter: DriverRoutePresenter
    var route = RouteModel()
    lateinit var map: GoogleMap
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 4000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__driver_route)
        super.init(toolbarAdd1, true);


        presenter = initPresenter (DriverRoutePresenter(this)) as DriverRoutePresenter

        StopLocation1.setOnClickListener { presenter.doSetLocation() }

        mapView1.getMapAsync {
            presenter.doConfigureMap(it)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }

        mapView1.onCreate(savedInstanceState)
        mapView1.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
        }

        presenter.refresh()

        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
        if (BusNumber1.text.toString().isEmpty()) {
            toast(R.string.enter_route_title)
        } else {
            presenter.doAddOrSave(BusNumber1.text.toString(), firststop1.text.toString(), laststop1.text.toString(),findFavouriteChecked())
        }
        }.also { runnable = it }, delay.toLong())

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_driver, menu)
        if (presenter.edit) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun showRoute(route: RouteModel) {
        BusNumber1.setText(route.busnumber)
        firststop1.setText(route.busstopstart)
        laststop1.setText(route.busstopend)
        route.favourite = false


//        if(route.favourite == true){
//            favourite.setChecked(true)
//            route.favourite = true
//        }else if(route.favourite == false){
//            route.favourite = false
//            favourite.setChecked(false)
//        }

        Glide.with(this).load(route.image).into(BusImage1)

        this.showLocation(route.location)
    }

    fun findFavouriteChecked(): Boolean{
        var favourite1 = false

//        if(favourite.isChecked){
//            favourite1 = true
//        }
//        if(favourite.isChecked == false){
//            favourite1 = false
//        }
        return favourite1
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add1 -> {
                if (BusNumber1.text.toString().isEmpty()) {
                    toast(R.string.enter_route_title)
                } else {
                    presenter.doAddOrSave(BusNumber1.text.toString(), firststop1.text.toString(), laststop1.text.toString(),findFavouriteChecked())
                }
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