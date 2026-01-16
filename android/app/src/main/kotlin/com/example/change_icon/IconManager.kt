package com.example.change_icon

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import java.util.Calendar
import android.util.Log

class IconManager(private val context: Context) {
    private val defaultIcon = ComponentName(context, "com.example.change_icon.MainActivityDefault")
    private val newYearIcon = ComponentName(context, "com.example.change_icon.MainActivityNewYear")

    fun changeIcon(iconName: String): Boolean {
        val pm = context.packageManager

        return when (iconName) {
            "AppIcon" -> {
                Log.d("IconManager", "Switching to default icon")
                pm.setComponentEnabledSetting(
                    defaultIcon,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    newYearIcon,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                true
            }
            "AppIconNewYear" -> {
                Log.d("IconManager", "Switching to New Year icon")
                pm.setComponentEnabledSetting(
                    newYearIcon,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    defaultIcon,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                true
            }
            else -> {
                Log.w("IconManager", "Unknown icon name: $iconName")
                false
            }
        }
    }

    fun updateAppIcon() {
        try {
            val calendar = Calendar.getInstance()
            val isNewYearPeriod = isInNewYearPeriod(calendar)
            
            Log.d("IconManager", "Current date: Month=${calendar.get(Calendar.MONTH)}, Day=${calendar.get(Calendar.DAY_OF_MONTH)}")
            Log.d("IconManager", "Is New Year Period: $isNewYearPeriod")

            val pm = context.packageManager

            if (isNewYearPeriod) {
                Log.d("IconManager", "Switching to New Year icon")
                // Enable New Year icon first
                pm.setComponentEnabledSetting(
                    newYearIcon,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                // Then disable default icon
                pm.setComponentEnabledSetting(
                    defaultIcon,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
            } else {
                Log.d("IconManager", "Switching to default icon")
                // Enable default icon first
                pm.setComponentEnabledSetting(
                    defaultIcon,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                // Then disable New Year icon
                pm.setComponentEnabledSetting(
                    newYearIcon,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
            
        } catch (e: Exception) {
            Log.e("IconManager", "Error updating icon", e)
            e.printStackTrace()
            // Ensure default icon is enabled if something goes wrong
            try {
                context.packageManager.setComponentEnabledSetting(
                    defaultIcon,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
            } catch (e: Exception) {
                Log.e("IconManager", "Error enabling default icon", e)
                e.printStackTrace()
            }
        }
    }

    private fun isInNewYearPeriod(calendar: Calendar): Boolean {
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Check if it's JANUARY (month 1) and the day is between 1 and 5
        return month == Calendar.JANUARY && day in 1..5
    }
}