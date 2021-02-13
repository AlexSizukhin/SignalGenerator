package com.shokker.formsignaler

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shokker.formsignaler.UI.MainActivity

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
/*    @Rule
    val myTestRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)*/

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.shokker.formsignaler", appContext.packageName)
    }
    @Test
    fun test_isActivityInView()
    {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()))
    }
    @Test
    fun test_has_spinnerdisplayed()
    {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.functionList)).check(matches(isDisplayed()))
        onView(withId(R.id.freq)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun test_isSomething()
    {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.functionCaption)).check(matches(withText("Signal function name")))
    }

}