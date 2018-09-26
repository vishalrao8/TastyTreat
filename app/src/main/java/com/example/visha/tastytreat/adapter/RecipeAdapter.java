package com.example.visha.tastytreat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visha.tastytreat.R;
import com.example.visha.tastytreat.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.transition.TransitionManager;

public class RecipeAdapter extends Adapter<RecipeAdapter.RecipeViewHolder> {

    private int expandedPosition = -1;

    private List<Recipe> recipeList;
    private CardClickListener cardClickListener;

    public interface CardClickListener {

        void onClick (int position);

    }

    public RecipeAdapter(List<Recipe> recipeList, CardClickListener cardClickListener) {
        this.recipeList = recipeList;
        this.cardClickListener = cardClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_home, parent, false);
        return new RecipeViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, final int position) {

        /**
         * A private boolean to keep track of current expanded card.
         */
        final boolean isExpanded = position == expandedPosition;
        holder.itemView.setActivated(isExpanded);

        /**
         * Showing or hiding contents on the basis of isExpanded.
         */
        if (isExpanded) {

            holder.ingredientsDetailRv.setVisibility(View.VISIBLE);
            holder.buttonIcon.setBackgroundResource(R.drawable.home_ic_up_24dp);


        } else {

            holder.ingredientsDetailRv.setVisibility(View.GONE);
            holder.buttonIcon.setBackgroundResource(R.drawable.home_ic_down_24dp);

        }

        if (!recipeList.get(position).getImageURL().equals(""))
            Picasso.get()
                    .load(recipeList.get(position).getImageURL())
                    .placeholder(R.drawable.home_placeholder_medium)
                    .into(holder.recipeImage);
        else
            holder.recipeImage.setImageResource(R.drawable.home_placeholder_medium);

        holder.recipeTv.setText(recipeList.get(position).getName());
        holder.noOfServings.setText(recipeList.get(position).getServings());
        holder.noOfIngredients.setText(String.valueOf(recipeList.get(position).getIngredientsList().size()));
        holder.noOfSteps.setText(String.valueOf(recipeList.get(position).getStepsList().size()));

        holder.buttonToExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * Changing value of global variable (expandedPosition) on the basis of isExpanded and notifying adapter to refresh.
                 * This value will always toggle whenever action to expand or collapse is invoked which will limit to expand only one card at a time.
                 */
                expandedPosition = isExpanded ? -1:position;
                TransitionManager.beginDelayedTransition(holder.ingredientsDetailRv);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView recipeImage;
        TextView recipeTv;

        TextView noOfServings;
        TextView noOfIngredients;
        TextView noOfSteps;

        RecyclerView ingredientsDetailRv;

        View buttonToExpand;
        View buttonIcon;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeTv = itemView.findViewById(R.id.recipe_tv);

            noOfServings = itemView.findViewById(R.id.serving_quantity_tv);
            noOfIngredients = itemView.findViewById(R.id.ingredient_quantity_tv);
            noOfSteps = itemView.findViewById(R.id.step_quantity_tv);

            ingredientsDetailRv = itemView.findViewById(R.id.ingredients_detail_rv);
            IngredientAdapter ingredientAdapter = new IngredientAdapter(recipeList.get(0).getIngredientsList());
            ingredientsDetailRv.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            ingredientsDetailRv.setAdapter(ingredientAdapter);

            buttonToExpand = itemView.findViewById(R.id.recipe_expand_bt);
            buttonIcon = itemView.findViewById(R.id.button_ic);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            cardClickListener.onClick(getAdapterPosition());
        }
    }
}
