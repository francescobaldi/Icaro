package it.sii.android.icaro;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TrainResults extends ActionBarActivity implements
		OnItemClickListener {

	List<Treno> treni = new ArrayList<>();
	ListAdapter customAdapter;
	TextView data;
	String data1;
	String data2;
	String partenza;
	String arrivo;
	String orario1a;
	String orario1b;
	String orario2a;
	String orario2b;
	String passeggeri;
	Treno trenoAndata;
	Treno trenoRitorno;
	boolean andata = true;
	boolean soloAndata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_train_results);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		// prendo il Bundle passato dall'intent di BuyTickets
		Bundle b = new Bundle();
		b = getIntent().getExtras();
		partenza = b.getString("partenza");
		arrivo = b.getString("arrivo");
		orario1a = b.getString("orario1a");
		orario1b = b.getString("orario1b");
		orario2a = b.getString("orario2a");
		orario2b = b.getString("orario2b");
		passeggeri = b.getString("passeggeri");
		data1 = b.getString("data1");
		data2 = b.getString("data2");
		soloAndata = b.getBoolean("soloAndata");

		data = (TextView) findViewById(R.id.dataView);
		data.setText(this.data1);
		TextView passeggeriView = (TextView) findViewById(R.id.passeggeri);
		passeggeriView.setText(this.passeggeri + " passeggero/i");

		aggiornaLista(partenza, arrivo, orario1a, orario1b, passeggeri, data1);

		ListView listView = (ListView) findViewById(R.id.arrayList);
		customAdapter = new ListAdapter(this, R.layout.train_view, treni);
		listView.setAdapter(customAdapter);
		listView.setOnItemClickListener(this);
		andata = true;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.train_results, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_train_results,
					container, false);
			return rootView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
			long arg3) {
		AlertDialog.Builder builder = new AlertDialog.Builder(TrainResults.this);
		builder.setTitle(R.string.booking_title);
		builder.setMessage(R.string.booking_message);
		builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				Thread set = new Thread(new Runnable() {

					@Override
					public void run() {

						onTrainSelected(treni.get(position));

					}
				});
				set.start();

			}
		});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});
		builder.show();

	}

	public void onTrainSelected(Treno t) {
		if (andata) {
			trenoAndata = t;
		} else {
			trenoRitorno = t;
		}

		if (soloAndata) {
			sendBooking(trenoAndata, data1);
			showBookingDone();
		} else if (!soloAndata && andata) {
			aggiornaLista(arrivo, partenza, orario2a, orario2b, passeggeri,
					data2);

		} else if (!soloAndata && !andata) {
			sendBooking(trenoAndata, data1);
			sendBooking(trenoRitorno, data2);
			showBookingDone();
		}

		andata = !andata;
	}

	public void sendBooking(Treno t, String data) {
		// prendo l'id utente per usarlo con la prenotazione
		String id = null;
		SharedPreferences settings = getSharedPreferences("datiLogin", 0);
		id = settings.getString("_id", "");

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("treno", t.Treno));
		nameValuePairs.add(new BasicNameValuePair("data", data));
		nameValuePairs.add(new BasicNameValuePair("user_id", id));
		nameValuePairs.add(new BasicNameValuePair("passeggeri", t.Passeggeri));

		// Connessione al Server e richiesta al DB tramite prenotazioni.php
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLS.PRENOTAZIONI);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() != 200) {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		DatabaseManager dbHelper = new DatabaseManager(this, "icaro", null,
				DatabaseManager.DB_VERSION);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cValues = new ContentValues();
		cValues.put("Treno", t.Treno);
		cValues.put("StazionePartenza", t.StazionePartenza);
		cValues.put("StazioneArrivo", t.StazioneArrivo);
		cValues.put("OrarioPartenza", t.OrarioPartenza);
		cValues.put("OrarioArrivo", t.OrarioArrivo);
		cValues.put("Data", t.Data);
		cValues.put("Passeggeri", t.Passeggeri);
		db.insert("treno", null, cValues);
		db.close();

	}

	public void showBookingDone() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(),
						"prenotazione riuscita", Toast.LENGTH_LONG).show();

			}
		});
		this.gotoMain(null);
	}

	public void aggiornaLista(final String partenza, final String arrivo,
			String ora1, String ora2, final String passeggeri, final String data) {
		Thread t = new Thread(new Runnable() {
			public void run() {

				// array per la chiamata http
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs
						.add(new BasicNameValuePair("partenza", partenza));
				nameValuePairs.add(new BasicNameValuePair("arrivo", arrivo));
				nameValuePairs
						.add(new BasicNameValuePair("orario1a", orario1a));
				nameValuePairs
						.add(new BasicNameValuePair("orario2a", orario2a));
				nameValuePairs
						.add(new BasicNameValuePair("orario1b", orario1b));
				nameValuePairs
						.add(new BasicNameValuePair("orario2b", orario2b));
				InputStream is;
				String result = null;

				// Connessione al Server e richiesta al DB tramite treni.php
				try {
					DefaultHttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(URLS.TRENI);
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					if (response.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "UTF-8"), 8);
						StringBuilder sb = new StringBuilder();

						String line = null;
						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						is.close();
						result = sb.toString();

						Gson GsonIstance = new Gson();
						// viene creata una lista temporanea "tmp" con GSon e
						// poi tutti i record vengono aggiunti alla lista
						// "treni"
						List<Treno> tmp = GsonIstance.fromJson(result,
								new TypeToken<List<Treno>>() {
								}.getType());
						// per ogni record aggiungo i dati mancanti dalla query
						// SQL
						for (Treno t : tmp) {
							t.StazionePartenza = partenza;
							t.StazioneArrivo = arrivo;
							t.Data = data;
							t.Passeggeri = passeggeri;
						}

						treni.clear();
						treni.addAll(tmp);

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								customAdapter.notifyDataSetChanged();

							}
						});

						Log.v("LOG", treni.toString());

					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		});
		t.start();

	}

	public void gotoMain(View view) {
		Intent intent = new Intent(this, MyTravels.class);
		startActivity(intent);
	}
}
