package assignment.trackandtravel.views.trackandtravel.passenger.route

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import assignment.trackandtravel.R
import assignment.trackandtravel.models.RouteModel
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_route.*
import kotlinx.android.synthetic.main.activity_track_list.toolbarAdd
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast


class PassengerRouteView: BaseView(), AnkoLogger {

    lateinit var presenter: PassengerRoutePresenter
    var route = RouteModel()
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__driver_route)
        super.init(toolbarAdd, true);

        presenter = initPresenter (PassengerRoutePresenter(this)) as PassengerRoutePresenter

        chooseImage.setOnClickListener { presenter.doSelectImage() }
        StopLocation.setOnClickListener { presenter.doSetLocation() }

        mapView.getMapAsync {
            presenter.doConfigureMap(it)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        presenter.doResartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_route, menu)
        if (presenter.edit) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun showRoute(route: RouteModel) {
        BusNumber.setText(route.busnumber)
        firststop.setText(route.busstopstart)
        laststop.setText(route.busstopend)
        route.favourite = false


//        if(hillfort.favourite == true){
//            favourite.setChecked(true)
//            hillfort.favourite = true
//        }else if(hillfort.favourite == false){
//            hillfort.favourite = false
//            favourite.setChecked(false)
//        }

        Glide.with(this).load(route.image).into(BusImage)


        if (route.image != null) {
            chooseImage.setText(R.string.change_route_image)
        }
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
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
            R.id.item_add1 -> {
                if (BusNumber.text.toString().isEmpty()) {
                    toast(R.string.enter_route_title)
                } else {
                    presenter.doAddOrSave(BusNumber.text.toString(), firststop.text.toString(), laststop.text.toString(),findFavouriteChecked())
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