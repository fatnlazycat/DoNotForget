scalaVersion := "2.11.8"

enablePlugins(AndroidApp)
enablePlugins(AndroidGms)
android.useSupportVectors

versionCode := Some(1)
version := "0.1-SNAPSHOT"

instrumentTestRunner :=
  "android.support.test.runner.AndroidJUnitRunner"

platformTarget := "android-25"

javacOptions in Compile ++= "-source" :: "1.7" :: "-target" :: "1.7" :: Nil

libraryDependencies ++=
  "com.android.support" % "appcompat-v7" % "24.0.0" ::
  "com.android.support.test" % "runner" % "0.5" % "androidTest" ::
  "com.android.support.test.espresso" % "espresso-core" % "2.2.2" % "androidTest" ::
  "com.google.firebase" % "firebase-database" % "10.0.1" ::
  "com.google.android.gms" % "play-services-gcm" % "10.0.1" ::
  Nil

useProguard in Android := true
useProguardInDebug in Android := (useProguard in Android).value

proguardOptions ++= io.Source.fromFile("proguard-rules.pro").getLines().toSeq
