package ezlife.movil.oneparkingapp.ui.main.reserve


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.di.Injectable
import ezlife.movil.oneparkingapp.ui.main.zone.ZoneFragment
import ezlife.movil.oneparkingapp.ui.main.zone.ZoneViewModel

class ReserveFragment : DialogFragment(), Injectable {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_reserve, container, false)
    }

    companion object {
        fun instance(state: ZoneViewModel.State, disability: Boolean): ReserveFragment =
                ReserveFragment()
    }

}
