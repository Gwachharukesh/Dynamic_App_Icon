package com.example.change_icon

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.os.Bundle
import androidx.annotation.NonNull
import android.util.Log
import com.example.change_icon.IconManager
import android.content.SharedPreferences

class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.example.change_icon/change_icon"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->
            when (call.method) {
                "changeIcon" -> {
                    val iconName = call.arguments as? String
                    if (iconName != null) {
                        val changed = IconManager(this).changeIcon(iconName)
                        if (changed) {
                            val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                            prefs.edit().putString("selected_icon", iconName).apply()
                            result.success(true)
                        } else {
                            result.error("INVALID_ICON", "Icon not found", null)
                        }
                    } else {
                        result.error("INVALID_ARGUMENT", "Icon name is required", null)
                    }
                }
                "restartApp" -> {
                    // Restart the app by finishing current activity and starting it again
                    val intent = packageManager.getLaunchIntentForPackage(packageName)
                    intent?.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    result.success(true)
                }
                else -> result.notImplemented()
            }
        }
    }

    override fun onCreate(@NonNull savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val savedIcon = prefs.getString("selected_icon", null)
        if (savedIcon != null) {
            IconManager(this).changeIcon(savedIcon)
        } else {
            updateIcon()
        }
    }

    private fun updateIcon() {
        try {
            IconManager(this).updateAppIcon()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error updating icon", e)
            e.printStackTrace()
        }
    }
}
