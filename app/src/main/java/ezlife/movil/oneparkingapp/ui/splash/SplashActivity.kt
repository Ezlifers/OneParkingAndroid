package ezlife.movil.oneparkingapp.ui.splash

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import dagger.android.AndroidInjection
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.activities.MapActivity
import ezlife.movil.oneparkingapp.activities.startActivity
import ezlife.movil.oneparkingapp.databinding.SplashBinding
import ezlife.movil.oneparkingapp.ui.entry.EntryActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity(), Animator.AnimatorListener {

    @Inject
    lateinit var viewModel: SplashViewModel

    private lateinit var binding: SplashBinding
    private lateinit var set: AnimatorSet
    private lateinit var reveal: Animator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
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

    private fun revealLogo(): Animator {
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

    override fun onAnimationEnd(animation: Animator?) {
        when (animation) {
            reveal -> {
                reveal.removeAllListeners()
                binding.oneparking.visibility = View.VISIBLE
            }
            set -> {
                set.removeAllListeners()
                if (viewModel.isLogged()) startActivity<MapActivity>()
                else startActivity<EntryActivity>()
            }
        }
    }

    override fun onAnimationRepeat(p0: Animator?) {}
    override fun onAnimationCancel(p0: Animator?) {}
    override fun onAnimationStart(p0: Animator?) {}

}