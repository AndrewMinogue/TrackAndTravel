package assignment.trackandtravel.views.trackandtravel.trackandtravellist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_track_list.*
import assignment.trackandtravel.R
import assignment.trackandtravel.activities.*
import assignment.trackandtravel.main.MainApp
import org.jetbrains.anko.startActivityForResult
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainDriverView: BaseView(){


    // THIS WAS A TEST CLASS ANDREW




    lateinit var presenter: MainDriverPresenter
    lateinit var app: MainApp

    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.driver_home)
        super.init(toolbarAdd, false)
        app = application as MainApp

        presenter = initPresenter(MainDriverPresenter(this)) as MainDriverPresenter



        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
//            googleMap.isMyLocationEnabled = true
            val location1 = LatLng(13.03,77.60)
            googleMap.addMarker(MarkerOptions().position(location1).title("My Location"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location1,5f))

            val location2 = LatLng(9.89,78.11)
            googleMap.addMarker(MarkerOptions().position(location2).title("Madurai"))


            val location3 = LatLng(13.00,77.00)
            googleMap.addMarker(MarkerOptions().position(location3).title("Bangalore"))

        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_map -> presenter.doShowHillfortsMap()
            R.id.item_logout -> presenter.doLogout()
        }
        when (item?.itemId) {
            R.id.item_settings -> startActivityForResult<SettingsActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

}




