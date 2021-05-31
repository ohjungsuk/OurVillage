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

import java.util.ArrayList;

import static com.ajou.ourvillage.Tasty.TastyFragment.locationX;
import static com.ajou.ourvillage.Tasty.TastyFragment.locationY;

public class TastyAllMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView = null;
    private GoogleMap mMap;
    private FloatingActionButton mBackBtn;
    private String latitude, longitude;
    private ArrayList<TastyPostItem> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasty_all_map);

        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.tasty_all_map);
        mapFragment.getMapAsync(this); //getMapAsync must be called on the main thread.

        mBackBtn = findViewById(R.id.tasty_all_btn_back);
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

        LatLng startPoint = new LatLng(37.2814443, 127.0441587);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startPoint));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12)); // 확대

        for (int i = 0; i < locationX.size(); i++) {
            mMap.addMarker(markerOptions.title("").position(new LatLng(locationX.get(i), locationY.get(i))));
        }

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