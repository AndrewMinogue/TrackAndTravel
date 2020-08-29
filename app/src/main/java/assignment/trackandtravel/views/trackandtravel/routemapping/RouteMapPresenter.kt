package assignment.trackandtravel.views.trackandtravel.routemapping

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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class RouteMapPresenter(view: BaseView) : BasePresenter(view) {

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
                doSetCurrentLocation1()
                doSetCurrentLocation2()
                doSetCurrentLocation3()
                doSetCurrentLocation4()
                doSetCurrentLocation5()
                doSetCurrentLocation6()
                doSetCurrentLocation7()
                doSetCurrentLocation8()
                doSetCurrentLocation9()
            }
        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
            doSetCurrentLocation1()
            doSetCurrentLocation2()
            doSetCurrentLocation3()
            doSetCurrentLocation4()
            doSetCurrentLocation5()
            doSetCurrentLocation6()
            doSetCurrentLocation7()
            doSetCurrentLocation8()
            doSetCurrentLocation9()
        } else {
            locationUpdate(defaultLocation)
            locationUpdate1(defaultLocation)
            locationUpdate2(defaultLocation)
            locationUpdate3(defaultLocation)
            locationUpdate4(defaultLocation)
            locationUpdate5(defaultLocation)
            locationUpdate6(defaultLocation)
            locationUpdate7(defaultLocation)
            locationUpdate8(defaultLocation)
            locationUpdate9(defaultLocation)
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


    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        view?.navigateTo(VIEW.LOGIN)
    }

    ///////////////////////////////////////////////////////////////
    /////// METHODS FOR FIRST BUS STOP /////////////////////////
    //////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(Location(it.latitude, it.longitude))
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(route.stop1)
    }

    fun locationUpdate(location : Location) {
        route.stop1 = location
        route.stop1.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().position(LatLng(route.stop1.lat, route.stop1.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.stop1.lat, route.stop1.lng), route.stop1.zoom))
        view?.showLocation(route.stop1)
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

    fun doSetLocation() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(route.stop1.lat, route.stop1.lng, route.stop1.zoom))
    }


    ///////////////////////////////////////////////////////////////
    /////// METHODS FOR SECONDS BUS STOP /////////////////////////
    //////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation1() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate1(Location(it.latitude, it.longitude))
        }
    }

    fun doConfigureMap1(m: GoogleMap) {
        map = m
        locationUpdate1(route.stop2)
    }

    fun locationUpdate1(location : Location) {
        route.stop2 = location
        route.stop2.zoom = 15f
        val location1 = LatLng(route.stop2.lat,route.stop2.lng)
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().position(LatLng(route.stop2.lat, route.stop2.lng))
            .draggable(true)
            .position(location1)
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.stop2.lat, route.stop2.lng), route.stop2.zoom))
        view?.showLocation1(route.stop2)
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates1() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate1(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun doSetLocation1() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST2, "location", Location(route.stop2.lat, route.stop2.lng, route.stop2.zoom))
    }
    //////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    /////// METHODS FOR THIRD BUS STOP /////////////////////////
    //////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation2() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate2(Location(it.latitude, it.longitude))
        }
    }

    fun doConfigureMap2(m: GoogleMap) {
        map = m
        locationUpdate2(route.stop3)
    }

    fun locationUpdate2(location : Location) {
        route.stop3 = location
        route.stop3.zoom = 15f
        val location1 = LatLng(route.stop3.lat,route.stop3.lng)
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().position(LatLng(route.stop3.lat, route.stop3.lng))
            .draggable(true)
            .position(location1)
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.stop3.lat, route.stop3.lng), route.stop3.zoom))
        view?.showLocation1(route.stop3)
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates2() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate2(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun doSetLocation2() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST3, "location", Location(route.stop3.lat, route.stop3.lng, route.stop3.zoom))
    }
    //////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    /////// METHODS FOR FOURTH BUS STOP /////////////////////////
    //////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation3() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate3(Location(it.latitude, it.longitude))
        }
    }

    fun doConfigureMap3(m: GoogleMap) {
        map = m
        locationUpdate3(route.stop4)
    }

    fun locationUpdate3(location : Location) {
        route.stop4 = location
        route.stop4.zoom = 15f
        val location1 = LatLng(route.stop4.lat,route.stop4.lng)
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().position(LatLng(route.stop4.lat, route.stop4.lng))
            .draggable(true)
            .position(location1)
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.stop4.lat, route.stop4.lng), route.stop4.zoom))
        view?.showLocation1(route.stop4)
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates3() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate3(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun doSetLocation3() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST4, "location", Location(route.stop4.lat, route.stop4.lng, route.stop4.zoom))
    }
    //////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////
    /////// METHODS FOR FIFTH BUS STOP /////////////////////////
    //////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation4() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate4(Location(it.latitude, it.longitude))
        }
    }

    fun doConfigureMap4(m: GoogleMap) {
        map = m
        locationUpdate4(route.stop5)
    }

    fun locationUpdate4(location : Location) {
        route.stop5 = location
        route.stop5.zoom = 15f
        val location1 = LatLng(route.stop5.lat,route.stop5.lng)
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().position(LatLng(route.stop5.lat, route.stop5.lng))
            .draggable(true)
            .position(location1)
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.stop5.lat, route.stop5.lng), route.stop5.zoom))
        view?.showLocation1(route.stop5)
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates4() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate4(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun doSetLocation4() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST5, "location", Location(route.stop5.lat, route.stop5.lng, route.stop5.zoom))
    }
    //////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    /////// METHODS FOR SIXTH BUS STOP /////////////////////////
    //////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation5() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate5(Location(it.latitude, it.longitude))
        }
    }

    fun doConfigureMap5(m: GoogleMap) {
        map = m
        locationUpdate5(route.stop6)
    }

    fun locationUpdate5(location : Location) {
        route.stop6 = location
        route.stop6.zoom = 15f
        val location1 = LatLng(route.stop6.lat,route.stop6.lng)
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().position(LatLng(route.stop6.lat, route.stop6.lng))
            .draggable(true)
            .position(location1)
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.stop6.lat, route.stop6.lng), route.stop6.zoom))
        view?.showLocation1(route.stop6)
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates5() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate5(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun doSetLocation5() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST6, "location", Location(route.stop6.lat, route.stop6.lng, route.stop6.zoom))
    }
    //////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    /////// METHODS FOR SEVENTTH BUS STOP /////////////////////////
    //////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation6() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate6(Location(it.latitude, it.longitude))
        }
    }

    fun doConfigureMap6(m: GoogleMap) {
        map = m
        locationUpdate6(route.stop7)
    }

    fun locationUpdate6(location : Location) {
        route.stop7 = location
        route.stop7.zoom = 15f
        val location1 = LatLng(route.stop7.lat,route.stop7.lng)
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().position(LatLng(route.stop7.lat, route.stop7.lng))
            .draggable(true)
            .position(location1)
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.stop7.lat, route.stop7.lng), route.stop7.zoom))
        view?.showLocation1(route.stop7)
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates6() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate6(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun doSetLocation6() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST7, "location", Location(route.stop7.lat, route.stop7.lng, route.stop7.zoom))
    }

    //////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    /////// METHODS FOR EIGHTH BUS STOP /////////////////////////
    //////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation7() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate7(Location(it.latitude, it.longitude))
        }
    }

    fun doConfigureMap7(m: GoogleMap) {
        map = m
        locationUpdate7(route.stop8)
    }

    fun locationUpdate7(location : Location) {
        route.stop8 = location
        route.stop8.zoom = 15f
        val location1 = LatLng(route.stop8.lat,route.stop8.lng)
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().position(LatLng(route.stop8.lat, route.stop8.lng))
            .draggable(true)
            .position(location1)
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.stop8.lat, route.stop8.lng), route.stop8.zoom))
        view?.showLocation1(route.stop8)
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates7() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate7(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun doSetLocation7() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST8, "location", Location(route.stop8.lat, route.stop8.lng, route.stop8.zoom))
    }
    //////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    /////// METHODS FOR NINTH BUS STOP /////////////////////////
    //////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation8() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate8(Location(it.latitude, it.longitude))
        }
    }

    fun doConfigureMap8(m: GoogleMap) {
        map = m
        locationUpdate8(route.stop9)
    }

    fun locationUpdate8(location : Location) {
        route.stop9 = location
        route.stop9.zoom = 15f
        val location1 = LatLng(route.stop9.lat,route.stop9.lng)
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().position(LatLng(route.stop9.lat, route.stop9.lng))
            .draggable(true)
            .position(location1)
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.stop9.lat, route.stop9.lng), route.stop9.zoom))
        view?.showLocation1(route.stop9)
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates8() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate8(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun doSetLocation8() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST8, "location", Location(route.stop9.lat, route.stop9.lng, route.stop9.zoom))
    }
    //////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    /////// METHODS FOR TENTH BUS STOP /////////////////////////
    //////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation9() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate9(Location(it.latitude, it.longitude))
        }
    }

    fun doConfigureMap9(m: GoogleMap) {
        map = m
        locationUpdate9(route.stop10)
    }

    fun locationUpdate9(location : Location) {
        route.stop10 = location
        route.stop10.zoom = 15f
        val location1 = LatLng(route.stop10.lat,route.stop10.lng)
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().position(LatLng(route.stop10.lat, route.stop10.lng))
            .draggable(true)
            .position(location1)
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.stop10.lat, route.stop10.lng), route.stop10.zoom))
        view?.showLocation1(route.stop10)
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates9() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate9(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun doSetLocation9() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST9, "location", Location(route.stop10.lat, route.stop10.lng, route.stop10.zoom))
    }
    //////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////







    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                route.image = data.data.toString()
                view?.showRoute(route)
            }
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                route.stop1 = location
                locationUpdate(location)
            }
            LOCATION_REQUEST2 -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                route.stop2 = location
                locationUpdate1(location)
            }
            LOCATION_REQUEST3 -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                route.stop3 = location
                locationUpdate2(location)
            }
            LOCATION_REQUEST4 -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                route.stop4 = location
                locationUpdate3(location)
            }
            LOCATION_REQUEST5 -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                route.stop5 = location
                locationUpdate4(location)
            }
            LOCATION_REQUEST6 -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                route.stop6 = location
                locationUpdate5(location)
            }
            LOCATION_REQUEST7 -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                route.stop7 = location
                locationUpdate6(location)
            }
            LOCATION_REQUEST8-> {
                val location = data.extras?.getParcelable<Location>("location")!!
                route.stop8 = location
                locationUpdate7(location)
            }
            LOCATION_REQUEST9 -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                route.stop9 = location
                locationUpdate8(location)
            }
            LOCATION_REQUEST10 -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                route.stop10 = location
                locationUpdate9(location)
            }
        }
    }
}