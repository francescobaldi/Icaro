package it.sii.android.icaro;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class BuyTickets extends ActionBarActivity implements
		OnItemSelectedListener {

	private static final String LOG = "Francesco";

	protected TextView mtextView7;
	protected Button msetButton;
	protected TextView mtextView8;
	protected Button msetButton2;
	protected int mYear;
	protected int mMonth;
	protected int mDay;
	protected int nYear;
	protected int nMonth;
	protected int nDay;
	private Spinner spinner1;
	private Spinner spinner2;
	private Spinner spinner3;
	private Spinner spinner4;
	private Spinner spinner5;
	protected RadioButton onewayRadio;

	Calendar rightNow;
	Calendar dateObj1;
	Calendar dateObj2;

	// dal momento che mi uscivano due AlertDialog faccio il check se è già
	// checkato o meno cosi da farlo uscire solo una volta
	boolean mshowed = false;
	boolean nshowed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_tickets);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	protected void onStart() {
		super.onStart();

		// setto il ritorno non visibile di default
		final TextView returnTicket = (TextView) findViewById(R.id.textView6);
		returnTicket.setVisibility(View.GONE);
		final LinearLayout layoutReturn = (LinearLayout) findViewById(R.id.layoutReturn);
		layoutReturn.setVisibility(View.GONE);
		final LinearLayout layoutReturn2 = (LinearLayout) findViewById(R.id.layoutReturn02);
		layoutReturn2.setVisibility(View.GONE);

		// Adapter vari per gli spinner
		ArrayAdapter<String> adapter = createSpinnerAdapter();
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner1.setAdapter(adapter);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner2.setAdapter(adapter);

		spinner4 = (Spinner) findViewById(R.id.spinner4);
		String[] data2 = getResources()
				.getStringArray(R.array.passeggeri_array);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data2);
		spinner4.setAdapter(adapter2);

		spinner3 = (Spinner) findViewById(R.id.spinner3);
		spinner5 = (Spinner) findViewById(R.id.spinner5);
		String[] data3 = getResources().getStringArray(R.array.orari_array);
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data3);
		spinner3.setAdapter(adapter3);
		spinner5.setAdapter(adapter3);

		mtextView7 = (TextView) findViewById(R.id.textView7);
		msetButton = (Button) findViewById(R.id.setButton);
		mtextView8 = (TextView) findViewById(R.id.textView8);
		msetButton2 = (Button) findViewById(R.id.setButton2);
		onewayRadio = (RadioButton) findViewById(R.id.onewayRadio);

		// Listener per i due bottoni set per la data
		msetButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(0);
			}
		});

		msetButton2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(1);
			}
		});

		// creo un'istanza del calendario
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		nYear = c.get(Calendar.YEAR);
		nMonth = c.get(Calendar.MONTH);
		nDay = c.get(Calendar.DAY_OF_MONTH);
		updateDisplay();

		findViewById(R.id.searchButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						gotoTrainResults(view);
					}
				});

	}

	// La funzione updateDisplay() legge i valori di anno, mese e giorno e
	// scrive nella TextView la data nel formato deciso
	protected void updateDisplay() {
		mtextView7.setText(new StringBuilder().append(mDay).append("/")
				.append(mMonth + 1).append("/").append(mYear).append(" "));

		mtextView8.setText(new StringBuilder().append(nDay).append("/")
				.append(nMonth + 1).append("/").append(nYear).append(" "));
	}

	// Il listener imposta la data quando viene selezionata dall'utente
	protected DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			rightNow = Calendar.getInstance();
			dateObj1 = Calendar.getInstance();
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			dateObj1.set(mYear, mMonth, mDay);

			// la data scelta deve essere >= a quella odierna
			if (dateObj1.after(rightNow) || dateObj1.equals(rightNow)) {
				updateDisplay();
				Log.v(LOG, "data ok");
			} else {
				Log.v(LOG, "occhio, data precedente!");
				if (!mshowed) {
					mshowed = true;
					AlertDialog.Builder builder = new AlertDialog.Builder(
							BuyTickets.this);
					builder.setTitle(R.string.dialog_title);
					builder.setMessage(R.string.dialog_message);
					builder.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									mshowed = false;
								}
							});
					builder.show();
				}
			}
		}
	};

	protected DatePickerDialog.OnDateSetListener nDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			dateObj1 = Calendar.getInstance();
			dateObj2 = Calendar.getInstance();
			nYear = year;
			nMonth = monthOfYear;
			nDay = dayOfMonth;
			dateObj2.set(nYear, nMonth, nDay);
			dateObj1.set(mYear, mMonth, mDay);

			// la data del ritorno deve essere >= a quella di partenza
			if (dateObj2.after(dateObj1) || dateObj2.equals(dateObj1)) {
				updateDisplay();
				Log.v(LOG, "data ok");
			} else {
				Log.v(LOG, "occhio, data precedente!");
				if (!nshowed) {
					nshowed = true;
					AlertDialog.Builder builder = new AlertDialog.Builder(
							BuyTickets.this);
					builder.setTitle(R.string.dialog_title);
					builder.setMessage(R.string.dialog_message2);
					builder.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									nshowed = false;
								}
							});
					builder.show();
				}
			}
		}
	};

	/*
	 * Il dialog crea un oggetto DatePicker associato al listener appena creato.
	 * La funzione showDialog() triggera l'esecuzione dell'handler
	 * onCreateDialog(). Il parametro id puo' essere eventualmente utilizzato
	 * per aprire popup con un contenuto diverso.
	 */
	protected Dialog onCreateDialog(int id) {
		DatePickerDialog.OnDateSetListener listener;
		if (id == 0)
			listener = mDateSetListener;
		else
			listener = nDateSetListener;
		return new DatePickerDialog(this, listener, mYear, mMonth, mDay);
	}

	private ArrayAdapter<String> createSpinnerAdapter() {
		String[] data1 = getResources().getStringArray(R.array.stazioni_array);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data1);
		return arrayAdapter;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.buy_tickets, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_buy_tickets,
					container, false);
			return rootView;

		}
	}

	// conversione di preferenza orario di partenza in base alla posizione nello
	// spinner
	// toHourMin setta il filtro orario al minimo, toHourMax al massimo
	public int toHourMin(int position) {
		// lo switch è come un if con piu else.
		switch (position) {
		case 0:
			return 0;
		case 1:
			return 5;
		case 2:
			return 14;
		case 3:
			return 18;
		default: // nel caso trovasse un case 4
			return 0;
		}

	}

	public int toHourMax(int position) {
		switch (position) {
		case 0:
			return 23;
		case 1:
			return 14;
		case 2:
			return 18;
		case 3:
			return 22;
		default:
			return 23;
		}

	}

	public void gotoTrainResults(View view) {

		int orario1a;
		int orario1b;
		int orario2a;
		int orario2b;

		orario1a = toHourMin(spinner3.getSelectedItemPosition());
		orario1b = toHourMax(spinner3.getSelectedItemPosition());
		orario2a = toHourMin(spinner5.getSelectedItemPosition());
		orario2b = toHourMax(spinner5.getSelectedItemPosition());

		Intent intent = new Intent(this, TrainResults.class);

		// parametri da portare nella prossima Activity
		intent.putExtra("partenza", spinner1.getSelectedItem().toString());
		intent.putExtra("arrivo", spinner2.getSelectedItem().toString());
		intent.putExtra("orario1a", String.valueOf(orario1a));
		intent.putExtra("orario1b", String.valueOf(orario1b));
		intent.putExtra("orario2a", String.valueOf(orario2a));
		intent.putExtra("orario2b", String.valueOf(orario2b));
		intent.putExtra("passeggeri",
				String.valueOf(spinner4.getSelectedItemPosition() + 1));
		intent.putExtra("data1", mtextView7.getText().toString());
		intent.putExtra("data2", mtextView8.getText().toString());
		intent.putExtra("soloAndata", onewayRadio.isChecked());
		startActivity(intent);
	}

	// in base al RaioButton premuto setta degli elementi visibili o meno
	public void noReturn(View view) {
		final TextView returnTicket = (TextView) findViewById(R.id.textView6);
		returnTicket.setVisibility(View.GONE);
		final LinearLayout layoutReturn = (LinearLayout) findViewById(R.id.layoutReturn);
		layoutReturn.setVisibility(View.GONE);
		final LinearLayout layoutReturn2 = (LinearLayout) findViewById(R.id.layoutReturn02);
		layoutReturn2.setVisibility(View.GONE);
	}

	public void yesReturn(View view) {
		final TextView returnTicket = (TextView) findViewById(R.id.textView6);
		returnTicket.setVisibility(View.VISIBLE);
		final LinearLayout layoutReturn = (LinearLayout) findViewById(R.id.layoutReturn);
		layoutReturn.setVisibility(View.VISIBLE);
		final LinearLayout layoutReturn2 = (LinearLayout) findViewById(R.id.layoutReturn02);
		layoutReturn2.setVisibility(View.VISIBLE);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

}
