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
import java.util.Stack;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mMapView;
    private List<Marker> markerList = new ArrayList<>();
    private Polygon polygon;

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
                // 如果標記數量>=3，則創建多邊形
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
                // 如果標記數量>=3，則重新繪製多邊形
                if (markerList.size() >= 3) {
                    drawPolygon();
                }
                return true;
            }
        });
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

        // 將標記點轉換為LatLng點的清單
        List<LatLng> points = new ArrayList<>();
        for (Marker marker : markerList) {
            points.add(marker.getPosition());
        }
        // 計算凸包點
        List<LatLng> convexHull = getConvexHull(points);

        // 用凸包點創建多邊形
        PolygonOptions polygonOptions = new PolygonOptions();
        for (LatLng point : convexHull) {
            polygonOptions.add(point);
        }
        // 區域填充顏色
        polygonOptions.fillColor(Color.argb(100, 0, 255, 0));
        polygon = mMap.addPolygon(polygonOptions);
    }

    // 使用 Graham Scan 算法計算凸包
    private List<LatLng> getConvexHull(List<LatLng> points) {
        // 如果點的數量少於3，直接返回這些點（無法構成多邊形）
        if (points.size() < 3) return points;

        // 找到最底下且最左邊的點，作為算法的起點（pivot）
        LatLng pivot = points.get(0);
        for (LatLng point : points) {
            if (point.latitude < pivot.latitude || (point.latitude == pivot.latitude && point.longitude < pivot.longitude)) {
                pivot = point;
            }
        }

        final LatLng finalPivot = pivot;

        // 按照與 pivot 的極角（polar angle）對所有點進行排序
        points.sort(new Comparator<LatLng>() {
            @Override
            public int compare(LatLng a, LatLng b) {
                double angleA = Math.atan2(a.latitude - finalPivot.latitude, a.longitude - finalPivot.longitude);
                double angleB = Math.atan2(b.latitude - finalPivot.latitude, b.longitude - finalPivot.longitude);
                return Double.compare(angleA, angleB);
            }
        });

        // 使用Stack來存儲凸包的點
        Stack<LatLng> hull = new Stack<>();
        // 將排序後的前兩個點推入堆疊中
        hull.push(points.get(0));
        hull.push(points.get(1));

        // 處理剩餘的點
        for (int i = 2; i < points.size(); i++) {
            LatLng top = hull.pop(); // 彈出堆疊頂部的元素
            // 如果新的點和堆疊頂部的前一個點與 top 構成的轉折不是逆時針方向（不構成凸包的一部分），則繼續彈出堆疊頂部的元素
            while (!isCounterClockwise(hull.peek(), top, points.get(i))) {
                top = hull.pop();
            }
            // 將合法的 top 重新推入堆疊中
            hull.push(top);
            // 將新的點推入堆疊中
            hull.push(points.get(i));
        }

        // 返回存儲凸包點的堆疊轉換為的列表
        return new ArrayList<>(hull);
    }

    // 檢查三個點是否形成逆時針方向
    // 如果從 a 到 b 到 c 的轉折是逆時針方向，返回 true
    private boolean isCounterClockwise(LatLng a, LatLng b, LatLng c) {
        return (b.longitude - a.longitude) * (c.latitude - a.latitude) > (b.latitude - a.latitude) * (c.longitude - a.longitude);
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
