package com.example.visha.tastytreat.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.visha.tastytreat.R;

import static com.example.visha.tastytreat.ui.DetailActivity.EXTRA_STEP_POSITION;
import static com.example.visha.tastytreat.ui.HomeActivity.EXTRA_RECIPE_POSITION;
import static com.example.visha.tastytreat.widget.RecipeConfigureActivity.recipePosition;
import static com.example.visha.tastytreat.widget.RecipeConfigureActivity.stepsList;

public class RecipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewFactory(this.getApplicationContext(), intent);
    }

    class RecipeRemoteViewFactory implements RemoteViewsFactory {

        private final Context context;
        private final int appWidgetId;

        private RecipeRemoteViewFactory(Context applicationContext, Intent intent) {

            this.context = applicationContext;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return stepsList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            // Constructing a remote views item based on the app widget item XML file,
            // and setting the text based on the position.
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_recipe_widget);
            views.setTextViewText(R.id.item_step_tv, stepsList.get(position).getShortDescription());

            // Setting a fill-intent, which will be used to fill in the pending intent template
            // that is set on the list view in RecipeWidgetProvider.

            Bundle extras = new Bundle();
            extras.putInt(EXTRA_RECIPE_POSITION, recipePosition);
            extras.putInt(EXTRA_STEP_POSITION, position);

            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);

            views.setOnClickFillInIntent(R.id.item_step_tv, fillInIntent);

            // Returning the remote views object.
            return views;

        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
