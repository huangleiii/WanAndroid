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

####################################################################################
#                                                                                  #
#                                        基本规则                                  #
#                                                                                  #
####################################################################################

-optimizationpasses 5                  # 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-dontusemixedcaseclassnames            # 混合时不使用大小写混合，混合后的类名为小写
-dontskipnonpubliclibraryclasses       # 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclassmembers  # 指定不去忽略非公共库的类成员
-verbose                               # 这句话能够使我们的项目混淆后产生映射文件
                                       # 包含有类名->混淆后类名的映射关系
-ignorewarning                         #忽略警告
-dontpreverify                         # 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-optimizations !code/simplification/cast,!field/*,!class/merging/*# 指定混淆是采用的算法，后面的参数是一个过滤器
                                                                  # 这个过滤器是谷歌推荐的算法，一般不做更改
-keepattributes *Annotation*,InnerClasses# 保留Annotation不混淆
-keepattributes Signature                # 避免混淆泛型
-keepattributes SourceFile,LineNumberTable# 抛出异常时保留代码行号

####################################################################################
#                                                                                  #
#               记录生成的日志数据,gradle build时在本项目根目录输出                  #
#                                                                                  #
####################################################################################

#apk 包内所有 class 的内部结构
-dump proguard/class_files.txt
#未混淆的类和成员
-printseeds proguard/seeds.txt
#列出从 apk 中删除的代码
-printusage proguard/unused.txt
#混淆前后的映射
-printmapping proguard/mapping.txt


####################################################################################
#                                                                                  #
#                   Android开发中一些需要保留的公共部分                              #
#                                                                                  #
####################################################################################
#保留我们使用的四大组件，自定义的Application等等这些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.view.View
-keep public class * extends android.preference.Preference
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep class android.support.** {*;}
-keep public class * extends android.support.annotation.**

# support-v4
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }


# support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }

# support design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }


# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
public void *(android.view.View);
}

#R文件下面的资源
-keep class **.R$* {
 *;
}
# natvie 方法不混淆（JNI）
-keepclasseswithmembernames class * {
    native <methods>;
}
#枚举
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#Serializable序列化
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#Parcelable序列化
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

#webview
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
####################################################################################
#                                                                                  #
#                               项目中特殊处理部分                                  #
#                                                                                  #
####################################################################################

#--------------------------------处理反射类-----------------------------------


#--------------------------------处理js交互-----------------------------------


#--------------------------------处理实体类--------------------------------------
-keep class com.huanglei.wanandroid.model.bean.** { *; }


#-------------------------------处理第三方依赖库------------------------------------

#com.jakewharton:butterknife-compiler:9.0.0-rc1
#-ButterKnife 7.0以后
 -keep class butterknife.** { *; }
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *; }
 -keepclasseswithmembernames class * {
  @butterknife.* <fields>;
 }
 -keepclasseswithmembernames class * {
 @butterknife.* <methods>;
 }


#    com.google.code.gson:gson:2.8.5
#gson
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }



#    com.squareup.retrofit2:retrofit:2.5.0
#retrofit2.x
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions



#    com.squareup.okhttp3:okhttp:3.12.0
#okhttp3.x
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase


#    io.reactivex.rxjava2:rxandroid:2.1.0
#    io.reactivex.rxjava2:rxjava:2.x.x
#Rxjava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


#    com.github.franmontiel:PersistentCookieJar:v1.0.1
    -dontwarn com.franmontiel.persistentcookiejar.**
    -keep class com.franmontiel.persistentcookiejar.**


#    com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.30
    -keep class com.chad.library.adapter.** {
    *;
    }
    -keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
    -keep public class * extends com.chad.library.adapter.base.BaseViewHolder
    -keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
         <init>(...);
    }

#    com.github.bumptech.glide:glide:3.7.0
    -keep public class * implements com.bumptech.glide.module.GlideModule
    -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
    }


#    com.just.agentweb:agentweb:4.0.2
    -keep class com.just.agentweb.** {
        *;
    }
    -dontwarn com.just.agentweb.**


#    com.youth.banner:banner:1.4.10
    -keep class com.youth.banner.** {
        *;
     }


#    org.greenrobot:greendao:3.2.2
-keep class org.greenrobot.greendao.**{*;}
-keep public interface org.greenrobot.greendao.**
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class net.sqlcipher.database.**{*;}
-keep public interface net.sqlcipher.database.**
-dontwarn net.sqlcipher.database.**
-dontwarn org.greenrobot.greendao.**


#    com.github.yuweiguocn:GreenDaoUpgradeHelper:v2.1.0
    -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
        public static void dropTable(org.greenrobot.greendao.database.Database, boolean);
        public static void createTable(org.greenrobot.greendao.database.Database, boolean);
    }





