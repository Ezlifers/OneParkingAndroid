package ezlife.movil.oneparkingapp.ui.entry

import android.content.res.AssetManager
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.EntryBinding
import javax.inject.Inject

class EntryActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var navigation: EntryNavigationController
    lateinit var binding: EntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_entry)
        navigation.navigateToLogin()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = injector

    companion object {
        @JvmStatic
        @BindingAdapter("app:roboto")
        fun loadRoboto(view: TextView, name: String) {
            val assetManager: AssetManager = view.context.assets
            val typeface: Typeface = Typeface.createFromAsset(assetManager, "fonts/$name.ttf")
            view.typeface = typeface
        }
    }
}