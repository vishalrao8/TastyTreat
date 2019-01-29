package com.unitedcreation.visha.tastytreat.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.unitedcreation.visha.tastytreat.R;
import com.unitedcreation.visha.tastytreat.service.RecipeWidgetService;
import com.unitedcreation.visha.tastytreat.ui.DetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "extra_item";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Setting up the intent that starts the RecipeViewService, which will
        // provide the views for this collection.
        Intent intent = new Intent(context, RecipeWidgetService.class);

        // Adding the app widget ID to the intent extras.
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Instantiating the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        // Setting up the RemoteViews object to use a RemoteViews adapter.
        views.setRemoteAdapter(R.id.appwidget_steps, intent);

        Intent intentToApp = new Intent(context, DetailActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToApp,0);
        views.setPendingIntentTemplate(R.id.appwidget_steps, pendingIntent);

        // Instructing the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

