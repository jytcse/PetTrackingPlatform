package com.example.pettrackingplatform.ui.home;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pettrackingplatform.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mMapView;
    private List<Marker> markerList = new ArrayList<>();
    private Polygon polygon;
    //建立View時，先檢查API_KEY是否存在，沒有的話提示使用者，不然google map 跑不出來
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // 檢查 API 金鑰是否存在
        if (!checkApiKey()) {
            // 顯示提示給使用者
            Toast.makeText(getActivity(), "找不到API_KEY 請確認Manifest.xml與local.properties", Toast.LENGTH_LONG).show();
        } else {
            // API 金鑰存在，初始化地圖
            mMapView = rootView.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
        }


        return rootView;
    }
    // 檢查 API 金鑰是否存在
    private boolean checkApiKey() {
        try {
            ApplicationInfo appInfo = getActivity().getPackageManager().getApplicationInfo(
                    getActivity().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            if (bundle != null) {
                String apiKey = bundle.getString("com.google.android.geo.API_KEY");
                return apiKey != null && !apiKey.isEmpty();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 當地圖準備好
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 移動地圖視角至特定座標（雲科大）
        LatLng yuntechLatLng = new LatLng(23.694574183698478, 120.53449681325017);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yuntechLatLng, 16f));

        // 添加長按地圖事件監聽器 標記地點
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // 新增標記
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
                // 將標記加入清單
                markerList.add(marker);
                // 清除所有的多邊形
                // 如果標記數量為3，則創建多邊形
                if (markerList.size() >= 3) {
                    drawPolygon();
                }
            }
        });

        // 添加點擊標記事件監聽器 移除標記
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // 移除標記
                marker.remove();
                // 從清單中刪除標記
                markerList.remove(marker);

                // 清除所有的多邊形
                clearPolygon();


                // 如果標記數量>3，則重新繪製多邊形
                if (markerList.size() >= 3) {
                    drawPolygon();
                }
                return true;
            }
        });

        // 設置地圖樣式
//        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));

    }
    // 清除所有的多邊形
    private void clearPolygon() {
        if (polygon != null) {
            polygon.remove();
            polygon = null;
        }
    }

    // 創建並顯示多邊形
    private void drawPolygon() {
        // 清除上次的多邊形
        clearPolygon();

        // 創建多邊形
        PolygonOptions polygonOptions = new PolygonOptions();
        // 對標記點按照經度進行排序
        markerList.sort(new Comparator<Marker>() {
            @Override
            public int compare(Marker marker1, Marker marker2) {
                return Double.compare(marker1.getPosition().longitude, marker2.getPosition().longitude);
            }
        });
        // 將排序後的標記點添加到多邊形
        for (Marker marker : markerList) {
            polygonOptions.add(marker.getPosition());
        }
        // 添加第一個標記點，以封閉多邊形
        polygonOptions.add(markerList.get(0).getPosition());

        // 設置多邊形的填充顏色
        polygonOptions.fillColor(Color.argb(100, 0, 255, 0)); // 綠色，透明度為100

        polygon = mMap.addPolygon(polygonOptions);
    }
    // 取得標記清單
    public List<Marker> getMarkerList() {
        return markerList;
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
