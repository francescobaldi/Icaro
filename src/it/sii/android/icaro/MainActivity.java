package it.sii.android.icaro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	protected void onStart() {
		super.onStart();

		String mEmail = null;

		// se l'uente è loggato la mail è salvata nelle SharedPreferences
		SharedPreferences settings = getSharedPreferences("datiLogin", 0);
		mEmail = settings.getString("Email", "");
		if (mEmail != "") {
			findViewById(R.id.searchButton).setVisibility(View.VISIBLE);
			findViewById(R.id.myTravelsButton).setVisibility(View.VISIBLE);
			findViewById(R.id.newsButton).setVisibility(View.VISIBLE);
			findViewById(R.id.mapButton).setVisibility(View.VISIBLE);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == LOGIN_ACTIVITY_REQUEST) {
			findViewById(R.id.searchButton).setVisibility(View.VISIBLE);
			findViewById(R.id.myTravelsButton).setVisibility(View.VISIBLE);
			findViewById(R.id.newsButton).setVisibility(View.VISIBLE);
			findViewById(R.id.mapButton).setVisibility(View.VISIBLE);
		}
	}

	public static int LOGIN_ACTIVITY_REQUEST = 1;

	public void gotoLogin(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST);
	}

	public void gotoSearch(View view) {
		Intent intent = new Intent(this, BuyTickets.class);
		startActivity(intent);
	}

	public void gotoTravels(View view) {
		Intent intent = new Intent(this, MyTravels.class);
		startActivity(intent);
	}

	public void gotoNews(View view) {
		Intent intent = new Intent(this, News.class);
		startActivity(intent);
	}

	public void gotoMap(View view) {
		Intent intent = new Intent(this, Map.class);
		startActivity(intent);
	}
}
