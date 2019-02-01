package id.gits.gitsimageview.utils;

import android.content.Context;
import com.squareup.picasso.Picasso;

public class Instance {

    private static Picasso mPicasso;

    public static Picasso getPicassoInstance(Context context){
        if (mPicasso == null) {
            mPicasso = new Picasso.Builder(context).build();
        }

        return mPicasso;
    }

}
