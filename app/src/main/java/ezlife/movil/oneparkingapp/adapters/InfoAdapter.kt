package ezlife.movil.oneparkingapp.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class  InfoAdapter(fragmentManager: FragmentManager, val pages:List<Fragment>, val titles:Array<String>): FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = pages[position]

    override fun getCount(): Int = pages.size

    override fun getPageTitle(position: Int): CharSequence = titles[position]

}