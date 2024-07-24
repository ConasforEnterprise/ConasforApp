# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Gson
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.examples.android.model.** { *; }
#-keep class * implements com.google.gson.TypeAdapterFactory
#-keep class * implements com.google.gson.JsonSerializer
#-keep class * implements com.google.gson.JsonDeserializer
#-keep class com.google.gson.internal.Excluder
#-keep class com.google.gson.internal.bind.TreeTypeAdapter
#-keep class com.google.gson.internal.bind.MapTypeAdapterFactory
#-keep class com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory
#-keep class com.google.gson.JsonAdapter
#-keep class com.google.gson.annotations.JsonAdapter
#-keep class com.google.gson.annotations.SerializedName
#-keep class com.google.gson.annotations.Expose
#-keep class com.google.gson.FieldNamingStrategy
#-keep class com.google.gson.internal.$Gson$Types

# Mantener clases y métodos de Android
-keep class android.** { *; }
-keep class androidx.** { *; }

# Mantener clases y métodos de Gson
-keep class com.google.gson.** { *; }