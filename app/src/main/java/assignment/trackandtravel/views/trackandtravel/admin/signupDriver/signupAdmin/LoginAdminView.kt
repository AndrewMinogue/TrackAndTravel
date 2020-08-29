package assignment.trackandtravel.views.trackandtravel.admin.signupDriver.signupAdmin

import android.os.Bundle
import android.view.View
import assignment.trackandtravel.R
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast


class LoginAdminView : BaseView() {

    lateinit var presenter: LoginAdminPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        init(toolbar, false)
        progressBar.visibility = View.GONE

        presenter = initPresenter(LoginAdminPresenter(this)) as LoginAdminPresenter

        presenter.getAdmins()

        signUp.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            val admin1 = true
            if (email == "" || password == "") {
                toast("Please provide email + password")
            } else {
                presenter.doAddOrSave(email,admin1)
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