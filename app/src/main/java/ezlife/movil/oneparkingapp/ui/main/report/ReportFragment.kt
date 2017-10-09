package ezlife.movil.oneparkingapp.ui.main.report

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.squareup.picasso.Picasso
import dagger.android.support.AndroidSupportInjection
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.FragmentReportBinding
import ezlife.movil.oneparkingapp.util.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_report.*
import org.jetbrains.anko.support.v4.toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ReportFragment : DialogFragment() {

    @Inject
    lateinit var loader: Loader
    @Inject
    lateinit var viewModel: ReportViewModel

    lateinit var binding: FragmentReportBinding
    val dis: CompositeDisposable = CompositeDisposable()

    var width = 0
    var height = 0
    var fileImage: File? = null

    val name: String by lazy { arguments.getString(EXTRA_ZONE_NAME) }
    val code: Int by lazy { arguments.getInt(EXTRA_ZONE_CODE) }
    val id: String by lazy { arguments.getString(EXTRA_ZONE_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)
        binding.loader = loader
        width = resources.getDimensionPixelSize(R.dimen.dialog)
        height = resources.getDimensionPixelSize(R.dimen.report_img)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dis push btnImg.clicks()
                .subscribe { takePhoto() }

        dis push btnReport.clicks()
                .flatMap {
                    validateForm(R.string.report_form_image, description.text(),
                            if (fileImage == null) "" else "ready")
                }
                .loader(loader)
                .flatMap { viewModel.notifyIncident(id, code, name, img, it[0]) }
                .subscribeWithError(
                        onNext = {
                            toast(R.string.report_success)
                            dismiss()
                        },
                        onError = { toast(it.message!!) },
                        onHttpError = this::toast
                )
    }

    override fun onStop() {
        super.onStop()
        dis.clear()
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
            if (resultCode == Activity.RESULT_OK) Picasso.with(activity)
                    .load(fileImage)
                    .resize(width, width)
                    .centerCrop()
                    .into(binding.img)
            else fileImage = null
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