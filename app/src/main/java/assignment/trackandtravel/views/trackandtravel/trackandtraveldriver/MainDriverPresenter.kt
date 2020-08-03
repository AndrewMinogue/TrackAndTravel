package assignment.trackandtravel.views.trackandtravel.trackandtravellist

import assignment.trackandtravel.views.trackandtravel.base.BasePresenter
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import assignment.trackandtravel.views.trackandtravel.base.VIEW
import com.google.firebase.auth.FirebaseAuth


class MainDriverPresenter(view: BaseView) : BasePresenter(view) {


    fun doShowHillfortsMap() {
        view?.navigateTo(VIEW.MAPS)
    }


    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        view?.navigateTo(VIEW.LOGIN)
    }

}