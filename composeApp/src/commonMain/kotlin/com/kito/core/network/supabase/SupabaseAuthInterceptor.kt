package com.kito.core.network.supabase

import com.kito.core.platform.AppConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Creates an authenticated Ktor HttpClient for Supabase API calls.
 * Replaces the OkHttp SupabaseAuthInterceptor with Ktor's DefaultRequest plugin.
 */
fun createSupabaseClient(): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(DefaultRequest) {
            url(AppConfig.supabaseUrl)
            header("apikey", AppConfig.supabaseAnonKey)
            header("Authorization", "Bearer ${AppConfig.supabaseAnonKey}")
            contentType(ContentType.Application.Json)
            header("Prefer", "return=representation")
        }
    }
}
