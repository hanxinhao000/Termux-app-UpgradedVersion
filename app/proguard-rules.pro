# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-ignorewarnings

-dontwarn javax.**
-keep public class main.java.com.termux.android_cm.commands.main.raw.* { public *; }
-keep public abstract class main.java.com.termux.android_cm.commands.main.generals.* { public *; }
-keep public class main.java.com.termux.android_cm.commands.tuixt.raw.* { public *; }
-keep public class main.java.com.termux.android_cm.managers.notifications.NotificationService
-keep public class main.java.com.termux.android_cm.managers.notifications.KeeperService
-keep public class main.java.com.termux.android_cm.managers.options.**
-keep class main.java.com.termux.android_cm.tuils.libsuperuser.**
-keep class main.java.com.termux.android_cm.managers.suggestions.HideSuggestionViewValues
-keep public class it.andreuzzi.comparestring2.**

-dontwarn main.java.com.termux.android_cm.commands.main.raw.**

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

-dontwarn org.htmlcleaner.**
-dontwarn com.jayway.jsonpath.**
-dontwarn org.slf4j.**

-dontwarn org.jdom2.**
