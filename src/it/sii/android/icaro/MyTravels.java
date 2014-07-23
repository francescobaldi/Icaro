package it.sii.android.icaro;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

public class MyTravels extends ActionBarActivity {

	private SQLiteDatabase db;
	private Cursor cursor;
	private CursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_travels);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		DatabaseManager dbHelper = new DatabaseManager(this, "icaro", null,
				DatabaseManager.DB_VERSION);
		db = dbHelper.getWritableDatabase();
		String sql = "SELECT Treno, StazionePartenza, StazioneArrivo, OrarioPartenza, OrarioArrivo, Data, Passeggeri FROM Treno";
		cursor = db.rawQuery(sql, null);

		List<Treno> treni = new ArrayList<Treno>();
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Treno t = new Treno();
				t.Treno = cursor.getString(0);
				t.StazionePartenza = cursor.getString(1);
				t.StazioneArrivo = cursor.getString(2);
				t.OrarioPartenza = cursor.getString(3);
				t.OrarioArrivo = cursor.getString(4);
				t.Data = cursor.getString(5);
				t.Passeggeri = cursor.getString(6);
				treni.add(t);
			} while (cursor.moveToNext());
		}

		ListView listView = (ListView) findViewById(R.id.prenotazioniList);
		ListAdapterPrenotazioni customAdapter = new ListAdapterPrenotazioni(
				this, R.layout.booking_view, treni);
		listView.setAdapter(customAdapter);
		customAdapter.notifyDataSetChanged();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_travels, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_my_travels,
					container, false);
			return rootView;
		}
	}

}
