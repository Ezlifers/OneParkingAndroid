package ezlife.movil.oneparkingapp.attrs;

import android.content.res.AssetManager;
import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Dario Chamorro on 25/09/2016.
 */

public class Global {

    @BindingAdapter("app:imgUrl")
    public static void loadImage(ImageView view, String url){
        Picasso.with(view.getContext())
                .load(Uri.parse(url))
                .into(view);
    }

    @BindingAdapter("app:roboto")
    public static void loadRoboto(TextView view, String name){
        AssetManager assetManager = view.getContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager,"fonts/"+name+".ttf");
        view.setTypeface(typeface);
    }

}
