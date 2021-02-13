package com.shokker.formsignaler

import android.content.Context
import android.util.Log
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.shokker.formsignaler.UI.MainActivity
import com.shokker.formsignaler.model.MainContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.random.nextUInt

@Module
@InstallIn(SingletonComponent::class) //, replaces = [SettingsModule::class])
class FakeSettingsModule {
    @Singleton
    @Provides
    fun provideSettings(@ApplicationContext context: Context): MainContract.GenerationSetting {
        Log.d("test", "Fake settings provided!")
        return SettingsTestImpl()
    }

    inner class SettingsTestImpl(
            override var frameRate: Int = 44100,
            override var bufferSize: Int = Random.nextUInt().rem(10000u).toInt(),
            override var startOnPlugCord: Boolean = true,
            override var stopOnUnPlugCord: Boolean = true,
            override var stopOnClose: Boolean = true
    ) : MainContract.GenerationSetting {
    }
}
