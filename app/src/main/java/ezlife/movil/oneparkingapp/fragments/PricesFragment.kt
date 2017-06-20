package ezlife.movil.oneparkingapp.fragments


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.adapters.PriceAdapter
import ezlife.movil.oneparkingapp.databinding.FragmentPricesBinding
import ezlife.movil.oneparkingapp.db.DB
import ezlife.movil.oneparkingapp.util.asyncUI
import ezlife.movil.oneparkingapp.util.await

class PricesFragment : Fragment() {

    lateinit var binding: FragmentPricesBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_prices, container, false)
        loadPrices()

        return binding.root
    }

    fun loadPrices() = asyncUI {
        val dao =  DB.con.configDao()
        val config = await { dao.config() }
        val prices = config.precio
        val timeMin =  config.tiempoMin / 60
        binding.list.adapter = PriceAdapter(timeMin, prices)
        binding.list.layoutManager = LinearLayoutManager(context)
    }

    companion object{
        fun instance():PricesFragment = PricesFragment()
    }
}
