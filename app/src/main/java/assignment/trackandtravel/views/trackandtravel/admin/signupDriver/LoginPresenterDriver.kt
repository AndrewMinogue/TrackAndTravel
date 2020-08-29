package assignment.trackandtravel.views.trackandtravel.admin.signupDriver

import assignment.trackandtravel.models.UserModel
import assignment.trackandtravel.models.firebase.RouteFireStore
import assignment.trackandtravel.views.trackandtravel.base.BasePresenter
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import assignment.trackandtravel.views.trackandtravel.base.VIEW
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


class LoginPresenterDriver(view: BaseView) : BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUsers: List<UserModel> = arrayListOf()

    var fireStore: RouteFireStore? = null
    var fireStore1: RouteFireStore? = null
    lateinit var db: DatabaseReference
    var user = UserModel()
    lateinit var users: List<UserModel>

    init {
        if (app.routes is RouteFireStore) {
            fireStore = app.routes as RouteFireStore
        }
    }

    init {
        if (app.users is RouteFireStore) {
            fireStore1 = app.users as RouteFireStore
        }
    }


    fun getUsers() {
        fireStore1!!.fetchUsers {
            users = app.users.findAllUsers().filter { it.id == it.id }
            view?.info(users)
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
                    } else {
                        view?.hideProgress()
                        view?.toast("Sign Up Failed: ${task.exception?.message}")
                    }
                }
            }
    }

        fun doAddOrSave(email: String, busdriver: Boolean) {
            user.emailBus = email
            user.busdriver = busdriver


            doAsync {
                app.users.createUser(user)
            }
        }
    }



