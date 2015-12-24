package com.javatechig.widgetdemo;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;


public class MyWidgetIntentReceiver extends BroadcastReceiver {
	String preferencia = "";
	String estacion = "";
	Boolean isInternetPresent = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(WidgetUtils.WIDGET_UPDATE_ACTION)) {
			preferencia = GetDataFromDB.getDefaults("estacion",context);
			estacion = GetDataFromDB.estacion(preferencia);
			updateWidgetPictureAndButtonListener(context);
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);

		// updating view
		ConnectionDetector cd;
		cd = new ConnectionDetector(context);
		isInternetPresent = cd.isConnectingToInternet();
		estacion = GetDataFromDB.estacion(preferencia);
		final GetDataFromDB getdb = new GetDataFromDB();
		getdb.getDataFromDB(estacion);
		String titulo = GetDataFromDB.getDefaults("estacion", context);
		remoteViews.setTextViewText(R.id.title, getTitle(titulo));
		remoteViews.setTextViewText(R.id.fecha, getdb.datoSimple("fecha"));
		remoteViews.setTextViewText(R.id.hora, getdb.datoSimple("hora"));
		remoteViews.setTextViewText(R.id.tExterior, getdb.datoSimple("temp")+"°C");
		remoteViews.setTextViewText(R.id.tInterior, getdb.datoSimple("tempInterior")+"°C");
		remoteViews.setTextViewText(R.id.hInterior, getdb.datoSimple("humInterior")+"%");
		remoteViews.setTextViewText(R.id.hExterior, getdb.datoSimple("humedad")+"%");
		remoteViews.setTextViewText(R.id.presion, getdb.datoSimple("presion") + "hPa");

		remoteViews.setOnClickPendingIntent(R.id.title, MyWidgetProvider.maps(context, estacion));
		remoteViews.setOnClickPendingIntent(R.id.posicion, MyWidgetProvider.maps(context, estacion));
		remoteViews.setOnClickPendingIntent(R.id.config, MyWidgetProvider.config(context));
		remoteViews.setOnClickPendingIntent(R.id.button, MyWidgetProvider.botones(context));
		remoteViews.setOnClickPendingIntent(R.id.button2, MyWidgetProvider.botones(context));
		// re-registering for click listener
		remoteViews.setOnClickPendingIntent(R.id.sync_button, MyWidgetProvider.buildButtonPendingIntent(context));

		MyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(),
				remoteViews);
	}

	private String getTitle(String hola) {
		if (isInternetPresent == true)
		{
			return hola;
		}
		else
		{
			return  "No hay conexion a Internet";
		}

	}
}
