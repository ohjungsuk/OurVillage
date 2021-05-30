package com.ajou.ourvillage.Tasty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;

import com.ajou.ourvillage.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TastyShowMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView = null;
    private GoogleMap mMap;
    private FloatingActionButton mBackBtn;
    private Double show_latitdue, show_longtidue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasty_show_map);

        SharedPreferences sf = getSharedPreferences("Location", MODE_PRIVATE);
        String latitude = sf.getString("latitude", "37.2814443");
        String longitude = sf.getString("longitude", "127.0441587");

        System.out.println("위도경도" + latitude + " " + longitude);

        show_latitdue = Double.valueOf(latitude);
        show_longtidue = Double.valueOf(longitude);

        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //getMapAsync must be called on the main thread.

        mBackBtn = findViewById(R.id.map_btn_back);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.icon_location);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 80, 80, false);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        LatLng startPoint = new LatLng(show_latitdue, show_longtidue);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startPoint));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17)); // 확대

        mMap.addMarker(markerOptions.title("").position(new LatLng(show_latitdue, show_longtidue)));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(marker.getPosition()).zoom(17).bearing(0).tilt(30).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                marker.showInfoWindow();
                return true;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                SharedPreferences sharedPreferences = getSharedPreferences("StationName", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String stationName = marker.getTitle();
                editor.putString("StationName", stationName); // key, value 이용하여 저장
                editor.apply(); // 최종 커밋

            }
        });
    }
}