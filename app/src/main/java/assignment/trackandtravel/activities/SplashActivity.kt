package assignment.trackandtravel.activities


import assignment.trackandtravel.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler



class SplashActivity : Activity() {

    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashActivity, ChooseLoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

    }
}