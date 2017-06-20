package ezlife.movil.oneparkingapp.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.adapters.InfoAdapter
import ezlife.movil.oneparkingapp.databinding.InfoBinding
import ezlife.movil.oneparkingapp.fragments.PricesFragment
import ezlife.movil.oneparkingapp.fragments.ScheduleFragment

class InfoZoneActivity : AppCompatActivity() {

    lateinit var binding: InfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info_zone)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        val id = extras.getString(EXTRA_ID)
        val name = extras.getString(EXTRA_NAME)

        supportActionBar?.subtitle = name
        val titles = resources.getStringArray(R.array.info_titles)
        val pages = listOf(ScheduleFragment.instance(id), PricesFragment.instance())

        binding.pager.adapter = InfoAdapter(supportFragmentManager, pages, titles)
        binding.tabs.setupWithViewPager(binding.pager)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    companion object {

        val EXTRA_ID = "zoneId"
        val EXTRA_NAME = "zoneName"

    }
}
