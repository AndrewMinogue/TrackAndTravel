package assignment.trackandtravel.views.trackandtravel.adminhomepage

import assignment.trackandtravel.views.trackandtravel.base.BasePresenter
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import assignment.trackandtravel.views.trackandtravel.base.VIEW
import com.google.firebase.auth.FirebaseAuth

class AdminHomePresenter(view: BaseView) : BasePresenter(view) {


    fun doCreateRoutes() {
        view?.navigateTo(VIEW.ROUTE)
    }

    fun doShowRoutes() {
        view?.navigateTo(VIEW.LIST1)
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        view?.navigateTo(VIEW.LOGIN)
    }


}