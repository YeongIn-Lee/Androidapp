package com.example.mysns;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.Tm128;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.MapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storelocation extends AppCompatActivity implements OnMapReadyCallback {

    private  static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private String clientId = "vtOYrv8OWZUoEGbC3Unk";
    private String clientSecret = "lGpE7aUCew";
    private String text2;
    location locationData = new location();
    RequestQueue queue;
    RequestQueue queue2;
    //final Geocoder geocoder = new Geocoder(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        text2 = getIntent().getStringExtra("find2");

        Log.d("text=>","text="+text2);
        queue = Volley.newRequestQueue(this);
        queue2 = Volley.newRequestQueue(this);


        FragmentManager fm =getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if(mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if(!locationSource.isActivated()){
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        getlocation();
    }

    public void getlocation(){

        String url = "https://openapi.naver.com/v1/search/local?query=" + text2;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);

                            JSONArray arraylocation= jsonObj.getJSONArray("items");;

                            for(int i = 0, j = arraylocation.length(); i<j; i++){
                                JSONObject obj = arraylocation.getJSONObject(i);

                                Log.d("location =>", obj.toString());
                                locationData.setX(obj.getString("mapx"));
                                locationData.setY(obj.getString("mapy"));
                                locationData.setAddress(obj.getString("address"));


                                Log.d("value=>","value_x"+locationData.getX());
                                Log.d("value=>","value_y"+locationData.getY());
                                Log.d("value=>","address"+locationData.getAddress());

                                Tm128 tm128 = new Tm128(Double.parseDouble(locationData.getX()), Double.parseDouble(locationData.getY()));
                                LatLng latLng = tm128.toLatLng();

                                Marker marker = new Marker();
                                marker.setPosition(new LatLng(latLng.latitude, latLng.longitude));

                                marker.setMap(naverMap);
                                marker.setWidth(50);
                                marker.setHeight(70);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error=>", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("X-Naver-Client-Id", clientId);
                params.put("X-Naver-Client-Secret", clientSecret);
                Log.d("getHeader =>",""+params);
                get(url,params);
                return params;
            }

            public String get(String apiUrl, Map<String, String> requestHeaders){
                HttpURLConnection con = connect(apiUrl);
                try {
                    con.setRequestMethod("GET");
                    for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                        con.setRequestProperty(header.getKey(), header.getValue());
                    }

                    int responseCode = con.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                        return readBody(con.getInputStream());
                    } else { // 에러 발생
                        return readBody(con.getErrorStream());
                    }
                } catch (IOException e) {
                    throw new RuntimeException("API 요청과 응답 실패", e);
                } finally {
                    con.disconnect();
                }
            }

            public HttpURLConnection connect(String apiUrl){
                try {
                    URL url = new URL(apiUrl);
                    return (HttpURLConnection)url.openConnection();
                } catch (MalformedURLException e) {
                    throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
                } catch (IOException e) {
                    throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
                }
            }

            public String readBody(InputStream body){
                InputStreamReader streamReader = new InputStreamReader(body);

                try (BufferedReader lineReader = new BufferedReader(streamReader)) {
                    StringBuilder responseBody = new StringBuilder();

                    String line;
                    while ((line = lineReader.readLine()) != null) {
                        responseBody.append(line);
                    }

                    return responseBody.toString();
                } catch (IOException e) {
                    throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
                }
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
