package it.sii.android.icaro;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;



public class BuyTickets extends ActionBarActivity implements OnItemSelectedListener {

	private static final String LOG = "Francesco";
	
	//DatePicker. Aggiungo come campi di istanza della activity i componenti textview e button e tre interi che conterranno l'anno, il mese e il giorno selezionati
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
	
	protected void onStart (){
		super.onStart();
		
		final TextView returnTicket = (TextView) findViewById(R.id.textView6);
		returnTicket.setVisibility(View.GONE);
		final LinearLayout layoutReturn = (LinearLayout) findViewById(R.id.layoutReturn);
		layoutReturn.setVisibility(View.GONE);
		final LinearLayout layoutReturn2 = (LinearLayout) findViewById(R.id.layoutReturn02);
		layoutReturn2.setVisibility(View.GONE);
		
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
		
		
		
		//Vengono letti i componenti tramite gli id, viene associato un listener al bottone che apre un dialog e viene impostata come data la data odierna.
		mtextView7 = (TextView) findViewById(R.id.textView7);
		msetButton = (Button) findViewById(R.id.setButton);
		mtextView8 = (TextView) findViewById(R.id.textView8);
		msetButton2 = (Button) findViewById(R.id.setButton2);

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

	    final Calendar c = Calendar.getInstance();
	    mYear = c.get(Calendar.YEAR);
	    mMonth = c.get(Calendar.MONTH);
	    mDay = c.get(Calendar.DAY_OF_MONTH);
	    nYear = c.get(Calendar.YEAR);
	    nMonth = c.get(Calendar.MONTH);
	    nDay = c.get(Calendar.DAY_OF_MONTH);
	    updateDisplay();
				
	}
	
	//La funzione updateDisplay() legge i valori di anno, mese e giorno e scrive nella TextView la data nel formato deciso
	protected void updateDisplay() {
        mtextView7.setText(
            new StringBuilder()
            		.append(mDay).append("/")
            		.append(mMonth + 1).append("/")
                    .append(mYear).append(" "));
        
        mtextView8.setText(
                new StringBuilder()
                		.append(nDay).append("/")        
                		.append(nMonth + 1).append("/")
                        .append(nYear).append(" "));
    }
	
	//Il listener imposta la data quando viene selezionata dall'utente
	protected DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
	            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	            	Calendar rightNow = Calendar.getInstance();
	            	Calendar dateObj1 = Calendar.getInstance();
	            	mYear = year;
	                mMonth = monthOfYear;
	                mDay = dayOfMonth;
	                dateObj1.set(mYear, mMonth, mDay);
	                if(dateObj1.after(rightNow) || dateObj1.equals(rightNow)){
	                updateDisplay();
	                Log.v(LOG, "data ok");
	                }
	                else{
	                	Log.v(LOG, "occhio, data precedente!");
	                	if(!mshowed) { 
	                		mshowed = true;
		                	AlertDialog.Builder builder = new AlertDialog.Builder(BuyTickets.this);
		                	builder.setTitle(R.string.dialog_title);
		                	builder.setMessage(R.string.dialog_message);
		                	builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		                		public void onClick(DialogInterface dialog, int id) {
		                			mshowed = false;
		                        }
		                    });
		                	builder.show();
	                	}
	                }  
	            }
	    };
	    
    protected DatePickerDialog.OnDateSetListener nDateSetListener = new DatePickerDialog.OnDateSetListener() {
            	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            		Calendar dateObj1 = Calendar.getInstance();
	            	Calendar dateObj2 = Calendar.getInstance();
	            	nYear = year;
	                nMonth = monthOfYear;
	                nDay = dayOfMonth;
	                dateObj2.set(nYear, nMonth, nDay);
	                dateObj1.set(mYear, mMonth, mDay);
	                //va settato per bene con l'ora di arrivo, non quella di partenza
	                if(dateObj2.after(dateObj1) || dateObj2.equals(dateObj1)){
	                updateDisplay();
	                Log.v(LOG, "data ok");
	                }
	                else{
	                	Log.v(LOG, "occhio, data precedente!");
	                	if(!nshowed) { 
	                		nshowed = true;
		                	AlertDialog.Builder builder = new AlertDialog.Builder(BuyTickets.this);
		                	builder.setTitle(R.string.dialog_title);
		                	builder.setMessage(R.string.dialog_message2);
		                	builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		                		public void onClick(DialogInterface dialog, int id) {
		                            nshowed = false;
		                        }
		                    });
		                	builder.show();
	                	}
	                }
            	}
    	};
	    
    /*Il dialog crea un oggetto DatePicker associato al listener appena creato. La funzione showDialog() triggera l'esecuzione dell'handler onCreateDialog().
    Il parametro id puo' essere eventualmente utilizzato per aprire popup con un contenuto diverso.*/    
    protected Dialog onCreateDialog(int id) {
    	DatePickerDialog.OnDateSetListener listener;
    	if (id==0) listener = mDateSetListener;
    	else listener = nDateSetListener;
        return new DatePickerDialog(this,
                listener,
                mYear, mMonth, mDay);
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
	
	
}
