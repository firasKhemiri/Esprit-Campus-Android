package com.esprit.firas.espritcampus.Tools;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esprit.firas.espritcampus.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    Double lat;
    Double lng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_frag, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately




        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                Bundle bundle= getArguments();
                if (bundle!= null) {// to avoid the NullPointerException
                    lat = getArguments().getDouble("lat");
                    lng = getArguments().getDouble("lng");
                    String locationName = getArguments().getString("location");
                //    Toast.makeText(getContext(), lat + " " + lng, Toast.LENGTH_LONG).show();

                    // For showing a move to my location button
                    googleMap.setMyLocationEnabled(true);

                    // For dropping a marker at a point on the Map
                    LatLng location = new LatLng(lat, lng);
                    googleMap.addMarker(new MarkerOptions().position(location).title(locationName));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}