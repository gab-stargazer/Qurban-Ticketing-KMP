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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.itextpdf.** { *; }
-keep class org.apache.** { *; }
-keep class org.spongycastle.** { *; }
-keep class org.bouncycastle.** { *; }
-dontwarn com.itextpdf.**
-dontwarn org.apache.**
-dontwarn org.spongycastle.**
-dontwarn org.bouncycastle.**

#   Generated from IDE suggestion mapping rules
-dontwarn aQute.bnd.annotation.spi.ServiceProvider
-dontwarn java.awt.Shape
-dontwarn javax.xml.stream.XMLEventFactory
-dontwarn javax.xml.stream.XMLEventReader
-dontwarn javax.xml.stream.XMLInputFactory
-dontwarn javax.xml.stream.XMLOutputFactory
-dontwarn javax.xml.stream.XMLResolver
-dontwarn javax.xml.stream.util.XMLEventAllocator

# Apache POI
-keep class org.apache.poi.** { *; }
-dontwarn org.apache.poi.**

# XMLBeans
-keep class org.apache.xmlbeans.** { *; }
-dontwarn org.apache.xmlbeans.**

# OOXML schemas
-keep class org.openxmlformats.** { *; }
-dontwarn org.openxmlformats.**

# Commons Compress
-keep class org.apache.commons.** { *; }
-dontwarn org.apache.commons.**