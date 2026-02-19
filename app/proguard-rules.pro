# Gson reflection models — Gson instantiates these via reflection
-keep class com.sls.handbook.core.network.model.** { *; }

# Preserve source file/line info for crash reporting
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
