package com.shokker.formsignaler

//import com.google.common.base.Predicates.instanceOf
import android.content.Context
import android.os.DropBoxManager
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import android.widget.SpinnerAdapter
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoActivityResumedException

import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.collect.Multiset
import com.shokker.formsignaler.UI.MainActivity
import com.shokker.formsignaler.model.MainContract
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random


@UninstallModules(SettingsModule::class)
@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
class UserCaseTest
{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get: Rule
    val activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)


    @Inject
    lateinit var fakeSettings : MainContract.GenerationSetting

    private fun getResourceString(id: Int): String? {
        val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
        return targetContext.getResources().getString(id)
    }

    @Test
    fun startApp1(){         // only start and stop app
        try {
            pressBack()
        }catch (e: NoActivityResumedException) { Log.d("test", "All closed") ; return}
        throw (Exception("test failed. App not closed"))
    }
    @Test
    fun startApp2()
    {
        Espresso.onView(ViewMatchers.withId(R.id.mainLayout)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_HOME))
    }
    @Test
    fun checkLoadToDialogSettings(){         // only start and stop app
        hiltRule.inject()
        Espresso.onView(ViewMatchers.withId(R.id.settingsFloatButton)).perform(ViewActions.click())
        //Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.spinnerSampleRate)).check(ViewAssertions.matches( ViewMatchers.withSpinnerText(fakeSettings.frameRate.toString())))
        Espresso.onView(ViewMatchers.withId(R.id.editTextBufferSize)).check(ViewAssertions.matches(ViewMatchers.withText( fakeSettings.bufferSize.toString())) )
        Espresso.onView(ViewMatchers.withId(R.id.swStopOnUnplug)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()) )
        Espresso.onView(ViewMatchers.withId(R.id.swStartOnPlug)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()) )
        Espresso.onView(ViewMatchers.withId(R.id.swStopOnUnplug)).check(ViewAssertions.matches(ViewMatchers.isChecked () ))
        Espresso.onView(ViewMatchers.withId(R.id.swStartOnPlug)).check(ViewAssertions.matches(ViewMatchers.isChecked()) )
    }
    @Test
    fun checkSavedSettings()
    {
        val newBuffer = Random.nextInt().rem(50000)
        Log.d("test", "Buffer value ${newBuffer}")

        Espresso.onView(ViewMatchers.withId(R.id.settingsFloatButton)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.swStartOnPlug)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.swStopOnUnplug)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.editTextBufferSize)).perform(ViewActions.clearText())
                                                                    .perform(ViewActions.typeText(newBuffer.toString()))
                                                                    .perform(ViewActions.closeSoftKeyboard())

//        Espresso.onView(ViewMatchers.withId(R.id.spinnerSampleRate)).perform(ViewActions.click())

/*        Espresso.onView(ViewMatchers.withText("44100"))
                //.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())*/
//        onData(allOf(`is`(instanceOf(Multiset.Entry::class.java)))).atPosition(3).perform(ViewActions.click())
  //      Thread.sleep(2000)
        /* Espresso.onData(allOf(`is`(instanceOf(String::class.java)),
                        `is`("44100"))).perform(ViewActions.click()) */         // do data select later

        Espresso.onView(ViewMatchers.withId(R.id.okSettingsButton)).perform(ViewActions.click())
       // TODO("Check saved values")
    }
    @Test
    fun checkFunctionList()
    {
        //onData(allOf(`is`(instanceOf(MainContract.SignalFunction::class.java)))).atPosition(2).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.functionList)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(getResourceString(R.string.sin_function_name))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(getResourceString(R.string.pwm_function_name))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(getResourceString(R.string.noise_function_name))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(getResourceString(R.string.double_pwm_function_name))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//        Espresso.onView(ViewMatchers.withText(getResourceString(R.string.saw_function_name))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
    @Test
    fun checkFunctionSelect()
    {
        Espresso.onView(ViewMatchers.withId(R.id.functionList)).perform(ViewActions.click())
        onData(allOf(`is`(instanceOf(MainContract.SignalFunction::class.java)))).atPosition(3).perform(ViewActions.click())
      //  TODO("Check proper function is selected")
    }
    @Test
    fun checkStartStopNoDelya()
    {
        Espresso.onView(ViewMatchers.withId(R.id.playFloatButton)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.playFloatButton)).perform(ViewActions.click())
    }
    @Test
    fun checkStartStopDelyaed()
    {
        Espresso.onView(ViewMatchers.withId(R.id.playFloatButton)).perform(ViewActions.click())
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.playFloatButton)).perform(ViewActions.click())
    }
    @Test
    fun checkChangingParamsOnRunning()
    {
        Espresso.onView(ViewMatchers.withId(R.id.playFloatButton)).perform(ViewActions.click())


        val xx = ViewMatchers.isAssignableFrom(EditText::class.java)
//        val yy = ViewMatchers.withContentDescription("50.0")// isAssignableFrom(EditText::class.java)
        val zz = ViewMatchers.withParent(ViewMatchers.withId(R.id.freq))
        Espresso.onView(allOf(xx, zz)).perform(ViewActions.click())
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("123"))
                .perform(ViewActions.pressImeActionButton())
                .perform(ViewActions.closeSoftKeyboard())

        Thread.sleep(100)
      //  TODO("Test new frequency value")
    }
}