package assignment.trackandtravel.views.trackandtravel.base

import android.content.Intent

import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import assignment.trackandtravel.views.trackandtravel.map.TrackMapView
import assignment.trackandtravel.views.trackandtravel.trackandtravellist.MainDriverView
import assignment.trackandtravel.views.trackandtravel.loginadmin.LoginViewPassenger
import assignment.trackandtravel.views.trackandtravel.routemapping.RouteMapView
import assignment.trackandtravel.views.trackandtravel.trackandtraveladmin.MainAdminView
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger


val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2
val IMAGE_REQUEST1 = 3
val IMAGE_REQUEST2 = 4
val IMAGE_REQUEST3 = 5

enum class VIEW {
    ROUTE, MAPS, LIST, LOGIN, ADMIN, DRIVER
}

open abstract class BaseView : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, MainDriverView::class.java)
        when (view) {
            VIEW.ADMIN -> intent = Intent(this,MainAdminView::class.java )
            VIEW.ROUTE -> intent = Intent(this,RouteMapView::class.java )
            VIEW.MAPS -> intent = Intent(this, TrackMapView::class.java)
            VIEW.LIST -> intent = Intent(this, MainDriverView::class.java)
            VIEW.LOGIN -> intent = Intent(this, LoginViewPassenger::class.java)
            VIEW.DRIVER -> intent = Intent(this, MainDriverView::class.java)

        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar, upEnabled: Boolean) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            toolbar.title = "${title}: ${user.email}"
        }
    }


        override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showProgress() {}
    open fun hideProgress() {}
}