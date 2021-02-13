package com.shokker.formsignaler

//import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shokker.formsignaler.UI.MainActivity


import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SecondaryTests {
    @get: Rule
    val activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_FloatButtons()
    {
        onView(withId(R.id.playFloatButton)).check(matches(isDisplayed()))
        onView(withId(R.id.settingsFloatButton)).check(matches(isDisplayed()))
    }
    @Test
    fun test_SettingsClick1()
    {
        onView(withId(R.id.settingsFloatButton)).perform(click())
        //onView(withId(R.layout.settings_fragment)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.okSettingsButton)).check(matches(isDisplayed()))
        onView(withId(R.id.cancelSettingsButton)).check(matches(isDisplayed()))
        onView(withText(R.string.settings)).check(matches(isDisplayed()))              // convert to resource "Settings"
        // alternative
        onView(withText(R.string.settings))
            .inRoot(isDialog()) // <---
            .check(matches(isDisplayed()));
    }
    @Test
    fun test_hasFragment()
    {
        //onView(withId(R.layout.fragment_function)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.mainfragment)).check(matches(isDisplayed()))
        onView(withId(R.id.functionView)).check(matches(isDisplayed()))
     //   onView(AllOf.allOf(withId(R.id.mainLayout), withText("Sinus"))).check(matches(withText("Sinus")))*/
    }
    @Test
    fun test_startAndStop()
    {
        //onView(withId(R.id.playFloatButton)).check(matches( (R.drawable.ic_baseline_play_arrow_24)))
        onView(withId(R.id.playFloatButton)).perform(click())
        Thread.sleep(2500)
        onView(withId(R.id.playFloatButton)).perform(click())

    }
    @Test
    fun test_typeAmpletude()
    {
        onView(withId(R.id.ampletude)).perform(typeText("100"))
        Thread.sleep(2000)
    }
    @Test
    fun test_functionListSpinner()
    {
        onView(withId(R.id.functionList)).perform(click())
//        onData(allOf('is'(instanceOf(String::class.java)),'is'("Noise"))).perform(click())


       // onView(withId(R.id.ampletude)).check(matches(withText(containsString("Ampletude"))))
    }
    @Test
    fun test_fragmentInIsolation()
    {
     /*  val fragmentArgs = bundleOf("selectedListItem" to 0)

        val scenario2 = launchFragmentInContainer<FunctionFragment>()
        val scenario1 = launchFragmentInContainer<SettingsDialog>()
*/
        onView(withId(R.id.freq)).check(matches(isDisplayed()))
        onView(withId(R.id.functionView)).check(matches(isDisplayed()))
    }
}