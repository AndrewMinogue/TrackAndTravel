package assignment.trackandtravel.views.trackandtravel.routemapping

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import assignment.trackandtravel.R
import assignment.trackandtravel.models.RouteModel
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_route.*
import kotlinx.android.synthetic.main.activity_track_list.toolbarAdd
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast


class RouteMapView: BaseView(), AnkoLogger {

    lateinit var presenter: RouteMapPresenter
    var route = RouteModel()
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        super.init(toolbarAdd, true);

        presenter = initPresenter (RouteMapPresenter(this)) as RouteMapPresenter

        chooseImage.setOnClickListener { presenter.doSelectImage() }



        //////////////////////////////////////////////////////////////////
        StopLocation.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                StopLocation.setBackgroundColor(Color.RED)
                presenter.doSetLocation()
            }
        })

        mapView.getMapAsync {
            presenter.doConfigureMap(it)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
        }
        //////////////////////////////////////////////////////////////////


        StopLocation2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                StopLocation2.setBackgroundColor(Color.RED)
                presenter.doSetLocation1()
            }
        })

        StopLocation3.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                StopLocation3.setBackgroundColor(Color.RED)
                presenter.doSetLocation2()
            }
        })

        StopLocation4.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                StopLocation4.setBackgroundColor(Color.RED)
                presenter.doSetLocation3()
            }
        })

        StopLocation5.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                StopLocation5.setBackgroundColor(Color.RED)
                presenter.doSetLocation4()
            }
        })

        StopLocation6.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                StopLocation6.setBackgroundColor(Color.RED)
                presenter.doSetLocation5()
            }
        })

        StopLocation7.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                StopLocation7.setBackgroundColor(Color.RED)
                presenter.doSetLocation6()
            }
        })

        StopLocation8.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                StopLocation8.setBackgroundColor(Color.RED)
                presenter.doSetLocation7()
            }
        })

        StopLocation9.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                StopLocation9.setBackgroundColor(Color.RED)
                presenter.doSetLocation8()
            }
        })

        StopLocation10.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                StopLocation10.setBackgroundColor(Color.RED)
                presenter.doSetLocation9()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
//        mapView3.onDestroy()
//        mapView2.onDestroy()
        mapView.onDestroy()

    }

    override fun onLowMemory() {
        super.onLowMemory()
//        mapView3.onLowMemory()
//        mapView2.onLowMemory()
        mapView.onLowMemory()

    }

    override fun onPause() {
        super.onPause()
//        mapView3.onPause()
//        mapView2.onPause()
        mapView.onPause()



    }

    override fun onResume() {
        super.onResume()
//        mapView3.onResume()
//        mapView2.onResume()
        mapView.onResume()

        presenter.doResartLocationUpdates9()
        presenter.doResartLocationUpdates8()
        presenter.doResartLocationUpdates7()
        presenter.doResartLocationUpdates6()
        presenter.doResartLocationUpdates5()
        presenter.doResartLocationUpdates4()
        presenter.doResartLocationUpdates3()
        presenter.doResartLocationUpdates2()
        presenter.doResartLocationUpdates1()
        presenter.doResartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
//        mapView2.onSaveInstanceState(outState)
//        mapView3.onSaveInstanceState(outState)


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
        this.showLocation(route.stop1)
        this.showLocation1(route.stop2)
        this.showLocation1(route.stop3)
        this.showLocation1(route.stop4)
        this.showLocation1(route.stop5)
        this.showLocation1(route.stop6)
        this.showLocation1(route.stop7)
        this.showLocation1(route.stop8)
        this.showLocation1(route.stop9)
        this.showLocation1(route.stop10)
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