package ezlife.movil.oneparkingapp.activities

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.SplashBinding
import ezlife.movil.oneparkingapp.providers.User
import ezlife.movil.oneparkingapp.util.Preference
import ezlife.movil.oneparkingapp.util.SessionApp

class SplashActivity : AppCompatActivity(), Animator.AnimatorListener {

    lateinit var binding: SplashBinding
    lateinit var set: AnimatorSet
    lateinit var reveal: Animator
    val logged:Boolean by lazy {preferences().getBoolean(Preference.USER_LOGGED, false)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        if(logged){
            retrieveUser()
        }
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {

            val textLogo: ObjectAnimator = ObjectAnimator.ofFloat(binding.oneparking, "alpha", 0f, 1f)
            textLogo.duration = 300

            val wait: ObjectAnimator = ObjectAnimator.ofFloat(binding.oneparking, "alpha", 1f, 1f)
            wait.duration = 200

            reveal = revealLogo()
            reveal.addListener(this)

            binding.logo.visibility = View.VISIBLE

            set = AnimatorSet()
            set.play(reveal).before(textLogo)
            set.play(textLogo).before(wait)

            set.addListener(this)
            set.start()
        }
    }

    fun retrieveUser(){
        val preference = preferences()
        val id = preference.getString(Preference.USER_ID, "")
        val email = preference.getString(Preference.USER_EMAIL, "")
        val cel = preference.getString(Preference.USER_CEL, "")
        val name = preference.getString(Preference.USER_NAME, "")
        val disability = preference.getBoolean(Preference.USER_DISABILITY, false)
        val token = preference.getString(Preference.TOKEN, "")
        val user:User = User(id, "Cliente", name, "", cel, email, disability, emptyList(), 0)
        SessionApp.token = token
        SessionApp.user = user
    }

    fun revealLogo(): Animator {
        val animator: Animator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            ViewAnimationUtils.createCircularReveal(binding.logo
                    , (binding.logo.right / 2)
                    , (binding.logo.bottom / 2)
                    , 0f, (binding.logo.width * 2f)
            )
        else ObjectAnimator.ofFloat(binding.logo, "alpha", 0f, 1f)
        animator.duration = 500
        return animator
    }

    override fun onAnimationRepeat(animation: Animator?) {}

    override fun onAnimationEnd(animation: Animator?) {
        animation?.removeAllListeners()
        when (animation) {
            reveal -> {
                reveal.removeAllListeners()
                binding.oneparking.visibility = View.VISIBLE
            }
            set -> {
                set.removeAllListeners()
                if(logged) {
                    startActivity<MapActivity>()
                }else{
                    startActivity<LoginActivity>()
                }
            }
        }
    }

    override fun onAnimationCancel(animation: Animator?) {}

    override fun onAnimationStart(animation: Animator?) {}

}