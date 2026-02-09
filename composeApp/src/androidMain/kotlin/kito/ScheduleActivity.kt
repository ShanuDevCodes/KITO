package com.kito

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import com.kito.core.presentation.theme.KitoTheme
import com.kito.feature.schedule.presentation.ScheduleScreen
class ScheduleActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KitoTheme {
                Surface {
                    ScheduleScreen()
                }
            }
        }
    }
}
