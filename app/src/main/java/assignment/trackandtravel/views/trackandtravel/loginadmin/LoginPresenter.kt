package assignment.trackandtravel.views.trackandtravel.loginadmin

import assignment.trackandtravel.models.AdminModel
import assignment.trackandtravel.models.UserModel
import assignment.trackandtravel.models.firebase.RouteFireStore
import assignment.trackandtravel.views.trackandtravel.base.BasePresenter
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import assignment.trackandtravel.views.trackandtravel.base.VIEW
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.toast


class LoginPresenter(view: BaseView) : BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: RouteFireStore? = null
    var fireStore1: RouteFireStore? = null
    var admin = AdminModel()
    lateinit var admins: List<AdminModel>

    init {
        if (app.routes is RouteFireStore) {
            fireStore = app.routes as RouteFireStore
        }
    }

    init {
        if (app.admins is RouteFireStore) {
            fireStore1 = app.admins as RouteFireStore
        }
    }



    fun getAdmins() {
        fireStore1!!.fetchAdmins {
            admins = app.admins.findAllAdmins().filter { it.id == it.id }
            view?.info(admins)
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
                    if (fireStore != null) {
                        fireStore!!.fetchRoutes {
                            view?.hideProgress()
                            view?.navigateTo(VIEW.ADMIN1)
                        }
                    }
                } else {
                    view?.hideProgress()
                    view?.toast("Sign Up Failed: ${task.exception?.message}")
                }
            }
    }

    fun findAdmin(email: String) :AdminModel{
        val foundAdmins = ArrayList<AdminModel>()
        getAdmins()
        admins.forEach {
            if (it.emailAdmin.contains(email, ignoreCase = true)) {
                foundAdmins.add(it)
            }
        }
        view?.info(foundAdmins)
        return foundAdmins[0]
    }


    fun doAddOrSave(email: String, admin1: Boolean) {
        admin.emailAdmin = email
        admin.admin = admin1


        doAsync {
            app.admins.createAdmin(admin)
        }
    }

}

