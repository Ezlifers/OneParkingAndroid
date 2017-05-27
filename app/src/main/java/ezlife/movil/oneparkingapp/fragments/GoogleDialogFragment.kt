package ezlife.movil.oneparkingapp.fragments


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.google.android.gms.common.GoogleApiAvailability

import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.activities.MapActivity

class GoogleDialogFragment : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val errorCode = this.arguments.getInt(DIALOG_ERROR)
        return GoogleApiAvailability.getInstance().getErrorDialog(
                this.activity, errorCode, REQUEST_RESOLVE_ERROR)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        (activity as MapActivity).onDialogDismissed()
    }

    companion object {
        val DIALOG_ERROR = "dialog_error"
        private val REQUEST_RESOLVE_ERROR = 1001

        fun show(fragmentManager:FragmentManager, error:Int){
            val ft = fragmentManager.beginTransaction()
            val prev = fragmentManager.findFragmentByTag(DIALOG_ERROR)
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            val dialog = GoogleDialogFragment()
            dialog.setupArgs(DIALOG_ERROR to error)
            dialog.show(ft, DIALOG_ERROR)
        }
    }
}
