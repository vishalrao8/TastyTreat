package com.unitedcreation.visha.tastytreat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unitedcreation.visha.tastytreat.R;
import com.unitedcreation.visha.tastytreat.model.Ingredients;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredients> ingredientsList;

    public IngredientAdapter (List<Ingredients> ingredientsList) {

        this.ingredientsList = ingredientsList;

    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_ingredients, parent, false);
        return new IngredientViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {

        holder.ingredientTv.setText(ingredientsList.get(position).getIngredient());
        holder.quantityTv.setText(ingredientsList.get(position).getQuantity());
        holder.measureTv.setText(ingredientsList.get(position).getMeasure());

    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder{

        TextView ingredientTv;
        TextView quantityTv;
        TextView measureTv;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            ingredientTv = itemView.findViewById(R.id.ingredient);
            quantityTv = itemView.findViewById(R.id.quantity);
            measureTv = itemView.findViewById(R.id.measure);

        }
    }
}
