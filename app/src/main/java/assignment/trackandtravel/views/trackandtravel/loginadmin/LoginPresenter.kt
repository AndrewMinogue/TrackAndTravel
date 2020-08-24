package assignment.trackandtravel.views.trackandtravel.loginadmin

import assignment.trackandtravel.models.firebase.RouteFireStore
import assignment.trackandtravel.views.trackandtravel.base.BasePresenter
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import assignment.trackandtravel.views.trackandtravel.base.VIEW
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast


class LoginPresenter(view: BaseView) : BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: RouteFireStore? = null

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
                        view?.navigateTo(VIEW.ADMIN1)
                    }
                } else {
                    view?.hideProgress()
                    view?.navigateTo(VIEW.ADMIN1)
                }

                view?.hideProgress()
                view?.navigateTo(VIEW.ADMIN1)
            }
        }
    }

    fun doSignUp(email: String, password: String) {
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(view!!) { task ->
                if (task.isSuccessful) {
                    view?.hideProgress()
                    view?.navigateTo(VIEW.ADMIN1)
                } else {
                    view?.hideProgress()
                    view?.toast("Sign Up Failed: ${task.exception?.message}")
                }
            }
    }

}

