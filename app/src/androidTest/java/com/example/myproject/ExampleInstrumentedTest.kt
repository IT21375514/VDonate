package com.example.myproject

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.myproject", appContext.packageName)
    }
    @Test
    fun testButton() {
        val cost = 10.5 // assume this is the cost entered by the user
        val count = 2 // assume this is the count entered by the user
        val expectedValue = (cost * count).toString() // calculate the expected value

        onView(withId(R.id.typegoods_items)).perform(replaceText(cost.toString()))
        onView(withId(R.id.editfoodTextNumber)).perform(replaceText(count.toString()))
        onView(withId(R.id.btnfood)).perform(click())
        onView(withId(R.id.editfoodTextNumber)).check(matches(withText(expectedValue)))
    }

}