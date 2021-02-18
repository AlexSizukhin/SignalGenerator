package com.shokker.formsignaler

//import com.google.common.base.Predicates.instanceOf

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.shokker.formsignaler.DIModules.RealGeneratorModule
import com.shokker.formsignaler.UI.MainActivity
import com.shokker.formsignaler.model.MainContract
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextUInt


@UninstallModules(SettingsModule::class, RealGeneratorModule::class)
@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
@LargeTest
class UserCaseTest
{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get: Rule
    val activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)



    @Before
    fun prepare()
    {
        hiltRule.inject()
        Log.d("Test", "On @Before injected ${fakeSettings}")
        Log.d("Test", "On @Before injected ${fakeGenerator}")
    }


    @Inject
    lateinit var fakeSettings : MainContract.GenerationSetting
    @Inject
    lateinit var fakeGenerator: MainContract.GeneratorModel

    private fun getResourceString(id: Int): String {
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
        Espresso.onView(ViewMatchers.withId(R.id.settingsFloatButton)).perform(ViewActions.click())
        //Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.spinnerSampleRate)).check(
            ViewAssertions.matches(
                ViewMatchers.withSpinnerText(
                    fakeSettings.frameRate.toString()
                )
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.editTextBufferSize)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    fakeSettings.bufferSize.toString()
                )
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.swStopOnUnplug)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.swStartOnPlug)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.swStopOnUnplug)).check(
            ViewAssertions.matches(
                ViewMatchers.isChecked()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.swStartOnPlug)).check(
            ViewAssertions.matches(
                ViewMatchers.isChecked()
            )
        )
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
                                                                    .perform(
                                                                        ViewActions.typeText(
                                                                            newBuffer.toString()
                                                                        )
                                                                    )
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
        Espresso.onView(ViewMatchers.withText(getResourceString(R.string.sin_function_name))).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withText(getResourceString(R.string.pwm_function_name))).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withText(getResourceString(R.string.noise_function_name))).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withText(getResourceString(R.string.double_pwm_function_name))).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
//        Espresso.onView(ViewMatchers.withText(getResourceString(R.string.saw_function_name))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
    @Test
    fun checkFunctionSelect()
    {
        Espresso.onView(ViewMatchers.withId(R.id.functionList)).perform(ViewActions.click())
        onData(allOf(`is`(instanceOf(MainContract.SignalFunction::class.java)))).atPosition(3).perform(
            ViewActions.click()
        )
        if(fakeGenerator.generatingFunction?.functionName!=getResourceString(R.string.double_pwm_function_name))
            throw Exception(
                "Selected function is ${fakeGenerator.generatingFunction?.functionName} while must be ${
                    getResourceString(
                        R.string.double_pwm_function_name
                    )
                }"
            )

    }
    @Test
    fun checkFunctionSetOnLoad()
    {
        Thread.sleep(500)
        if(fakeGenerator.generatingFunction?.functionName!=getResourceString(R.string.sin_function_name))
            throw Exception(
                "Selected function is ${fakeGenerator.generatingFunction?.functionName} while must be ${
                    getResourceString(
                        R.string.sin_function_name
                    )
                }"
            )

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
        if(fakeGenerator.isRunning.value!=MainContract.GenaratorStatus.IS_RUNNING)
            throw Exception("Not running with ${fakeGenerator.isRunning.value}")
        Espresso.onView(ViewMatchers.withId(R.id.playFloatButton)).perform(ViewActions.click())
        Thread.sleep(500)
        if(fakeGenerator.isRunning.value!=MainContract.GenaratorStatus.STOPPED)
            throw Exception("Not stopped with ${fakeGenerator.isRunning.value}")
    }
    @Test
    fun checkChangingFrequencyOnRunning()
    {
        val newFreq:Int = 123
          Espresso.onView(ViewMatchers.withId(R.id.playFloatButton)).perform(ViewActions.click())


        val xx = ViewMatchers.isAssignableFrom(EditText::class.java)
//        val yy = ViewMatchers.withContentDescription("50.0")// isAssignableFrom(EditText::class.java)
        val zz = ViewMatchers.withParent(ViewMatchers.withId(R.id.freq))
        Espresso.onView(allOf(xx, zz)).perform(ViewActions.click())
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText(newFreq.toString()))
                .perform(ViewActions.pressImeActionButton())
                .perform(ViewActions.closeSoftKeyboard())

        Thread.sleep(100)

        if(fakeGenerator.generatingFunction==null)
            throw Exception("fakeGenerator.generatingFunction is NULL")
        if(fakeGenerator.generatingFunction?.frequency!=newFreq.toDouble())
            throw Exception("freq was ${fakeGenerator.generatingFunction?.frequency} but ${newFreq} excepted ")

    }

    @Test
    fun checkChangingParametersOnRunningPWM()
    {
        val newParam:Int = Random.nextUInt().rem(90u).toInt()

        Espresso.onView(ViewMatchers.withId(R.id.functionList)).perform(ViewActions.click())
        onData(allOf(`is`(instanceOf(MainContract.SignalFunction::class.java)))).atPosition(2).perform(
            ViewActions.click()
        )

        Espresso.onView(ViewMatchers.withId(R.id.playFloatButton)).perform(ViewActions.click())

        val xx = ViewMatchers.isAssignableFrom(EditText::class.java)  // EditText
//        val yy = ViewMatchers.withContentDescription("50.0")// isAssignableFrom(EditText::class.java)
        val zz = ViewMatchers.withParent(ViewMatchers.withChild(ViewMatchers.withText(R.string.step_proc_param)))

        Espresso.onView(allOf(zz, xx)).perform(ViewActions.click())
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText(newParam.toString()))
                .perform(ViewActions.pressImeActionButton())
                .perform(ViewActions.closeSoftKeyboard())

        Thread.sleep(100)

        if(fakeGenerator.generatingFunction==null)
            throw Exception("fakeGenerator.generatingFunction is NULL")
        if(fakeGenerator.generatingFunction!!.parameters.count()==0)
            throw Exception("parameters doesnt present")
        if(fakeGenerator.generatingFunction!!.parameters[0].currentValue!=newParam.toDouble())
            throw Exception("Param was ${fakeGenerator.generatingFunction!!.parameters[0].currentValue} but ${newParam} excepted ")

    }

    @Test
    fun screenRotate()                      // that f*cken works!!!
    {
        Thread.sleep(1000)
        val currentActivity = getCurrentActivity()
        currentActivity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        getInstrumentation().waitForIdleSync()
        Thread.sleep(1000)
    }



    fun getCurrentActivity(): Activity? {
        var activity: Activity? = null
        activityRule.scenario.onActivity {
            activity = it
        }
        return activity
    }

}
