package com.ajou.ourvillage.Apart;

import android.net.Uri;

public interface ImageInterface {
    void uploadFireBaseSuccess(Uri uri);

    void uploadFireBaseFailure();
}