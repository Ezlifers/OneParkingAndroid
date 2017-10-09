package ezlife.movil.oneparkingapp.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ezlife.movil.oneparkingapp.ui.info.prices.PricesFragment
import ezlife.movil.oneparkingapp.ui.infozone.schedule.ScheduleFragment

class InfoAdapter(fragmentManager: FragmentManager, private val id: String,
                  private val titles: Array<String>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> ScheduleFragment.instance(id)
        1 -> PricesFragment.instance()
        else -> ScheduleFragment.instance(id)
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence = titles[position]

}