package com.example.fastuae.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.fastuae.R;

public class LoadImage {

    public static void glide(Context context, ImageView imageView, Drawable imagePath){

        Glide.with(context).load(imagePath)
                .error(R.drawable.ic_no_car)
                .apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .into(imageView);
    }

    public static void glideString(Context context, ImageView imageView, String imagePath){

        if(!TextUtils.isEmpty(imagePath)){
            Glide.with(context).load(imagePath)
                    .error(R.drawable.ic_no_car)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                    .into(imageView);
        }else {
            Glide.with(context).load(R.drawable.ic_no_car)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                    .into(imageView);
        }

    }


}
