# Optimized ProGuard/R8 rules for Qurban Ticketing KMP
# Generated based on r8-analyzer surgical rule principles

# --- General ---
-keepattributes SourceFile,LineNumberTable,Signature,InnerClasses,EnclosingMethod,*Annotation*

# --- iText 7 ---
# Refined from broad package-wide wildcard to surgical rules
-keep class com.itextpdf.** { <init>(...); }
-keepnames class com.itextpdf.**
-keepclassmembers class com.itextpdf.** {
    @com.itextpdf.commons.actions.annotation.Reflected *;
}
# Keep service loaders for iText kernel/layout
-keep class com.itextpdf.kernel.pdf.PdfOutputIntent { *; }
-dontwarn com.itextpdf.**

# --- Bouncy Castle / Spongy Castle (iText Dependencies) ---
# Surgical rules to preserve only necessary crypto providers
-keep class org.bouncycastle.jce.provider.BouncyCastleProvider
-keep class org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider
-keep class org.bouncycastle.jcajce.provider.digest.** { *; }
-dontwarn org.bouncycastle.**
-dontwarn org.spongycastle.**

# --- Apache POI & OOXML ---
# POI uses a lot of reflection for schema loading
-keep class org.apache.poi.** { *; }
-keep interface org.apache.poi.** { *; }
-keep enum org.apache.poi.** { *; }
-keep class org.apache.xmlbeans.metadata.** { *; }

# Preserve OOXML Schema classes and members used by reflection/XMLBeans
-keep class org.openxmlformats.** { *; }
-keep interface org.openxmlformats.** { *; }
-keep enum org.openxmlformats.** { *; }
-keepnames class org.openxmlformats.**

# Apache Commons and XMLBeans soft dependencies
-dontwarn org.apache.poi.**
-dontwarn org.apache.xmlbeans.**
-dontwarn org.apache.commons.**
-dontwarn org.openxmlformats.**

# Specific ServiceLoader and XML Stream warnings
-dontwarn aQute.bnd.annotation.baseline.BaselineIgnore
-dontwarn aQute.bnd.annotation.spi.ServiceConsumer
-dontwarn aQute.bnd.annotation.spi.ServiceProvider
-dontwarn java.awt.Shape
-dontwarn javax.xml.stream.**
-dontwarn javax.xml.crypto.**
-dontwarn edu.umd.cs.findbugs.annotations.Nullable
-dontwarn edu.umd.cs.findbugs.annotations.SuppressFBWarnings

# OSGi Framework (Transitive dependencies from Apache POI / iText)
-dontwarn org.osgi.framework.Bundle
-dontwarn org.osgi.framework.BundleContext
-dontwarn org.osgi.framework.FrameworkUtil
-dontwarn org.osgi.framework.ServiceReference
-dontwarn org.osgi.framework.wiring.BundleRevision

# --- Koin ---
# Koin typically bundles its own rules, but keep constructors for reflected classes
-keepclassmembers class * {
    @org.koin.core.annotation.KoinInternalApi *;
}

# --- Kotlin Serialization ---
# Handled by compiler plugin, but keep the generated serializer fields
-keepclassmembers class * {
    public static ** Companion;
    public static ** $serializer;
}

# --- Redundant Rules Removed ---
# The following libraries bundle their own consumer rules (AGP 8.0+):
# - AndroidX Room
# - AndroidX WorkManager
# - Kotlin Coroutines
# - Jetpack Compose
# - KMPFile / FileKit
