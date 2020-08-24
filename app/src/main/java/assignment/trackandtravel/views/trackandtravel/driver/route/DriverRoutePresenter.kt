package assignment.trackandtravel.views.trackandtravel.driver.route

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class DriverRoutePresenter(view: BaseView) : BasePresenter(view) {

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

    init {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
        if (view.intent.hasExtra("route_edit")) {
            edit = true
            route = view.intent.extras?.getParcelable<RouteModel>("route_edit")!!
            view.showRoute(route)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
        }.also { runnable = it }, delay.toLong())
    }

    init {
        if (app.routes is RouteFireStore) {
            fireStore = app.routes as RouteFireStore
        }
    }



    fun run() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())

        }.also { runnable = it }, delay.toLong())
    }

    fun refresh()
    {
        fireStore!!.fetchRoutes{
            handler.postDelayed(Runnable {
                handler.postDelayed(runnable!!, delay.toLong())
            }.also { runnable = it }, delay.toLong())
        }
    }
    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(Location(it.latitude, it.longitude))
        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation)
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
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.location.lat, route.location.lng), route.location.zoom))
        view?.showLocation(route.location)
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

    fun doSelectImage() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    fun doSetLocation() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(route.location.lat, route.location.lng, route.location.zoom))
    }


    fun doRouteMap() {
        view?.navigateTo(VIEW.ROUTE)
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
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                route.location = location
                locationUpdate(location)
            }
        }
    }
}