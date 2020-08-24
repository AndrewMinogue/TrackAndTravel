package assignment.trackandtravel.views.trackandtravel.passenger.route

import android.annotation.SuppressLint
import android.content.Intent
import assignment.trackandtravel.helpers.checkLocationPermissions
import assignment.trackandtravel.helpers.createDefaultLocationRequest
import assignment.trackandtravel.helpers.isPermissionGranted
import assignment.trackandtravel.helpers.showImagePicker
import assignment.trackandtravel.models.Location
import assignment.trackandtravel.models.RouteModel
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


class PassengerRoutePresenter(view: BaseView) : BasePresenter(view) {

    var map: GoogleMap? = null
    var route = RouteModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false;
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()

    init {
        if (view.intent.hasExtra("route_edit")) {
            edit = true
            route = view.intent.extras?.getParcelable<RouteModel>("route_edit")!!
            view.showRoute(route)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
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