package assignment.trackandtravel.views.trackandtravel.passenger

import assignment.trackandtravel.R
import assignment.trackandtravel.models.RouteModel
import assignment.trackandtravel.views.trackandtravel.base.BasePresenter
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import assignment.trackandtravel.views.trackandtravel.base.VIEW
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class PassengerListPresenter1(view: BaseView) : BasePresenter(view) {


    private var currentRoutes: List<RouteModel> = arrayListOf()



    fun doAddRoute() {
        view?.navigateTo(VIEW.PASSENGERROUTE)
    }

    fun doEditRoute(route: RouteModel) {
        view?.navigateTo(VIEW.PASSENGERROUTE, 0, "route_edit", route)
    }

    fun doShowRoutesMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun loadRoutes() {
        doAsync {
            val routes = app.routes.findALL()
            uiThread {
                currentRoutes = routes
                view?.showRoutes(routes)
            }
        }
    }

    fun doSortFavourite() {
        val favourites = app.routes.sortedByFavourite()
        if (favourites != null) {
            currentRoutes = favourites
            view?.showRoutes(favourites)
        }
    }


    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.routes.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

}