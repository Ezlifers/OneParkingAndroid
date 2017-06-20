package ezlife.movil.oneparkingapp.fragments


import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.squareup.picasso.Picasso
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.FragmentReportBinding
import ezlife.movil.oneparkingapp.providers.Incident
import ezlife.movil.oneparkingapp.providers.IncidentProvider
import ezlife.movil.oneparkingapp.providers.UserIncident
import ezlife.movil.oneparkingapp.providers.ZoneIncident
import ezlife.movil.oneparkingapp.util.SessionApp
import ezlife.movil.oneparkingapp.util.text
import ezlife.movil.oneparkingauxiliar.util.getBytesFromBitmap
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class ReportFragment : DialogFragment() {

    lateinit var binding: FragmentReportBinding

    val loading: ProgressDialog by lazy { makeLoading() }
    val provider: IncidentProvider  by lazy { IncidentProvider(activity as AppCompatActivity, loading) }

    var width = 0
    var height = 0
    lateinit var fileImage: File
    var captured: Boolean = false

    var name:String = ""
    var code:Int = 0
    var id:String = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)
        binding.handler = this

        val windowsManager: WindowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowsManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)

        width = metrics.widthPixels
        height = resources.getDimensionPixelSize(R.dimen.report_img)

        name = arguments.getString(EXTRA_ZONE_NAME)
        code = arguments.getInt(EXTRA_ZONE_CODE)
        id = arguments.getString(EXTRA_ZONE_ID)

        return binding.root
    }

    //region Take Photo
    fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "OneParking")
        if (!dir.exists())
            dir.mkdir()
        fileImage = File("${dir.path}${File.separator}Report_$name.jpg")
        val imageUri: Uri = FileProvider.getUriForFile(activity, activity.applicationContext.packageName + ".provider", fileImage)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                captured = true
                Picasso.with(activity).load(fileImage).resize(width, width).centerCrop().into(binding.img)
            }
        }
    }
    //endregion

    //region Send Report

    fun report() {
        val msg = binding.description.text()

        if (msg == "") {
            toast(R.string.report_form)
            return
        } else if (!captured) {
            toast(R.string.report_image)
            return
        }
        sendReport(msg)
    }


    fun sendReport(msg: String) {
        val drawable: BitmapDrawable = binding.img.drawable as BitmapDrawable
        loading.show()
        thread {
            val imageData = getBytesFromBitmap(drawable.bitmap)
            val img = Base64.encodeToString(imageData, Base64.DEFAULT)

            val zoneIncident: ZoneIncident = ZoneIncident(id, code, name)
            val user: UserIncident = UserIncident(SessionApp.user.nombre, SessionApp.user.celular)
            val req: Incident = Incident(img, msg, zoneIncident, user)

            activity.runOnUiThread {
                provider.notifyIncident(req) { success, _ ->
                    loading.dismiss()
                    if (success) {
                        toast(R.string.report_success)
                        dismiss()
                    } else {
                        toast(R.string.report_fail)
                    }

                }
            }
        }
    }
    //endregion

    //region Extras
    companion object {

        val EXTRA_ZONE_ID = "zoneId"
        val EXTRA_ZONE_CODE = "zoneCode"
        val EXTRA_ZONE_NAME = "zoneName"

        fun instance(id: String, code: Int, name: String): ReportFragment {
            val fragment = ReportFragment()
            fragment.setupArgs(EXTRA_ZONE_ID to id
                    , EXTRA_ZONE_CODE to code
                    , EXTRA_ZONE_NAME to name)
            return fragment
        }

    }
    //endregion

}
