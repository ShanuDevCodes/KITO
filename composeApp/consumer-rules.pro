# Consumer ProGuard rules for composeApp library
# These rules are automatically applied to consumers (e.g., androidApp)

# Keep Room entities and DAOs
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-keep class * extends androidx.room.RoomDatabase
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>(...);
}

# Keep serialization annotations
-keepattributes Signature
-keepattributes *Annotation*

# Keep Ktor/OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
