package ezlife.movil.oneparkingapp.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;

import ezlife.movil.oneparkingapp.R;
import ezlife.movil.oneparkingapp.databinding.SplashBinding;
import ezlife.movil.oneparkingapp.util.Preference;

public class SplashActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener, Animator.AnimatorListener {


    SplashBinding binding;

    AnimatorSet set;
    Animator reveal;
    boolean logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(this);
        SharedPreferences preferences =  getSharedPreferences(Preference.NAME, MODE_PRIVATE);
        logged = preferences.getBoolean(Preference.USER_LOGGED, false);
    }

    public Animator revealLogo(){
        Animator animator;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            animator = ViewAnimationUtils
                    .createCircularReveal(binding.logo
                            ,(binding.logo.getRight()/2)
                            ,(binding.logo.getBottom()/2)
                            ,0,(binding.logo.getWidth()*2)
                    );
        }else{
            animator = ObjectAnimator.ofFloat(binding.logo,"alpha",0f,1f);
        }
        animator.setDuration(500);
        return animator;
    }

    //region Animator Listener
    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if(animation==reveal){
            reveal.removeAllListeners();
            binding.oneparking.setVisibility(View.VISIBLE);
        }else if(set==animation){
            set.removeAllListeners();
            Intent intent;
            if(logged)
                intent = new Intent(this, MapActivity.class);
            else
                intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
    //endregion

    @Override
    public void onGlobalLayout() {
        ObjectAnimator textLogo = ObjectAnimator.ofFloat(binding.oneparking,"alpha",0f,1f);
        textLogo.setDuration(300);

        ObjectAnimator wait = ObjectAnimator.ofFloat(binding.oneparking,"alpha",1f,1f);
        wait.setDuration(200);

        reveal = revealLogo();
        reveal.addListener(this);

        binding.logo.setVisibility(View.VISIBLE);
        set = new AnimatorSet();
        set.play(reveal).before(textLogo);
        set.play(textLogo).before(wait);

        set.addListener(this);
        set.start();

    }
}