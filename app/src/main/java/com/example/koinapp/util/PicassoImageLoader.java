package com.example.koinapp.util;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class PicassoImageLoader implements ImageLoader {

    @Inject
    PicassoImageLoader() {

    }

    @NonNull
    @Override
    public ImageRequest load(String url) {
        return new PicassoImageRequest(Picasso.get().load(url));
    }

    private static class PicassoImageRequest implements ImageRequest {

        private RequestCreator request;

        PicassoImageRequest(RequestCreator request) {

            this.request = request;
        }

        @Override
        public void into(@NonNull ImageView view) {
            request.into(view);

        }
    }
}
