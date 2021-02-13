package com.shokker.formsignaler.data

import androidx.room.*
import com.shokker.formsignaler.model.MainContract


@Entity(tableName = "settings_table")
data class SettingsData(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    var mSampleRate:Int,
    var mBufferSize:Int,
    var mStartOnPlug:Boolean,
    var mStopOnUnPlug:Boolean,
    var mStopOnClose:Boolean
): MainContract.GenerationSetting {


    override var frameRate: Int
        @Ignore
        get() = mSampleRate
        set(value) { mSampleRate = value }
    override var bufferSize: Int
        @Ignore
        get() = mBufferSize
        set(value) {  mBufferSize = value }
    override var startOnPlugCord: Boolean
        @Ignore
        get() = mStartOnPlug
        set(value) { mStartOnPlug = value }
    override var stopOnUnPlugCord: Boolean
        @Ignore
        get() =mStopOnUnPlug
        set(value) { mStopOnUnPlug = value }
    override var stopOnClose: Boolean
        @Ignore
        get() = mStopOnClose
        set(value) { mStopOnClose = value }
}

@Dao
interface SettingsDataDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSettings(settings:SettingsData)

    @Query("SELECT * from settings_table")
    fun getAllSettings():List<SettingsData>

    @Query("SELECT * from settings_table WHERE id = 13  LIMIT 1")
    fun getSettings(): SettingsData?
}


@Database(entities = arrayOf(SettingsData::class), version = 1, exportSchema = false)
abstract class SignalGeneratorDB: RoomDatabase()
{
    abstract fun settingsDao(): SettingsDataDao

}