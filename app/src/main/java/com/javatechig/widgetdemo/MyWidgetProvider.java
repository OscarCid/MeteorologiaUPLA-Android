package com.javatechig.widgetdemo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {
	String preferencia = "";
	String estacion = "";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// initializing widget layout
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);

		// register for button event
		remoteViews.setOnClickPendingIntent(R.id.sync_button,
				buildButtonPendingIntent(context));

		remoteViews.setOnClickPendingIntent(R.id.config,
				config(context));

		// updating view with initial data
		remoteViews.setTextViewText(R.id.title, getTitle());

		// request for widget update
		pushWidgetUpdate(context, remoteViews);
	}

	public static PendingIntent buildButtonPendingIntent(Context context) {

		// initiate widget update request
		Intent intent = new Intent();
		intent.setAction(WidgetUtils.WIDGET_UPDATE_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static PendingIntent config(Context context) {

		// initiate widget update request
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		return pendingIntent;
	}

	public static PendingIntent maps(Context context, String estacion) {

		double latitude = 0;
		double longitude = 0;
		String label = "";
		// initiate widget update request
		if (estacion.equals("Peral"))
		{
			latitude = -33.503889;
			longitude = -71.610278;
			label = "Estacion meteorológica El Peral";
		}
		else
		{
			if (estacion.equals("Campana"))
			{
				latitude = -32.988056;
				longitude = -71.142778;
				label = "Estacion meteorológica La Campana";
			}
			else
			{
				if (estacion.equals("Yali"))
				{
					latitude = -33.748889;
					longitude = -71.700000;
					label = "Estacion meteorológica El Yali";
				}
			}
		}

		String uriBegin = "geo:" + latitude + "," + longitude;
		String query = latitude + "," + longitude + "(" + label + ")";
		String encodedQuery = Uri.encode(query);
		String uriString = uriBegin + "?q=" + encodedQuery + "&z=12";
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
		intent.setPackage("com.google.android.apps.maps");
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		return pendingIntent;
	}

	private static CharSequence getTitle() {
		return "Presiona el boton para obtener los datos";
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context,
				MyWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);
	}
}
