package com.angelhack.aedlocator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by AsTex on 4/3/2016.
 */
public final class AedFinder {
    public void FindNearestAed(GoogleMap googleMap, double currentLattitude, double currentLongtitude, HashMap<Marker, Integer> map){
        MarkerOptions mo = new MarkerOptions()
                .position(new LatLng(currentLattitude+0.00001, currentLongtitude+0.00002))
                .title("Aed at Innopolis University, 3rd floor, library");
        Marker m = googleMap.addMarker(mo);
        map.put(m,1);
    }
    public void EnableAED(int id){
        HashMap<String,String> params = new HashMap<>();
        WebAPIWorker.get("http://cancerlab.pro/api/EnableAED.ashx", new RequestParams(params), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
