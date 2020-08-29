package assignment.trackandtravel.views.trackandtravel.passenger.route

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import android.os.Handler
import assignment.trackandtravel.R
import assignment.trackandtravel.helpers.checkLocationPermissions
import assignment.trackandtravel.helpers.createDefaultLocationRequest
import assignment.trackandtravel.helpers.isPermissionGranted
import assignment.trackandtravel.helpers.showImagePicker
import assignment.trackandtravel.models.Location
import assignment.trackandtravel.models.RouteModel
import assignment.trackandtravel.models.firebase.RouteFireStore
import assignment.trackandtravel.views.trackandtravel.base.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class PassengerRoutePresenter1(view: BaseView) : BasePresenter(view) {

    var map: GoogleMap? = null
    var route = RouteModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false;
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()
    var fireStore: RouteFireStore? = null
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 5000
    private var currentRoutes: List<RouteModel> = arrayListOf()

    init {
        if (app.routes is RouteFireStore) {
            fireStore = app.routes as RouteFireStore
        }
    }

    init {
        if (view.intent.hasExtra("route_edit")) {
            edit = true
            route = view.intent.extras?.getParcelable<RouteModel>("route_edit")!!
            val timer = object: CountDownTimer(5000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }
                override fun onFinish() {
                    fireStore!!.fetchRoutes {
                        view.showRoute(route)
                        locationUpdate(route.location)
                        start()
                    }
                }
            }
            timer.start()
        }
    }




    fun refresh()
    {
        fireStore!!.fetchRoutes{
            handler.postDelayed(Runnable {
                handler.postDelayed(runnable!!, delay.toLong())
            }.also { runnable = it }, delay.toLong())
        }
    }

    fun loadRoutes() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
        fireStore!!.fetchRoutes {
            doAsync {
                val routes = app.routes.findALL()
                uiThread {
                    currentRoutes = routes
                }
            }
        }
        }.also { runnable = it }, delay.toLong())
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
                var locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        if (locationResult != null && locationResult.locations != null) {
                            val l = locationResult.locations.last()
                            locationUpdate(Location(l.latitude, l.longitude))
                        }
                    }
                }
                if (!edit) {
                    locationService.requestLocationUpdates(locationRequest, locationCallback, null)
                }
    }



    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(route.location)
    }

    fun locationUpdate(location : Location) {
        route.location = location
        route.location.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().position(LatLng(route.location.lat, route.location.lng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.abus))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.location.lat, route.location.lng), route.location.zoom))

        val options1 = MarkerOptions().position(LatLng(route.stop1.lat, route.stop1.lng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop))
        map?.addMarker(options1)

        val options2 = MarkerOptions().position(LatLng(route.stop2.lat, route.stop2.lng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop))
        map?.addMarker(options2)

        val options3 = MarkerOptions().position(LatLng(route.stop3.lat, route.stop3.lng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop))
        map?.addMarker(options3)

        val options4 = MarkerOptions().position(LatLng(route.stop4.lat, route.stop4.lng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop))
        map?.addMarker(options4)

        val options5 = MarkerOptions().position(LatLng(route.stop5.lat, route.stop5.lng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop))
        map?.addMarker(options5)

        val options6 = MarkerOptions().position(LatLng(route.stop6.lat, route.stop6.lng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop))
        map?.addMarker(options6)

        val options7 = MarkerOptions().position(LatLng(route.stop7.lat, route.stop7.lng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop))
        map?.addMarker(options7)

        val options8 = MarkerOptions().position(LatLng(route.stop8.lat, route.stop8.lng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop))
        map?.addMarker(options8)

        val options9 = MarkerOptions().position(LatLng(route.stop9.lat, route.stop9.lng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop))
        map?.addMarker(options9)

        val options10 = MarkerOptions().position(LatLng(route.stop10.lat, route.stop10.lng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop))
        map?.addMarker(options10)

        view?.showLocation(route.location)
        view?.showLocation(route.stop1)
        view?.showLocation(route.stop2)
        view?.showLocation(route.stop3)
        view?.showLocation(route.stop4)
        view?.showLocation(route.stop5)
        view?.showLocation(route.stop6)
        view?.showLocation(route.stop7)
        view?.showLocation(route.stop8)
        view?.showLocation(route.stop9)
        view?.showLocation(route.stop10)
    }


//    @SuppressLint("MissingPermission")
//    fun doResartLocationUpdates() {
//        handler.postDelayed(Runnable {
//            handler.postDelayed(runnable!!, delay.toLong())
//            fireStore!!.fetchRoutes{
//        locationUpdate(route.location)
//        }
//    }.also { runnable = it }, delay.toLong())
//    }

    fun doAddOrSave(busnumber: String, busstopstart: String, busstopend : String, favourite: Boolean) {
        route.busnumber = busnumber
        route.busstopstart = busstopstart
        route.busstopend = busstopend
        route.favourite = favourite


        doAsync {
            if (edit) {
                app.routes.update(route)
            } else {
                app.routes.create(route)
            }
            uiThread {
                view?.finish()
            }
        }
    }

    fun doCancel() {
        view?.finish()
    }


    fun doSetLocation() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(route.location.lat, route.location.lng, route.location.zoom))
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                route.image = data.data.toString()
                view?.showRoute(route)
            }
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                route.location = location
                locationUpdate(location)
            }
        }
    }
}