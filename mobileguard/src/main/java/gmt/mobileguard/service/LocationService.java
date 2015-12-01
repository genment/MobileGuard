package gmt.mobileguard.service;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;

import gmt.mobileguard.util.SecurityUtil;

public class LocationService extends Service implements LocationListener {

    private LocationManager locationManager;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String bestProvider = locationManager.getBestProvider(getCriteria(), true);
        bestProvider = bestProvider != null ? bestProvider : LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(bestProvider, 0, 0, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

    /**
     * 获取最佳提供者的基准
     *
     * @return 基准
     */
    @NonNull
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);                // 付费。如3G网络定位
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 精度。
        criteria.setAltitudeRequired(false);          // 海拔。
        criteria.setBearingRequired(false);           // 方向。
        return criteria;
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "成功通过 " + location.getProvider() + " 获取到位置：" +
                "经度：" + location.getLongitude() +
                "，纬度：" + location.getLatitude() +
                "，精度范围：" + location.getAccuracy();
        SecurityUtil.sendSecuritySms(msg);
        stopSelf();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
