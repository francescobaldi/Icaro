package it.sii.android.icaro;

import java.util.Calendar;

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
	
	@Override
	public void onWindowFocusChanged (boolean hasFocus) {
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter_1 = ArrayAdapter.createFromResource(this, R.array.stazioni_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter_1);
		spinner.setOnItemSelectedListener(this);
		
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
	public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
	 TextView v =  	(TextView) arg1;
      Log.d( "TESTO", v.getText().toString());
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
