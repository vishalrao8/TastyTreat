package com.example.visha.tastytreat;

import android.app.Activity;

import com.example.visha.tastytreat.ui.HomeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeActivityInstrumentedTest {

    /*@Rule
    public ActivityTestRule<HomeActivity> homeActivityTestRule =
            new ActivityTestRule<>(HomeActivity.class);*/

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {

        ActivityScenario activityScenario = ActivityScenario.launch(HomeActivity.class);
        activityScenario.onActivity(new ActivityScenario.ActivityAction<HomeActivity>() {
            @Override
            public void perform(HomeActivity activity) {

                mIdlingResource = activity.getIdlingResource();
                IdlingRegistry.getInstance().register(mIdlingResource);

            }
        });
    }

    @Test
    public void testRecipeCard() {

        onView(withId(R.id.recipe_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.video_player))
                .check(matches(isDisplayed()));

    }

    @After
    public void unregisterIdlingResource() {

        if (mIdlingResource != null)
            IdlingRegistry.getInstance().unregister(mIdlingResource);

    }
}
