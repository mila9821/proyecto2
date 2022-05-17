package com.ditec.milocalizacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity  extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mapa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            Toast.makeText(this, "GPS available", Toast.LENGTH_LONG).show();
        else Toast.makeText(this, "GPS not available", Toast.LENGTH_LONG).show();
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (checkseguir == 1) {
                    Toast.makeText(getApplicationContext(), "Se cambio de posicion", Toast.LENGTH_SHORT).show();
                    Double latitude = location.getLatitude();
                    Double longitude = location.getLongitude();
                    Toast.makeText(getApplicationContext(), "latitud: " + latitude.toString() +
                            " longitud: " + longitude.toString(), Toast.LENGTH_SHORT).show();
                    mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
                }
            }
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            public void onProviderEnabled(String provider) { }
            public void onProviderDisabled(String provider) { }
        };
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-18.013766, -70.255331), 15));
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(false);
            mapa.getUiSettings().setCompassEnabled(true);
        } else {
            Button btnMiPos=(Button) findViewById(R.id.btnmiubi);
            btnMiPos.setEnabled(false);
        }
    }
    public void miubicacion(View view) {
        if (mapa.getMyLocation() != null)
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mapa.getMyLocation().getLatitude(),
                            mapa.getMyLocation().getLongitude()), 17));
    }
    int checkseguir=0;
    public void Seguir(View view) {
        Button btnseguir=findViewById(R.id.btnseguir);
        if (checkseguir==0) {
            checkseguir = 1;
            btnseguir.setText("OFF seguir");
            Toast.makeText(this, "Se Activo el seguimiento", Toast.LENGTH_SHORT).show();
        }
        else{
            checkseguir = 0;
            btnseguir.setText("ON seguir");
            Toast.makeText(this, "Se desactivo el seguimiento", Toast.LENGTH_SHORT).show();
        }
    }
    public void fusedlocation(View view) {
        startActivity(new Intent(this, MiFusedLocation.class));
    }

    public void fusedlocationprovider(View view) {
        startActivity(new Intent(this, MiFusedLocationClient.class));
    }

    public void markerdirection(View view) {
        startActivity(new Intent(this, MarkerDirection.class));
    }
}