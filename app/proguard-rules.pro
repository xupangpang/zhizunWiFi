# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/wukun/android-sdk-mac_x86/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*


# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#如果有引用v4包可以添加下面这行
#-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.** { *; }
#如果引用了v4或者v7包
-dontwarn android.support.*
#忽略警告
-ignorewarning


#####################记录生成的日志数据,gradle build时在本项目根目录输出################
 #混淆时是否记录日志
-verbose
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt



#####################记录生成的日志数据，gradle build时 在本项目根目录输出-end################


#####混淆保护自己项目的部分代码以及引用的第三方jar包library - start #######


#如果不想混淆 keep 掉  保留一个完整的包
#-keep class com.lippi.recorder.iirfilterdesigner.** {*; }
#项目特殊处理代码
#忽略警告
#-dontwarn com.lippi.recorder.utils**


#如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
#//原因分析，可能是高版本的 sdk 通过 proguard 混淆代码时默认已经将 lib目录中的 jar 都已经添加到打包脚本中，所以不需要再次手动添加
# 混淆jar
#-libraryjars libs/gson-2.2.4.jar
# 混淆类
#-keep class sun.misc.Unsafe { *; }
# 混淆包
#-keep class com.google.gson.examples.android.model.** { *; }
#dialog
-keep class me.drakeet.materialdialog.** { *; }
#加载框
-keep class com.kaopiz.kprogresshud.** { *; }
#下拉刷新
-keep class in.srain.cube.views.ptr.** { *; }
#实体类不混淆

-keep class com.ousrslook.shimao.commen.ioc.** { *; } #不能混淆 否则注解无效
-keep class com.ousrslook.shimao.model.** { *; } #不能混淆
-keep class com.ousrslook.shimao.net.XaResult{ *; }#统一返回的实体类泛型不能混淆
#-keep class com.ousrslook.shimao.net.** { *; }


####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####


-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}


#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}


#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}


#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}


#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable


#保持 Serializable 不被混淆并且enum 类也不被混淆
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


#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}


-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}


#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
 -keep class **.R$* { *; }
#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}


