package com.kito.core.presentation.components.animation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import io.github.alexzhirkevich.compottie.LottieAnimation
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieConstants
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition

@Composable
fun PageNotFoundAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(loadLottieJson("page_not_found.json")))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}

@Composable
fun PandaSleepingAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(loadLottieJson("panda_sleeping.json")))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}

@Composable
fun LockAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(loadLottieJson("lock.json")))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1,
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}

@Composable
fun NoInternetAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(loadLottieJson("no_internet_connection.json")))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1,
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}

/**
 * Load Lottie JSON from resources. Returns empty JSON as fallback.
 * Lottie files should be placed in commonMain/composeResources/files/
 */
private fun loadLottieJson(fileName: String): String {
    // TODO: Load from Compose resources once resource loading is set up
    // For now return minimal valid Lottie JSON to prevent crashes
    return "{\"v\":\"5.5.7\",\"fr\":30,\"ip\":0,\"op\":60,\"w\":100,\"h\":100,\"layers\":[]}"
}
