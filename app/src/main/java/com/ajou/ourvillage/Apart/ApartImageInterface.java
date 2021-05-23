package com.ajou.ourvillage.Apart;

import android.net.Uri;

public interface ApartImageInterface {
    void uploadFireBaseSuccess(Uri uri);

    void uploadFireBaseFailure();
}