package ezlife.movil.oneparkingapp.ui.infozone

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.ui.adapter.InfoAdapter
import kotlinx.android.synthetic.main.activity_info_zone.*
import javax.inject.Inject

class InfoZoneActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_info_zone)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        val id = extras.getString(EXTRA_ID)
        val name = extras.getString(EXTRA_NAME)

        supportActionBar?.title = name
        val titles = resources.getStringArray(R.array.info_titles)

        pager.adapter = InfoAdapter(supportFragmentManager, id, titles)
        tabs.setupWithViewPager(pager)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = injector

    companion object {
        val EXTRA_ID = "zoneId"
        val EXTRA_NAME = "zoneName"
    }
}