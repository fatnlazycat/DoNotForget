-dontwarn com.google.android.gms.internal.*

-keep class com.google.android.gms.dynamic.*
-keep class com.google.android.gms.internal.*
-keep class com.google.firebase.database.connection.idl.*
-keep class com.google.android.gms.dynamite.descriptors.com.google.android.gms.firebase_database.ModuleDescriptor {
    java.lang.String MODULE_ID;
    int MODULE_VERSION;
}
-keep class com.google.android.gms.dynamite.DynamiteModule$DynamiteLoaderClassLoader {
    java.lang.ClassLoader sClassLoader;
}