package it.sii.android.icaro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener,
		OnAddGeofencesResultListener {

	SharedPreferences mPrefs;
	SharedPreferences.Editor mEditor;

	// il LocationClient gestisce i Callback della classe
	LocationClient mLocationClient;
	boolean mUpdatesRequested;

	// Variabile per mantenere la posizione corrente
	Location mCurrentLocation;

	// Define an object that holds accuracy and frequency parameters
	LocationRequest mLocationRequest;

	// Milliseconds per second
	private static final int MILLISECONDS_PER_SECOND = 1000;
	// Update frequency in seconds
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	// Update frequency in milliseconds
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	// A fast frequency ceiling in milliseconds
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				/*
				 * Try the request again
				 */

				break;
			}

		}
	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {

			GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0).show();

			return false;
		}
	}

	MapView mMapView;
	GoogleMap mMap;

	MenuItem mNormalItem = null;
	MenuItem mSatelliteItem = null;
	MenuItem mTerrainItem = null;
	MenuItem mHybridItem = null;
	String lastMarkerClicked = null;

	LatLng chosenLatLng;

	private static final LatLng ANCONA = new LatLng(43.607602, 13.497654);
	private static final LatLng BOLOGNA = new LatLng(44.50614, 11.343411);
	private static final LatLng FIRENZE = new LatLng(43.776894, 11.247373);
	private static final LatLng MILANO_PG = new LatLng(45.485032, 9.187585);
	private static final LatLng MILANO_R = new LatLng(45.433364, 9.238343);
	private static final LatLng NAPOLI = new LatLng(40.852934, 14.272907);
	private static final LatLng PADOVA = new LatLng(45.417846, 11.880794);
	private static final LatLng PESARO = new LatLng(43.906436, 12.903699);
	private static final LatLng REGGIOEMILIA = new LatLng(44.697793, 10.643093);
	private static final LatLng RIMINI = new LatLng(44.064241, 12.574332);
	private static final LatLng ROMA_OS = new LatLng(41.872778, 12.483893);
	private static final LatLng ROMA_T = new LatLng(41.911757, 12.5305);
	private static final LatLng SALERNO = new LatLng(40.67514, 14.772822);
	private static final LatLng TORINO = new LatLng(45.06987, 7.66477);
	private static final LatLng VENEZIA_M = new LatLng(45.482123, 12.232069);
	private static final LatLng VENEZIA_SL = new LatLng(45.441393, 12.320459);
	private static final LatLng ITALIA = new LatLng(41.29246, 12.5736108);

	public static final HashMap<String, LatLng> CoorDict = new HashMap<String, LatLng>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("Ancona", ANCONA);
			put("Bologna Centrale", BOLOGNA);
			put("Firenze SMN", FIRENZE);
			put("Milano PG", MILANO_PG);
			put("Milano Rogoredo", MILANO_R);
			put("Napoli Centrale", NAPOLI);
			put("Padova", PADOVA);
			put("Pesaro", PESARO);
			put("Reggio Emilia Mediopadana", REGGIOEMILIA);
			put("Rimini", RIMINI);
			put("Roma Ostiense", ROMA_OS);
			put("Roma Tiburtina", ROMA_T);
			put("Salerno", SALERNO);
			put("Torino PS", TORINO);
			put("Venezia Mestre", VENEZIA_M);
			put("Venezia santa Lucia", VENEZIA_SL);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_mapview);

		mMapView = (MapView) findViewById(R.id.mymapview);
		mMapView.onCreate(savedInstanceState);

		MapsInitializer.initialize(this);

		if (mMap == null) {
			mMap = mMapView.getMap();
			if (mMap != null) {
			}
		}

		// abilita la posizione dell'utente sulla mappa
		mMap.setMyLocationEnabled(true);
		// toglie la possibilità di vedere i palazzi in 3D
		mMap.setBuildingsEnabled(false);
		// setta tutti i marker sulle stazioni
		mMap.addMarker(new MarkerOptions().position(ANCONA).title("Ancona"));
		mMap.addMarker(new MarkerOptions().position(BOLOGNA).title("Bologna"));
		mMap.addMarker(new MarkerOptions().position(FIRENZE).title(
				"Firenze SMN"));
		mMap.addMarker(new MarkerOptions().position(MILANO_PG).title(
				"Milano PG"));
		mMap.addMarker(new MarkerOptions().position(MILANO_R).title(
				"Milano Rogoredo"));
		mMap.addMarker(new MarkerOptions().position(NAPOLI).title("Napoli"));
		mMap.addMarker(new MarkerOptions().position(PADOVA).title("Padova"));
		mMap.addMarker(new MarkerOptions().position(PESARO).title("Pesaro"));
		mMap.addMarker(new MarkerOptions().position(REGGIOEMILIA).title(
				"Reggio Emilia"));
		mMap.addMarker(new MarkerOptions().position(RIMINI).title("Rimini"));
		mMap.addMarker(new MarkerOptions().position(ROMA_OS).title(
				"Roma Ostiense"));
		mMap.addMarker(new MarkerOptions().position(ROMA_T).title(
				"Roma Tiburtina"));
		mMap.addMarker(new MarkerOptions().position(SALERNO).title("Salerno"));
		mMap.addMarker(new MarkerOptions().position(TORINO).title("Torino PS"));
		mMap.addMarker(new MarkerOptions().position(VENEZIA_M).title(
				"Venezia Mestre"));
		mMap.addMarker(new MarkerOptions().position(VENEZIA_SL).title(
				"Venezia S.Lucia"));

		mLocationClient = new LocationClient(this, this, this);

		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create();
		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval to 5 seconds
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		// Set the fastest update interval to 1 second
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

		// Open the shared preferences

		mPrefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
		// Get a SharedPreferences editor
		mEditor = mPrefs.edit();
		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		mLocationClient = new LocationClient(this, this, this);
		// Start with updates turned off
		mUpdatesRequested = false;

		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				if (arg0.getTitle().equals(lastMarkerClicked)) {

					showAddFenceDialog(arg0.getPosition());

					// arg0.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
					return true;
				}
				lastMarkerClicked = arg0.getTitle();
				return false;
			}
		});

		startService(new Intent(this, ReceiveTransitionsIntentService.class));

	}

	public void showAddFenceDialog(final LatLng latLng) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Servizio di notifica");
		builder.setMessage("Abilitare una notifica poco prima di raggiungere la destinazione?");
		builder.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				addGeoFence(latLng);
			}
		});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// non fa nulla
					}
				});
		builder.show();
	}

	public void addGeoFence(LatLng latLon) {
		chosenLatLng = latLon;

		if (mLocationClient == null) {
			Toast.makeText(this, "Unable to add fence", 100);
		}

		if (mLocationClient.isConnected()) {
			updateGeofence();
		} else if (servicesConnected()) {
			mLocationClient.connect();
		} else {
			Toast.makeText(this, "Unable to add fence", 100);
		}
	}

	public void updateGeofence() {
		float RADIUS = 100000;
		if (chosenLatLng == null) {
			Log.i("ICARO geofence", "no chosenLatLng");
			return;
		}
		int ONE_DAY = 24 * 60 * 60 * 1000;
		Geofence geofence = (new Geofence.Builder())
				.setCircularRegion(chosenLatLng.latitude,
						chosenLatLng.longitude, RADIUS)
				.setRequestId("id")
				.setExpirationDuration(ONE_DAY)
				.setLoiteringDelay(500)
				.setTransitionTypes(
						Geofence.GEOFENCE_TRANSITION_ENTER
								| Geofence.GEOFENCE_TRANSITION_DWELL).build();
		List<Geofence> fences = new ArrayList<>();
		fences.add(geofence);
		Intent serviceIntent = new Intent();
		serviceIntent
				.setAction("it.sii.android.icaro.ReceiveTransitionsIntentService");
		mLocationClient.addGeofences(fences, PendingIntent.getService(this, 1,
				serviceIntent, PendingIntent.FLAG_ONE_SHOT), this);
		chosenLatLng = null;
	}

	@Override
	protected void onStart() {

		// Connect the client.
		if (servicesConnected()) {
			mLocationClient.connect();
		}
		super.onStart();
	}

	@Override
	public void onResume() {

		mMapView.onResume();
		if (mMap == null) {
			mMap = mMapView.getMap();
			if (mMap != null) {
			}
		}

		/*
		 * Get any previous setting for location updates Gets "false" if an
		 * error occurs
		 */
		if (mPrefs.contains("KEY_UPDATES_ON")) {
			mUpdatesRequested = mPrefs.getBoolean("KEY_UPDATES_ON", false);

			// Otherwise, turn off location updates
		} else {
			mEditor.putBoolean("KEY_UPDATES_ON", false);
			mEditor.commit();
		}
		super.onResume();
	}

	@Override
	public void onPause() {

		// Save the current setting for updates
		mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
		mEditor.commit();
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onStop() {

		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onDestroy() {

		mMapView.onDestroy();

		// TODO non vorrei che l'Update finisse una volta che muore l'Activity
		// ma l'intera App
		if (mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(this);
		}
		super.onDestroy();
	}

	public void centerMap(View Button) {
		if (mMap != null) {
			Log.i("ControlledMapTest", "centerMap");
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ITALIA, 5),
					2000, null);
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		int firstItem = Menu.FIRST;
		mSatelliteItem = menu.add(1, firstItem, firstItem, "Satellite");
		mSatelliteItem.setCheckable(true);
		mTerrainItem = menu.add(1, firstItem + 1, firstItem + 1, "Terrain");
		mTerrainItem.setCheckable(true);
		mHybridItem = menu.add(1, firstItem + 2, firstItem + 2, "Hybrid");
		mHybridItem.setCheckable(true);
		mNormalItem = menu.add(1, firstItem + 3, firstItem + 3, "Normal");
		mNormalItem.setCheckable(true);
		mNormalItem.setChecked(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mSatelliteItem.setChecked(false);
		mTerrainItem.setChecked(false);
		mHybridItem.setChecked(false);
		mNormalItem.setChecked(false);

		item.setChecked(!item.isChecked());
		switch (item.getItemId()) {
		case Menu.FIRST:
			mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		case Menu.FIRST + 1:
			mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
		case Menu.FIRST + 2:
			mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case Menu.FIRST + 3:
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			Toast.makeText(this, "err " + connectionResult.getErrorCode(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		// Display the connection status
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		// If already requested, start periodic updates
		if (mUpdatesRequested) {
			mLocationClient.requestLocationUpdates(mLocationRequest, this);
		}

		mCurrentLocation = mLocationClient.getLastLocation();
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onLocationChanged(Location location) {
		// Report to the UI that the location was updated
		String msg = "Updated Location: "
				+ Double.toString(location.getLatitude()) + ","
				+ Double.toString(location.getLongitude());
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onAddGeofencesResult(int arg0, String[] arg1) {
		Log.i("ICARO FENCE", "New fence was added!");
	}

}
