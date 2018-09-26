package com.example.visha.tastytreat.ui;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.visha.tastytreat.R;
import com.example.visha.tastytreat.utils.DpToPixel;
import com.example.visha.tastytreat.view.BottomSheetFragment;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.example.visha.tastytreat.ui.HomeActivity.INTENT_EXTRA_POSITION;
import static com.example.visha.tastytreat.ui.HomeActivity.recipeList;

public class DetailActivity extends AppCompatActivity implements BottomSheetFragment.OnNavigationItemChecked {

    private static final String TAG = DetailActivity.class.getSimpleName();

    public static int position;
    public static int fragmentPosition;

    public static boolean tabletLayout;

    private FrameLayout fragmentContainer;

    private FloatingActionButton floatingActionButton;
    private BottomAppBar bottomAppBar;
    private BottomSheetFragment bottomSheetFragment;

    private NavigationView navigationView;
    private Button nextButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabletLayout = getResources().getBoolean(R.bool.tablet_layout);
        fragmentContainer = findViewById(R.id.fragment_container_layout);

        /**
         * Initiating required fields based on device's smallest width i.e. tabletLayout = true when smallest width > 600dp and tabletLayout = false when sw < 600dp.
         */
        if (!tabletLayout) {

            floatingActionButton = findViewById(R.id.fab);

            /**
             * Increasing fragmentPosition by 1 and calling swapFragment() method to get to next fragment..
             */
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fragmentPosition++;
                    swapFragment();

                }
            });

            bottomAppBar = findViewById(R.id.bottom_bar);
            bottomAppBar.inflateMenu(R.menu.home_menu_item);

            /**
             * Decreasing fragmentPosition by 1 and calling swapFragment() method to get to previous fragment.
             */
            bottomAppBar.getMenu().findItem(R.id.item_back).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if (item.getItemId() == R.id.item_back) {

                        fragmentPosition--;
                        swapFragment();
                        return true;

                    } else return false;

                }
            });

            /**
             * Showing modal navigation drawer only on mobile devices on navigation icon clicked.
             */
            bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

                }
            });

            /**
             * Manually handling orientation changes to avoid exo player build rebuild after recreation of activity.
             */
            if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
                landscapeUiConfiguration();

        } else {

            /**
             * Forcing orientation to be LANDSCAPE on tablet devices and initiating required view fields.
             */
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            navigationView = findViewById(R.id.tablet_navigation_view);
            initiateNavigationItems();

            nextButton = findViewById(R.id.next_button);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fragmentPosition++;
                    swapFragment();

                }
            });

            backButton = findViewById(R.id.back_button);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fragmentPosition--;
                    swapFragment();

                }
            });

        }

        /**
         * Getting position of recipe item being selected from intent created this activity to show corresponding recipe details.
         */
        position = getIntent().getIntExtra(INTENT_EXTRA_POSITION, -1);

        if (position != -1)
            populateUI();

    }

    /**
     * A method from BottomSheetFragment's interface to replace current fragment in view by fragment at passed on position.
     * @param position of modal navigation item selected.
     */
    @Override
    public void onItemChecked(int position) {

        fragmentPosition = position;
        swapFragment();

        /**
         * Dismissing modal navigation drawer (only for mobile devices) as soon as navigation item is selected.
         */
        if (!tabletLayout)
            bottomSheetFragment.dismiss();

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        /**
         * Handling orientation configuration manually for mobile devices.
         */
        if (!tabletLayout) {

            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

                landscapeUiConfiguration();


            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

                /**
                 * Showing corresponding view fields in PORTRAIT mode.
                 */
                Objects.requireNonNull(getSupportActionBar()).show();

                bottomAppBar.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.VISIBLE);

                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fragmentContainer.getLayoutParams();
                params.setMargins(0, 0, 0, DpToPixel.convert(62, this));
                fragmentContainer.setLayoutParams(params);

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            }
        }
    }

    /**
     * A method to populate UI with appropriate data on successfully getting the recipe position.
     */
    private void populateUI () {

        Objects.requireNonNull(getSupportActionBar()).setTitle(recipeList.get(position).getName());

        if (!tabletLayout)
            bottomSheetFragment = new BottomSheetFragment();

        /**
         * Adding first ever instance of DetailFragment in container at activity_detail.xml.
         */
        DetailFragment detailFragment = new DetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_layout, detailFragment)
                .commit();

    }

    /**
     * A method to replace current fragment in view with another instance of DetailFragment.
     */
    private void swapFragment() {

        DetailFragment detailFragment = new DetailFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_layout, detailFragment)
                .commit();

    }

    private void initiateNavigationItems() {

        /**
         * Creating navigation menu items dynamically based on number of steps available in our list.
         */
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < recipeList.get(position).getStepsList().size(); i++) {
            menu.add(R.id.navigation_menu_group, i, i, recipeList.get(position).getStepsList().get(i).getShortDescription()).setCheckable(true);
        }

        navigationView.getMenu().getItem(fragmentPosition).setChecked(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                menuItem.setChecked(true);
                onItemChecked(menuItem.getOrder());
                return true;

            }
        });

    }

    @SuppressLint("RestrictedApi")
    private void landscapeUiConfiguration() {

        /**
         * Hiding corresponding view fields & removing fragmentContainer's margins to get exoPlayer in full screen mode in LANDSCAPE mode.
         */
        Objects.requireNonNull(getSupportActionBar()).hide();
        bottomAppBar.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fragmentContainer.getLayoutParams();
        params.setMargins(0,0,0,0);
        fragmentContainer.setLayoutParams(params);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    protected void onStop() {
        super.onStop();
        fragmentPosition = 0;
    }
}
