package com.ajou.ourvillage.Tasty;

import android.net.Uri;

public interface TastyImageInterface {
    void uploadFireBaseSuccess(Uri uri);

    void uploadFireBaseFailure();
}
