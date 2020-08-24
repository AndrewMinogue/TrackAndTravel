package assignment.trackandtravel.views.trackandtravel.loginadmin

import assignment.trackandtravel.models.firebase.RouteFireStore
import assignment.trackandtravel.views.trackandtravel.base.BasePresenter
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import assignment.trackandtravel.views.trackandtravel.base.VIEW
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import org.jetbrains.anko.toast


class LoginPresenterDriver(view: BaseView) : BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    var fireStore: RouteFireStore? = null
    lateinit var db: DatabaseReference

    init {
        if (app.routes is RouteFireStore) {
            fireStore = app.routes as RouteFireStore
        }
    }

    fun doLogin(email: String, password: String) {
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                if (fireStore != null) {
                    fireStore!!.fetchRoutes {
                        view?.hideProgress()
                        view?.navigateTo(VIEW.DRIVERLIST)
                    }
                } else {
                    view?.hideProgress()
                    view?.navigateTo(VIEW.DRIVERLIST)
                }
                view?.hideProgress()
                view?.navigateTo(VIEW.DRIVERLIST)
            }
        }
    }

    fun doSignUp(email: String, password: String) {
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(view!!) { task ->
                if (task.isSuccessful) {
                    view?.hideProgress()
                    view?.navigateTo(VIEW.DRIVERLIST)
                } else {
                    view?.hideProgress()
                    view?.toast("Sign Up Failed: ${task.exception?.message}")
                }
            }
    }

}

