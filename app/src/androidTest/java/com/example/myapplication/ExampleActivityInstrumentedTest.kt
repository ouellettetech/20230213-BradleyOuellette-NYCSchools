package com.example.myapplication

import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
class ExampleActivityInstrumentedTest {
    @get : Rule
    var mActivityRule = ActivityScenarioRule(ItemDetailHostActivity::class.java)

    @Before
    fun setUp() {
        //initial setup code
    }

    @Test
    fun checkTheActivityStarts() {
        onView(withId(R.id.item_list)).check(matches(isDisplayed()))
    }

    // Other Tests to add:
    // Click on an item that it shows up, both invalid ones, and valid
    // Check state if school list request fails.
    // Check performance time for the requests.

    @After
    fun tearDown() {
        //clean up code
    }
}