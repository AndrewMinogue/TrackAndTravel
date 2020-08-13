package assignment.trackandtravel.views.trackandtravel.trackandtraveladmin

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import assignment.trackandtravel.R
import assignment.trackandtravel.activities.SettingsActivity
import assignment.trackandtravel.main.MainApp
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import kotlinx.android.synthetic.main.activity_admin_home.*
import kotlinx.android.synthetic.main.activity_track_list.toolbarAdd
import org.jetbrains.anko.startActivityForResult

class MainAdminView: BaseView() {



    lateinit var presenter: MainAdminPresenter
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        super.init(toolbarAdd, false)
        app = application as MainApp

        presenter = initPresenter(MainAdminPresenter(this)) as MainAdminPresenter


        CreateRoutes.setOnClickListener {
            presenter.doRouteMap()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_map -> presenter.doRouteMap()
            R.id.item_logout -> presenter.doLogout()
        }
        when (item?.itemId) {
            R.id.item_settings -> startActivityForResult<SettingsActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

}