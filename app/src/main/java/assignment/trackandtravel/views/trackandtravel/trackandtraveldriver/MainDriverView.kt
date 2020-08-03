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

class MainDriverView: BaseView(){

    lateinit var presenter: MainDriverPresenter
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_list)
        super.init(toolbarAdd, false)
        app = application as MainApp

        presenter = initPresenter(MainDriverPresenter(this)) as MainDriverPresenter


        DisplayRoutes.setOnClickListener {
                presenter.doShowHillfortsMap()
            }
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




