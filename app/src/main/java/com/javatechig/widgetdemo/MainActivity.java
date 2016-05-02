package com.javatechig.widgetdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.pushbots.push.Pushbots;


public class MainActivity extends Activity
{
	String preferencia;
	String estacion = "";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Context contexto = this;
		GetDataFromDB.setDefaults("estacion", "Estacion El Yali", contexto);
		preferencia = GetDataFromDB.getDefaults("estacion", contexto);
		estacion = GetDataFromDB.estacion(preferencia);
		setContentView(R.layout.activity_main);
		Pushbots.sharedInstance().init(this);

		clickimage();


		Spinner staticSpinner = (Spinner) findViewById(R.id.static_spinner);

		// Create an ArrayAdapter using the string array and a default spinner
		ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
				.createFromResource(this, R.array.brew_array, android.R.layout.simple_spinner_item);

		// Specify the layout to use when the list of choices appears
		staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		staticSpinner.setAdapter(staticAdapter);
		staticSpinner.setSelection(getIndex(staticSpinner, GetDataFromDB.getDefaults("estacion", this)));

		staticSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				GetDataFromDB.setDefaults("estacion", (String) parent.getItemAtPosition(position), contexto);
				preferencia = GetDataFromDB.getDefaults("estacion", contexto);
				new Thread(new Runnable()
				{
					public void run()
					{
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								estacion = GetDataFromDB.estacion(preferencia);
								final GetDataFromDB getdb = new GetDataFromDB();
								getdb.getDataFromDB(estacion);
								TextView hora = (TextView) findViewById(R.id.hora);
								hora.setText(getdb.datoSimple("hora"));
								TextView fecha = (TextView) findViewById(R.id.fecha);
								fecha.setText(getdb.datoSimple("fecha"));
								TextView tExterior = (TextView) findViewById(R.id.tExterior);
								tExterior.setText(getdb.datoSimple("temp") + "°C");
								TextView tInterior = (TextView) findViewById(R.id.tInterior);
								tInterior.setText(getdb.datoSimple("tempInterior") + "°C");
								TextView hExterior = (TextView) findViewById(R.id.hExterior);
								hExterior.setText(getdb.datoSimple("humedad")+ "%");
								TextView hInterior = (TextView) findViewById(R.id.hInterior);
								hInterior.setText(getdb.datoSimple("humInterior")+ "%");
								TextView presion = (TextView) findViewById(R.id.presion);
								presion.setText(getdb.datoSimple("presion") + " hPa");
								TextView rSolar = (TextView) findViewById(R.id.rSolar);
								rSolar.setText(getdb.datoSimple("rSolar")+ " W/m²");
								TextView dViento = (TextView) findViewById(R.id.dViento);
								dViento.setText(getdb.direcViento("direcViento"));
                                TextView intViento = (TextView) findViewById(R.id.intViento);
                                intViento.setText(getdb.datoSimple("vPromedio") + " kts");
                                TextView vBase = (TextView) findViewById(R.id.vBase);
                                vBase.setText(getdb.datoSimple("vRafaga")+ " kts");
								Toast toast1 = Toast.makeText(getApplicationContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT);
								toast1.show();
							}
						});
					}
				}).start();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	public void clickimage (){
		ImageView uplafi = (ImageView) findViewById(R.id.uplafi);
		uplafi.setImageResource(R.drawable.uplafi);
		ImageView conaf = (ImageView) findViewById(R.id.conaf);
		conaf.setImageResource(R.drawable.logoconaf);
		ImageView armada = (ImageView) findViewById(R.id.armada);
		armada.setImageResource(R.drawable.armada);

		uplafi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Uri uri = Uri.parse("http://www.upla.cl/facultadingenieria"); // missing 'http://' will cause crashed
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		conaf.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Uri uri = Uri.parse("http://www.conaf.cl/"); // missing 'http://' will cause crashed
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		armada.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Uri uri = Uri.parse("http://meteoarmada.directemar.cl/"); // missing 'http://' will cause crashed
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
	}

	private int getIndex(Spinner spinner, String myString){

		int index = 0;

		for (int i=0;i<spinner.getCount();i++){
			if (spinner.getItemAtPosition(i).equals(myString)){
				index = i;
			}
		}
		return index;
	}
}