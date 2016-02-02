package com.javatechig.widgetdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class GetDataFromDB {

	String estacion = "";
	String data = "";


	public void getDataFromDB(String preferencia) {

		estacion = preferencia;

		try {
			HttpPost httppost;
			HttpClient httpclient;
			httpclient = new DefaultHttpClient();
			System.out.print(preferencia);
			httppost = new HttpPost("http://sistema.meteorologiaupla.cl/Clima/scr/php/widget/widget"+preferencia+".php"); // change this to your URL.....
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost,
					responseHandler);
			Log.d("getuserdb", preferencia);
			this.data = response.trim();

		} catch (Exception e) {
			System.out.println("ERROR : " + e.getMessage());
		}
	}

	public String direcViento(final String dato) {
		Integer datoRescatado = 0;
		String retorno = "";
		try {
			JSONArray jArray = new JSONArray(this.data);
			// Mansa huea para colocar direccion viento
			for (int i = 0; i < jArray.length(); i++)
			{
				JSONObject json_data = jArray.getJSONObject(i);
				datoRescatado = json_data.getInt(dato);
				if((datoRescatado >= 349.5 && datoRescatado <= 360) || (datoRescatado >= 0 && datoRescatado <= 10.5))
				{
					retorno = datoRescatado + "° N";
				}
				else if ((datoRescatado >= 10.5) && datoRescatado <= 33.5)
				{
					retorno = datoRescatado + "° NNE";
				}
				else if ((datoRescatado >= 33.5) && datoRescatado <= 55.5)
				{
					retorno = datoRescatado + "° NE";
				}
				else if ((datoRescatado >= 55.5) && datoRescatado <= 78.5)
				{
					retorno = datoRescatado + "° ENE";
				}
				else if ((datoRescatado >= 78.5) && datoRescatado <= 100.5)
				{
					retorno = datoRescatado + "° E";
				}
				else if ((datoRescatado >= 100.5) && datoRescatado <= 123.5)
				{
					retorno = datoRescatado + "° ESE";
				}
				else if ((datoRescatado >= 123.5) && datoRescatado <= 145.5)
				{
					retorno = datoRescatado + "° SE";
				}
				else if ((datoRescatado >= 145.5) && datoRescatado <= 168.5)
				{
					retorno = datoRescatado + "° SSE";
				}
				else if ((datoRescatado >= 168.5) && datoRescatado <= 190.5)
				{
					retorno = datoRescatado + "° S";
				}
				else if ((datoRescatado >= 190.5) && datoRescatado <= 213.5)
				{
					retorno = datoRescatado + "° SSO";
				}
				else if ((datoRescatado >= 213.5) && datoRescatado <= 235.5)
				{
					retorno = datoRescatado + "° SO";
				}
				else if ((datoRescatado >= 235.5) && datoRescatado <= 258.5)
				{
					retorno = datoRescatado + "° OSO";
				}
				else if ((datoRescatado >= 258.5) && datoRescatado <= 280.5)
				{
					retorno = datoRescatado + "° O";
				}
				else if ((datoRescatado >= 280.5) && datoRescatado <= 303.5)
				{
					retorno = datoRescatado + "° ONO";
				}
				else if ((datoRescatado >= 303.5) && datoRescatado <= 325.5)
				{
					retorno = datoRescatado + "° NO";
				}
				else if ((datoRescatado >= 325.5) && datoRescatado <= 349.5)
				{
					retorno = datoRescatado + "° NNO";
				}
			}
		}
		catch (JSONException e)
		{
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return  retorno;
	}

	public String datoSimple(final String dato) {
		String datoRescatado = "";
		try {
			JSONArray jArray = new JSONArray(this.data);
			for (int i = 0; i < jArray.length(); i++)
			{
				JSONObject json_data = jArray.getJSONObject(i);
				datoRescatado = json_data.getString(dato);
			}
		}
		catch (JSONException e)
		{
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return datoRescatado;
	}

	public static String estacion (String estacion)
	{
		String retorno ="";

		if (estacion.equals("Estacion El Peral"))
		{
			retorno = "Peral";
		}
		else
		{
			if (estacion.equals("Estacion La Campana"))
			{
				retorno = "Campana";
			}
			else
			{
				if (estacion.equals("Estacion El Yali"))
				{
					retorno = "Yali";
				}
			}
		}
		return retorno;
	}

	public static void setDefaults(String key, String value, Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getDefaults(String key, Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(key, null);
	}
}
