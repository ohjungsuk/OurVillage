package com.ajou.ourvillage.Community;

import android.net.Uri;

public interface CommunityImageInterface {
    void uploadFireBaseSuccess(Uri uri);

    void uploadFireBaseFailure();
}
