package it.sii.android.icaro;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;



public class BuyTickets extends ActionBarActivity implements OnItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_tickets);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	protected void onStart (){
		super.onStart();
		
		ArrayAdapter<String> adapter = createSpinnerAdapter();
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner1.setAdapter(adapter);
		Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner2.setAdapter(adapter);
		
		Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
		String[] data2 = getResources().getStringArray(R.array.passeggeri_array);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, data2);
		spinner4.setAdapter(adapter2);
		
		Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
		Spinner spinner5 = (Spinner) findViewById(R.id.spinner5);
		String[] data3 = getResources().getStringArray(R.array.orari_array);
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, data3);
		spinner3.setAdapter(adapter3);
		spinner5.setAdapter(adapter3);
				
	}
		
	private ArrayAdapter<String> createSpinnerAdapter() {
		String[] data1 = getResources().getStringArray(R.array.stazioni_array);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, data1);
		return arrayAdapter;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buy_tickets, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_buy_tickets,
					container, false);
			return rootView;
			
		}
	}
	
	public void gotoTrainResults(View view) {
		Intent intent = new Intent(this, TrainResults.class);
		startActivity(intent);
	}
	
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
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
		
	}
	
	
	
		
	/*public static class TimePickerFragment extends DialogFragment
								implements TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
		}
		
		public void showTimePickerDialog(View v) {
		    DialogFragment newFragment = new TimePickerFragment();
		    newFragment.show(getSupportFragmentManager(), "timePicker");
		}
		
	}*/
	
	

	
}
