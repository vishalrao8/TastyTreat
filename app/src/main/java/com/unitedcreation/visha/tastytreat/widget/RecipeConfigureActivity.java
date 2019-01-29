package com.unitedcreation.visha.tastytreat.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.Toast;

import com.unitedcreation.visha.tastytreat.R;
import com.unitedcreation.visha.tastytreat.api.ApiClient;
import com.unitedcreation.visha.tastytreat.api.ApiInterface;
import com.unitedcreation.visha.tastytreat.model.Recipe;
import com.unitedcreation.visha.tastytreat.model.Steps;
import com.unitedcreation.visha.tastytreat.service.RecipeWidgetService;
import com.unitedcreation.visha.tastytreat.ui.DetailActivity;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeConfigureActivity extends AppCompatActivity {

    private static final String TAG = RecipeWidgetService.class.getSimpleName();
    public static int recipePosition;

    private int appWidgetId;

    public static List<Steps> stepsList;
    public static List<Recipe> recipeList;

    private Spinner categorySpinner;
    private Button categorySelectorButton;
    private Intent resultIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_configuration);

        getWidgetId();

        categorySpinner = findViewById(R.id.category_spinner);
        categorySelectorButton = findViewById(R.id.category_selector_bt);

        // Creating an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.recipe_array, android.R.layout.simple_spinner_item);

        // Specifying the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Applying the adapter to the spinner
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                recipePosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categorySelectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fetchRecipeList();

            }
        });

        resultIntent = new Intent();
        resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultIntent);

    }

    private void getWidgetId () {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

        }
    }

    private void updateWidget () {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        // Setting up the intent that starts the RecipeViewService, which will
        // provide the views for this collection.
        Intent intent = new Intent(this, RecipeWidgetService.class);

        // Adding the app widget ID to the intent extras.
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        String[] widgetTitle = getResources().getStringArray(R.array.recipe_array);

        // Instantiating the RemoteViews object
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.recipe_widget);

        views.setTextViewText(R.id.appwidget_title, widgetTitle[recipePosition]);

        // Setting up the RemoteViews object to use a RemoteViews adapter.
        views.setRemoteAdapter(R.id.appwidget_steps, intent);

        Intent intentToApp = new Intent(this, DetailActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentToApp,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.appwidget_steps, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    // Making network request to fetch Recipes' data
    private void fetchRecipeList() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Recipe>> call = apiInterface.getRecipe();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {

                recipeList = Objects.requireNonNull(response.body());
                stepsList = recipeList.get(recipePosition).getStepsList();
                updateWidget();
                setResult(RESULT_OK, resultIntent);
                finish();

            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {

                Log.d(TAG, t.getMessage());
                Toast.makeText(RecipeConfigureActivity.this, "Failed to communicate with server! Try again later.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }
}
