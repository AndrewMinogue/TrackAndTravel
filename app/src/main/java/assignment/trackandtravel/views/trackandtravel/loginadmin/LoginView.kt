package assignment.trackandtravel.views.trackandtravel.loginadmin

import android.os.Bundle
import android.view.View
import assignment.trackandtravel.R
import assignment.trackandtravel.views.trackandtravel.base.BaseView
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast


class LoginView : BaseView() {

    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login1)
        init(toolbar, false)
        progressBar.visibility = View.GONE

        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

        presenter.getAdmins()

        logIn.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            val user1 = presenter.findAdmin(email)

            if (email == "" || password == "") {
                toast("Please provide email + password")
            } else if(user1.equals(null) || user1.admin == false){
                toast("This is not a registered bus driver email")
            }else{
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