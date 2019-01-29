package com.unitedcreation.visha.tastytreat.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unitedcreation.visha.tastytreat.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;

import static com.unitedcreation.visha.tastytreat.ui.DetailActivity.fragmentPosition;
import static com.unitedcreation.visha.tastytreat.ui.DetailActivity.position;
import static com.unitedcreation.visha.tastytreat.ui.HomeActivity.recipeList;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private static final String TAG = BottomSheetFragment.class.getSimpleName();

    private TextView recipeNameTv;

    private OnNavigationItemChecked onNavigationItemChecked;
    private NavigationView navigationView;

    public BottomSheetFragment() {}

    public interface OnNavigationItemChecked {
        /**
         * Interface method to pass position of menu item being selected in modal navigation drawer to DetailActivity.
         * @param position of recipe's step in list which will replace current DetailFragment in view.
         */
        void onItemChecked (int position);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bottomsheet, container, false);

        navigationView = rootView.findViewById(R.id.navigation_view);
        recipeNameTv = navigationView.getHeaderView(0).findViewById(R.id.navigation_header_sub_tv);
        recipeNameTv.setText(recipeList.get(position).getName());

        /**
         * Creating navigation menu items dynamically based on number of steps available in our list.
         */
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < recipeList.get(position).getStepsList().size(); i++) {
            menu.add(R.id.navigation_menu_group, i, i, recipeList.get(position).getStepsList().get(i).getShortDescription()).setCheckable(true);
        }

        menu.getItem(fragmentPosition).setChecked(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                menuItem.setChecked(true);
                if (onNavigationItemChecked != null)
                    onNavigationItemChecked.onItemChecked(menuItem.getOrder());
                return true;

            }
        });

        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onNavigationItemChecked = (OnNavigationItemChecked) context;
        } catch (Exception e) {
            Log.e(TAG, "Interface not implemented by parent activity");
        }

    }
}
