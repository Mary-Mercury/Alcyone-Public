package com.mercury.alcyone.Data

import android.content.SharedPreferences
import com.mercury.alcyone.Presentation.ShowSubFragment
import java.time.LocalDate
import java.time.temporal.WeekFields
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MySharedPreferencesManager @Inject constructor(private val sharedPreferences: SharedPreferences) {
        fun saveData(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getData(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun saveStringData(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getStringData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue).toString()
    }

    fun saveIntData(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getIntData(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getDate(): String {
        val per = getData("altMode", false)
        val daysOfWeek: String = LocalDate.now().dayOfWeek.toString()
        val numsOfWeek = LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear())
        val parityOfWeek: String
        if (per) {
            parityOfWeek = if (numsOfWeek % 2 == 1) {
                "четная"
            } else {
                "нечетная"
            }
        } else {
            parityOfWeek = if (numsOfWeek % 2 == 0) {
                "четная"
            } else {
                "нечетная"
            }
        }
        return "$daysOfWeek $parityOfWeek"
    }

    fun getPosition(): Int {
        val per = getIntData("selectedItemPosition", 0)
        return per
    }
}