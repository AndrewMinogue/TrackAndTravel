package assignment.trackandtravel.views.trackandtravel.loginadmin

import android.os.Bundle
import android.view.View
import assignment.trackandtravel.R
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast


class LoginViewPassenger : BaseView() {

    lateinit var presenter: LoginPresenterPassenger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init(toolbar, false)
        progressBar.visibility = View.GONE

        presenter = initPresenter(LoginPresenterPassenger(this)) as LoginPresenterPassenger

        signUp.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            } else {
                presenter.doSignUp(email, password)
            }
        }

        logIn.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            } else {
                presenter.doLogin(email, password)
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