package ui.activity.GoogleMap;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.DreamTeam.HiTop.R;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle; 
import android.support.v4.app.FragmentActivity;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;
public class GoogleMapActivity extends FragmentActivity {
	private GoogleMap mMap;
	private UiSettings mUiSettings;
	@Override 
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_googlemap); 
        setUpMapIfNeeded();
    }
	 @Override
	    protected void onResume() {
	        super.onResume();
	        setUpMapIfNeeded();
	        mMap.setMapType(MAP_TYPE_TERRAIN);
	        mMap.setMyLocationEnabled(true);
	        mUiSettings=mMap.getUiSettings();
	        //设置地图显示指南针
	        mUiSettings.setCompassEnabled(true);
	        //倾斜手势操作
	        mUiSettings.setTiltGesturesEnabled(true);
	    }
	 private void setUpMapIfNeeded() {
	        if (mMap == null) {
	            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapGoogle))
	                    .getMap();
	        }
	    }
}
