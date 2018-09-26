package com.example.visha.tastytreat.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("ingredients")
    private List<Ingredients> ingredientsList;

    @SerializedName("steps")
    private List<Steps> stepsList;

    @SerializedName("servings")
    private String servings;

    @SerializedName("image")
    private String imageURL;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredients> getIngredientsList() {
        return ingredientsList;
    }

    public List<Steps> getStepsList() {
        return stepsList;
    }

    public String getServings() {
        return servings;
    }

    public String getImageURL() {
        return imageURL;
    }
}
