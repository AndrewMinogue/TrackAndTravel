package assignment.trackandtravel.views.trackandtravel.passenger

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_track_list.*
import assignment.trackandtravel.R
import assignment.trackandtravel.activities.*
import assignment.trackandtravel.main.MainApp
import assignment.trackandtravel.models.RouteModel
import org.jetbrains.anko.startActivityForResult
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import assignment.trackandtravel.views.trackandtravel.passenger.route.PassengerRoutePresenter
import assignment.trackandtravel.views.trackandtravel.trackandtravellist.RouteAdapter
import assignment.trackandtravel.views.trackandtravel.trackandtravellist.RouteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_route_list.*

class PassengerListView1: BaseView(), RouteListener {

    lateinit var presenter: PassengerListPresenter1
    lateinit var app: MainApp
    var favouriteT = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_list)
        super.init(toolbar, false)
        app = application as MainApp

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        presenter = initPresenter(PassengerListPresenter1(this)) as PassengerListPresenter1

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadRoutes()



        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home1 -> {
                    startActivityForResult<PassengerListView1>(0)
                }
                R.id.item_settings1 -> {

                    startActivityForResult<SettingsActivity>(0)
                }
            }
            false
        }



        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//        searchBtn.setOnClickListener{ presenter.doSearchHillforts()}

    }


    override fun showRoutes(routes: List<RouteModel>) {
        recyclerView.adapter = RouteAdapter(routes, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_logout ->presenter.doLogout()
//            R.id.item_favourites -> {
//                if(favouriteT) {
//                    presenter.loadHillforts()
//                    favouriteT = !favouriteT
//                    item.setTitle(R.string.hillfort_favourites)
//                } else {
//                    presenter.doSortFavourite()
//                    favouriteT = !favouriteT
//                    item.setTitle(R.string.hillfort_showall)
//                }
//            }
            R.id.item_settings -> startActivityForResult<SettingsActivity>(0)
//            R.id.item_favourites1 -> presenter.doSortFavourite()
            R.id.item_settings1 -> startActivityForResult<SettingsActivity>(0)
            R.id.item_home1 -> startActivityForResult<PassengerListView1>(0)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onRouteClick(route: RouteModel) {
        presenter.doEditRoute(route)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadRoutes()
        recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }


}
