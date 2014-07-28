package it.sii.android.icaro;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;

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

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

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

		SecurePreferences preferences = new SecurePreferences(
				MainActivity.this,
				"my-preferences",
				"3CB585140F41F6D9F1A4EFBB8C93BF9E93A4075E9EF1C6ADB156202C97F4F132",
				true);
		mEmail = preferences.getString("Email");

		// se l'utente si era precedentemente loggato, la mail è salvata
		// nelle SecurePreferences.
		// prima le salvavamo nelle Shared normali
		// SharedPreferences settings = getSharedPreferences("datiLogin", 0);
		// mEmail = settings.getString("Email", "");
		// se esiste una mail salvata nelle preferenze allora viene chiamato il
		// metodo logged()
		if (mEmail != null) {
			logged();
		} else {
			notlogged();
		}

	}

	// una volta effettuato il login, si ritorna il Result dalla LoginActivity
	// alla MainActivity settando la visibilità dei bottoni
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == LOGIN_ACTIVITY_REQUEST) {
			logged();
		}
	}

	public static int LOGIN_ACTIVITY_REQUEST = 1;

	public void logged() {

		// setta la TextView all'interno del bottone di logout la mail
		// dell'account connesso (prima si chiamavano le Shared poi abbiamo
		// cambiato.
		// SharedPreferences settings = getSharedPreferences("datiLogin", 0);
		// String mEmail = settings.getString("Email", "");

		SecurePreferences preferences = new SecurePreferences(
				MainActivity.this,
				"my-preferences",
				"3CB585140F41F6D9F1A4EFBB8C93BF9E93A4075E9EF1C6ADB156202C97F4F132",
				true);
		String mEmail = preferences.getString("Email");

		Button logout = (Button) findViewById(R.id.logoutButton);
		logout.setText(mEmail);

		findViewById(R.id.loginButton).setVisibility(View.GONE);
		findViewById(R.id.logoutButton).setVisibility(View.VISIBLE);
		findViewById(R.id.searchButton).setVisibility(View.VISIBLE);
		findViewById(R.id.myTravelsButton).setVisibility(View.VISIBLE);
		findViewById(R.id.newsButton).setVisibility(View.VISIBLE);
		findViewById(R.id.mapButton).setVisibility(View.VISIBLE);
	}

	public void notlogged() {
		findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
		findViewById(R.id.logoutButton).setVisibility(View.GONE);
		findViewById(R.id.searchButton).setVisibility(View.GONE);
		findViewById(R.id.myTravelsButton).setVisibility(View.GONE);
		findViewById(R.id.newsButton).setVisibility(View.GONE);
		findViewById(R.id.mapButton).setVisibility(View.GONE);
	}

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
		Intent intent = new Intent(this, NewsMeteo.class);
		startActivity(intent);
	}

	public void gotoMap(View view) {
		Intent intent = new Intent(this, Map.class);
		startActivity(intent);
	}

	public void logout(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle(R.string.logout_title);
		builder.setMessage(R.string.logout_message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				SharedPreferences settings = getSharedPreferences("datiLogin",
						0);
				SharedPreferences.Editor editor = settings.edit();
				// editor.putString("Email", null);
				editor.putString("_id", null);
				editor.commit();

				SecurePreferences preferences = new SecurePreferences(
						MainActivity.this,
						"my-preferences",
						"3CB585140F41F6D9F1A4EFBB8C93BF9E93A4075E9EF1C6ADB156202C97F4F132",
						true);
				preferences.clear();

				// refresha l'activity
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
		});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});
		builder.show();

	}
}
