package assignment.trackandtravel.views.trackandtravel.routemapping

import assignment.trackandtravel.views.trackandtravel.base.BasePresenter
import assignment.trackandtravel.views.trackandtravel.base.VIEW
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import com.google.firebase.auth.FirebaseAuth


class RouteMapPresenter(view: BaseView) : BasePresenter(view) {

    fun doRouteMap() {
        view?.navigateTo(VIEW.ROUTE)
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        view?.navigateTo(VIEW.LOGIN)
    }
}