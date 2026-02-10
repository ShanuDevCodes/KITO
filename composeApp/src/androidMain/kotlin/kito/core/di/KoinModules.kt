package com.kito.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.kito.BuildConfig
import com.kito.core.common.connectivity.ConnectivityObserver
import com.kito.core.database.AppDB
import com.kito.core.database.repository.AttendanceRepository
import com.kito.core.database.repository.SectionRepository
import com.kito.core.database.repository.StudentRepository
import com.kito.core.database.repository.StudentSectionRepository
import com.kito.core.datastore.PrefsRepository
import com.kito.core.datastore.ProtoDatastoreRepository
import com.kito.core.datastore.SecurePrefs


import com.kito.core.network.supabase.SupabaseRepository
import com.kito.core.presentation.components.AppSyncUseCase
import com.kito.core.presentation.components.StartupSyncGuard
import com.kito.feature.app.presentation.AppViewModel
import com.kito.feature.attendance.presentation.AttendanceListScreenViewModel
import com.kito.feature.auth.presentation.UserSetupViewModel
import com.kito.feature.exam.presentation.UpcomingExamViewModel
import com.kito.feature.faculty.presentation.FacultyDetailViewModel
import com.kito.feature.faculty.presentation.FacultyScreenViewModel
import com.kito.feature.home.presentation.HomeViewModel
import com.kito.feature.schedule.presentation.ScheduleScreenViewModel
import com.kito.feature.settings.presentation.SettingsViewModel
import com.kito.sap.SapPortalClient
import com.kito.sap.SapRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val DATASTORE_NAME = "app_prefs"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

val appModule = module {
    // Context-dependent providers
    single { androidContext().dataStore }
    
    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDB::class.java,
            "kito_db"
        ).build()
    }
    
    // DAOs
    single { get<AppDB>().attendanceDao() }
    single { get<AppDB>().studentDao() }
    single { get<AppDB>().sectionDao() }
    single { get<AppDB>().studentSectionDao() }
    
    
    // Ktor HttpClient for Supabase
    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 15000
            }
            defaultRequest {
                url(BuildConfig.SUPABASE_URL)
                header("apikey", BuildConfig.SUPABASE_ANON_KEY)
                header("Authorization", "Bearer ${BuildConfig.SUPABASE_ANON_KEY}")
                header("Accept", "application/json")
                header("Content-Type", "application/json")
                header("Prefer", "return=representation")
            }
        }
    }

    
    // Coroutine Scope
    single(named("ApplicationScope")) { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    
    // Repositories & Services
    singleOf(::SapPortalClient)
    singleOf(::SapRepository)
    singleOf(::SupabaseRepository)
    singleOf(::AttendanceRepository)
    singleOf(::SectionRepository)
    singleOf(::StudentRepository)
    singleOf(::StudentSectionRepository)
    single { ConnectivityObserver(androidContext(), get(named("ApplicationScope"))) }
    singleOf(::PrefsRepository)
    singleOf(::SecurePrefs)
    singleOf(::ProtoDatastoreRepository)
    singleOf(::StartupSyncGuard)
    singleOf(::AppSyncUseCase)
}

val viewModelModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AttendanceListScreenViewModel)
    viewModelOf(::UserSetupViewModel)
    viewModelOf(::UpcomingExamViewModel)
    viewModelOf(::FacultyScreenViewModel)
    viewModelOf(::FacultyDetailViewModel)
    viewModelOf(::ScheduleScreenViewModel)
    viewModelOf(::SettingsViewModel)
}

fun initKoin(appContext: Context) {
    startKoin {
        androidContext(appContext)
        modules(appModule, viewModelModule)
    }
}
