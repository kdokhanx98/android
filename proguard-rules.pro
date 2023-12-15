# native methods
-keep class com.b44t.messenger.** { *; }
-keep class org.thoughtcrime.securesms.util.FileUtils* { *; }

# bug with video recoder
-keep class com.coremedia.iso.** { *; }

# unused SealedData constructor needed by JsonUtils
-keep class org.thoughtcrime.securesms.crypto.KeyStoreHelper* { *; }

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn com.google.android.gms.common.GoogleApiAvailability
-dontwarn com.google.android.gms.location.FusedLocationProviderClient
-dontwarn com.google.android.gms.location.LocationCallback
-dontwarn com.google.android.gms.location.LocationRequest
-dontwarn com.google.android.gms.location.LocationServices
-dontwarn com.google.android.gms.tasks.OnFailureListener
-dontwarn com.google.android.gms.tasks.OnSuccessListener
-dontwarn com.google.android.gms.tasks.Task

-keep class androidx.startup.AppInitializer
-keepnames class * extends androidx.startup.Initializer
# These Proguard rules ensures that ComponentInitializers are are neither shrunk nor obfuscated,
# and are a part of the primary dex file. This is because they are discovered and instantiated
# during application startup.
-keep class * extends androidx.startup.Initializer {
    # Keep the public no-argument constructor while allowing other methods to be optimized.
    <init>();
}
