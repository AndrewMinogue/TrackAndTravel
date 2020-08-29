package assignment.trackandtravel.views.trackandtravel.admin.signupDriver

import android.os.Bundle
import android.view.View
import assignment.trackandtravel.R
import assignment.trackandtravel.models.UserModel
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import assignment.trackandtravel.views.trackandtravel.admin.signupDriver.LoginPresenterDriver
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast


class LoginViewDriver : BaseView() {

    lateinit var presenter: LoginPresenterDriver
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        init(toolbar, false)
        progressBar.visibility = View.GONE

        presenter = initPresenter(LoginPresenterDriver(this)) as LoginPresenterDriver


        presenter.getUsers()


        signUp.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            val busdriver = true
            if (email == "" || password == "") {
                toast("Please provide email + password")
            } else {
                presenter.doAddOrSave(email,busdriver)
                presenter.doSignUp(email, password)
            }
        }

    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}