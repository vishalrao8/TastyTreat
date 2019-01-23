package com.example.visha.tastytreat.ui;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;

import com.example.visha.tastytreat.R;
import com.example.visha.tastytreat.adapter.RecipeAdapter;
import com.example.visha.tastytreat.api.ApiClient;
import com.example.visha.tastytreat.api.ApiInterface;
import com.example.visha.tastytreat.model.Recipe;

import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.visha.tastytreat.utils.NetworkState.isConnected;

public class HomeActivity extends AppCompatActivity implements RecipeAdapter.CardClickListener {

    private static final int SCROLL_DIRECTION_UP = -1;

    private static final String TAG = HomeActivity.class.getSimpleName();
    public static final String EXTRA_RECIPE_POSITION = "extra_recipe_position";
    public static List<Recipe> recipeList;

    private ConnectivityManager connectivityManager;

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;
    private LinearLayoutManager recyclerViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (isConnected(Objects.requireNonNull(connectivityManager)))
            fetchRecipeList();

    }

    @Override
    public void onClick(int position) {
        moveToDetailScreen(position);
    }

    // Making network request to fetch Recipes' data
    private void fetchRecipeList() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Recipe>> call = apiInterface.getRecipe();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {

                recipeList = response.body();
                setUpRecyclerView();

            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

    }

    //Setting up RecyclerView showing Recipe cards
    private void setUpRecyclerView() {

        recipeRecyclerView = findViewById(R.id.recipe_rv);
        recipeAdapter = new RecipeAdapter(recipeList, this);

        recyclerViewLayout = new LinearLayoutManager(this);

        recipeRecyclerView.setLayoutManager(recyclerViewLayout);
        recipeRecyclerView.setAdapter(recipeAdapter);

        recipeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (recyclerView.canScrollVertically(SCROLL_DIRECTION_UP)) {
                    // Showing elevation
                    Objects.requireNonNull(getSupportActionBar()).setElevation(getResources().getDimension(R.dimen.actionBar));
                } else {
                    // Removing elevation
                    Objects.requireNonNull(getSupportActionBar()).setElevation(0);
                }
            }
        });

    }

    //Explicit intent to view recipe details
    private void moveToDetailScreen (int position) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_POSITION, position);
        startActivity(intent);

    }
}
