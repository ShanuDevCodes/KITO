package com.kito

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.DisposableEffect
import androidx.core.util.Consumer
import android.content.Intent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.kito.core.datastore.PrefsRepository
import com.kito.core.platform.AppConfig
import com.kito.core.presentation.components.ExpressiveEasing
import com.kito.core.presentation.navigation.RootDestination
import com.kito.core.presentation.theme.KitoTheme
import com.kito.feature.app.presentation.MainUI
import com.kito.feature.auth.presentation.OnBoardingScreen
import com.kito.feature.auth.presentation.UserSetupScreen
import com.kito.feature.schedule.notification.NotificationPipelineController
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val prefs: PrefsRepository by inject()
    private val notificationPipelineController by lazy {
        NotificationPipelineController.get(applicationContext)
    }
    private lateinit var appUpdateManager: AppUpdateManager

    private val updateLauncher: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result: ActivityResult ->
            if (result.resultCode != RESULT_OK) {
                // User cancelled or update failed
            }
        }

    private val installStateListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            showCompleteUpdateSnackbar()
        }
    }

    override fun onStart() {
        super.onStart()
        checkForUpdate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        appUpdateManager = AppUpdateManagerFactory.create(this)

        // Initialize AppConfig with BuildConfig values
        AppConfig.init(
            portalBase = BuildConfig.PORTAL_BASE,
            wdPath = BuildConfig.WD_PATH,
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseAnonKey = BuildConfig.SUPABASE_ANON_KEY
        )

            setContent {
            // Determine start destination while splash screen is visible
            var startDestination by remember { mutableStateOf<RootDestination?>(null) }
            var keepOnScreenCondition by remember { mutableStateOf(true) }
            var deepLinkTarget by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(Unit) {
                val intent = intent
                if (intent?.data?.scheme == "kito" && intent.data?.host == "schedule") {
                    deepLinkTarget = "schedule"
                    // Clear intent to avoid re-triggering on rotation/re-entry
                    this@MainActivity.intent = Intent(this@MainActivity, MainActivity::class.java)
                }

                notificationPipelineController.sync()
                val onboardingDone = prefs.onBoardingFlow.first()
                val isUserSetupDone = prefs.userSetupDoneFlow.first()
                startDestination = when {
                    !onboardingDone -> RootDestination.Onboarding
                    !isUserSetupDone -> RootDestination.UserSetup
                    else -> RootDestination.Tabs
                }
                keepOnScreenCondition = false
            }

            // Handle new intents (e.g., if activity is singleTop)
            DisposableEffect(Unit) {
                val listener = Consumer<Intent> { newIntent ->
                     if (newIntent?.data?.scheme == "kito" && newIntent.data?.host == "schedule") {
                        deepLinkTarget = "schedule"
                        this@MainActivity.intent = Intent(this@MainActivity, MainActivity::class.java)
                    }
                }
                addOnNewIntentListener(listener)
                onDispose { removeOnNewIntentListener(listener) }
            }


            splashScreen.setKeepOnScreenCondition { keepOnScreenCondition }

            val resolvedStart = startDestination
            if (resolvedStart != null) {
                KitoTheme {
                    Surface {
                        val rootNavController = rememberNavController()
                        NavHost(
                            navController = rootNavController,
                            startDestination = resolvedStart,
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(
                                        durationMillis = 600,
                                        easing = ExpressiveEasing.Emphasized
                                    )
                                )
                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> -(fullWidth * 0.3f).toInt() },
                                    animationSpec = tween(
                                        durationMillis = 600,
                                        easing = ExpressiveEasing.Emphasized
                                    )
                                )
                            },
                            popEnterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { fullWidth -> -(fullWidth * 0.3f).toInt() },
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = ExpressiveEasing.Emphasized
                                    )
                                )
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = ExpressiveEasing.Emphasized
                                    )
                                )
                            }
                        ) {
                            composable<RootDestination.Onboarding> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.linearGradient(
                                                listOf(
                                                    Color(0xFF131621),
                                                    Color(0xFF0A0C12)
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    OnBoardingScreen(
                                        prefRepository = prefs,
                                        onOnboardingComplete = {
                                            rootNavController.navigate(RootDestination.UserSetup) {
                                                popUpTo(RootDestination.Onboarding) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                            composable<RootDestination.UserSetup> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.linearGradient(
                                                listOf(
                                                    Color(0xFF131621),
                                                    Color(0xFF0A0C12)
                                                )
                                            )
                                        )
                                ) {
                                    UserSetupScreen(
                                        onSetupComplete = {
                                            rootNavController.navigate(RootDestination.Tabs) {
                                                popUpTo(rootNavController.graph.id) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                            composable<RootDestination.Tabs> {
                                MainUI(
                                    deepLinkTarget = deepLinkTarget,
                                    onDeepLinkConsumed = { deepLinkTarget = null }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkForUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (
                info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        appUpdateManager.registerListener(installStateListener)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (
                info.updateAvailability() ==
                UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                )
            }
        }
        lifecycleScope.launch {
            notificationPipelineController.sync()
        }
    }

    override fun onPause() {
        super.onPause()
        appUpdateManager.unregisterListener(installStateListener)
    }

    private fun showCompleteUpdateSnackbar() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Update ready",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Restart") {
            appUpdateManager.completeUpdate()
        }.show()
    }
}
