-dontskipnonpubliclibraryclasses
-verbose
-dontpreverify

-optimizations !class/merging/*,!code/allocation/variable
-optimizationpasses 5
-useuniqueclassmembernames
-ignorewarnings

 # Get rid of warnings about unreachable but unused classes referred to by Netty
-dontwarn org.jboss.netty.**
-dontwarn com.parse.**
-dontwarn com.google.api.client.**

#Some important internal fields that where removed
-keep class org.jboss.netty.channel.DefaultChannelPipeline{volatile <fields>;}

#A Factory which has a static factory implementation selector which is pruned
-keep class org.jboss.netty.util.internal.QueueFactory{static <fields>;}

#Some fields whose names need to be maintained because they are accessed using inflection
-keepclassmembernames class org.jboss.netty.util.internal.**{*;}

# To show source file name and line number when crashed...
-keepattributes SourceFile,LineNumberTable

#Some Factory that seemed to be pruned
-keep class java.util.concurrent.atomic.AtomicReferenceFieldUpdater {*;}
-keep class java.util.concurrent.atomic.AtomicReferenceFieldUpdaterImpl{*;}

-keep public class *Fragment* {
        <methods>;
}

-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepattributes *Annotation*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.view.ViewGroup

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }
-keep class com.slidingmenu.lib.** { *; }
-keep interface com.slidingmenu.lib.** { *; }

-keepattributes *Annotation*


# Needed by commons logging
-keep class org.apache.commons.logging.* {*;}
-keep class org.apache.http.**
-keep interface org.apache.http.**

-keep public class com.google.gson.** {*;}
-keepattributes Signature

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

-dontwarn android.support.**
