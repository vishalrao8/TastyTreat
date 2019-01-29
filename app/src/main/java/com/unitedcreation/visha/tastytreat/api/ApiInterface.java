package com.unitedcreation.visha.tastytreat.api;

import com.unitedcreation.visha.tastytreat.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("baking.json")
    Call<List<Recipe>> getRecipe();

}
