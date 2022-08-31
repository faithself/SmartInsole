package de.kai_morich.simple_usb_terminal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import de.kai_morich.simple_usb_terminal.databinding.FragmentMapBinding;

public class MapFragment extends Fragment {

    private FragmentMapBinding mapBinding;
    private AMap aMap;
    MapView mMapView = null;
    private LocationManager locationManager;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , 0);
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION} , 0);
        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mapBinding = FragmentMapBinding.inflate(inflater, container, false);
        mMapView = (MapView) mapBinding.mapView;
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300, 8, new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(String provider) {
                // 使用GPRS提供的定位信息来更新位置
                updatePosition(locationManager.getLastKnownLocation(provider));
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                updatePosition(location);
            }
        });

        ToggleButton tb = (ToggleButton)mapBinding.tb;
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                }else{
                    aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                }
            }

        });
        return mapBinding.getRoot();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    private void updatePosition(Location location){
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        //创建一个设置经纬度的CameraUpdate
        CameraUpdate cu = CameraUpdateFactory.changeLatLng(pos);
        //更新地图的显示区域
        aMap.moveCamera(cu);
        //清除所有的Marker等覆盖物
        aMap.clear();
        //创建一个MarkerOptions对象
        MarkerOptions markOptions = new MarkerOptions();
        markOptions.position(pos);
        //添加MarkerOptions（实际上是添加Marker）
        Marker marker = aMap.addMarker(markOptions);
    }
}