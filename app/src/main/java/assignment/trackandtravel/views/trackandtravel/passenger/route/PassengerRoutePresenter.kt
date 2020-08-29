package assignment.trackandtravel.views.trackandtravel.passenger.route

import android.annotation.SuppressLint
import android.content.Intent
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


class PassengerRoutePresenter(view: BaseView) : BasePresenter(view) {

    var map: GoogleMap? = null
    var route = RouteModel()
    var defaultLocation = Location(52.245696, -7.139102, 11f)
    var edit = false;
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()
    var fireStore: RouteFireStore? = null
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 5000

    init {
        if (app.routes is RouteFireStore) {
            fireStore = app.routes as RouteFireStore
        }
    }

    init {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            fireStore!!.fetchRoutes{
        if (view.intent.hasExtra("route_edit")) {
            edit = true
            route = view.intent.extras?.getParcelable<RouteModel>("route_edit")!!
            view.showRoute(route)
        }
            }
        }.also { runnable = it }, delay.toLong())
    }



    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(route.location)
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(Location(it.latitude, it.longitude))
        }
    }

    fun locationUpdate(location : Location) {
        route.location = location
        route.location.zoom = 11f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)

        val options = MarkerOptions().position(LatLng(route.location.lat, route.location.lng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.abus))
        map?.addMarker(options)

        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.location.lat, route.location.lng), route.location.zoom))
//
//        val options1 = MarkerOptions().position(LatLng(route.stop1.lat, route.stop1.lng))
//        map?.addMarker(options1)
//
//        val options2 = MarkerOptions().position(LatLng(route.stop2.lat, route.stop2.lng))
//        map?.addMarker(options2)
//
//        val options3 = MarkerOptions().position(LatLng(route.stop3.lat, route.stop3.lng))
//        map?.addMarker(options3)

        view?.showLocation(route.location)
//        view?.showLocation(route.stop1)
//        view?.showLocation(route.stop2)
//        view?.showLocation(route.stop3)
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

    fun doDelete() {
        doAsync {
            app.routes.delete(route)
            uiThread {
                view?.finish()
            }
        }
    }

    fun doSetLocation() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(route.location.lat, route.location.lng, route.location.zoom))
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        view?.navigateTo(VIEW.LOGIN)
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                route.image = data.data.toString()
                view?.showRoute(route)
            }
        }
    }
}