package com.example.movieexplorer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.BitmapShader;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieexplorer.R;

import java.security.MessageDigest;

public class ImageLoader {

    public static void loadMoviePoster(Context context, String posterPath, ImageView imageView) {
        String fullUrl = "https://image.tmdb.org/t/p/w500" + posterPath;

        Glide.with(context)
                .load(fullUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_movie_placeholder)
                        .error(R.drawable.ic_movie_error)
                        .centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    public static void loadMovieBackdrop(Context context, String backdropPath, ImageView imageView) {
        String fullUrl = "https://image.tmdb.org/t/p/w780" + backdropPath;

        Glide.with(context)
                .load(fullUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_backdrop_placeholder)
                        .error(R.drawable.ic_backdrop_error)
                        .centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    public static void loadProfileImage(Context context, String path, ImageView imageView) {
        if (path != null && !path.isEmpty()) {
            String fullUrl = "https://image.tmdb.org/t/p/w185" + path;

            Glide.with(context)
                    .load(fullUrl)
                    .apply(new RequestOptions()
                            .circleCrop()
                            .placeholder(R.drawable.ic_profile_placeholder)
                            .error(R.drawable.ic_profile_error))
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_profile_placeholder);
        }
    }

    public static void loadWithRoundedCorners(Context context, String url, ImageView imageView, int cornerRadius) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .transform(new RoundedCornersTransformation(cornerRadius))
                        .placeholder(R.drawable.ic_movie_placeholder)
                        .error(R.drawable.ic_movie_error))
                .into(imageView);
    }

    public static void preloadImages(Context context, String... urls) {
        for (String url : urls) {
            Glide.with(context)
                    .load(url)
                    .preload();
        }
    }

    public static void clearCache(Context context) {
        Glide.get(context).clearMemory();
        new Thread(() -> Glide.get(context).clearDiskCache()).start();
    }

    // Custom transformation for rounded corners (simplified version)
    public static class RoundedCornersTransformation extends BitmapTransformation {
        private final int radius;

        public RoundedCornersTransformation(int radius) {
            this.radius = radius;
        }

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvas.drawRoundRect(new RectF(0, 0, outWidth, outHeight), radius, radius, paint);

            return result;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
            messageDigest.update(("roundedCorners" + radius).getBytes());
        }
    }
}