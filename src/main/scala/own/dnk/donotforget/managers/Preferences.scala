package own.dnk.donotforget.managers

import android.app.Activity
import android.content.{ContextWrapper, SharedPreferences}
import android.util.Log

import scala.collection.JavaConverters._

/**
  * Created by Dima on 03.12.2016.
  */
class Preferences(val ctx: ContextWrapper) {
  val TAG = "Preferences class"
  val preferences = ctx.getSharedPreferences("mainActivityPreferences", 0)

  def get[T](key: String, default: Object, cl: Class[T] ) = {
    val className = cl.getSimpleName.replace("$", "") //remove trailing $ in class name
    val classNameFirstCap = className.splitAt(1)._1.toUpperCase + className.splitAt(1)._2
    val methodName = "get" + classNameFirstCap
    try {
      val result = classOf[SharedPreferences].getMethod(methodName, classOf[String], cl)
        .invoke(preferences, key, default)
        .asInstanceOf[T]

      result //return

    } catch {
      case ex: Exception => Log.e(TAG, ":", ex)
        ex //return
    }
  }

  def getAll = {
    preferences.getAll.asScala.map(a => {
      val intFromBool: java.lang.Integer = if (a._2.asInstanceOf[Boolean]) 1 else 0
      val javaInt: java.lang.Integer = a._1.toInt
      (javaInt, intFromBool)
    })
  }

  def clear = {
    preferences.edit().clear().commit()
  }

  def saveOne(key: String, value: Boolean) = {
    preferences.edit().putBoolean(key, value).commit()
  }

  def remove(key: String) = {
    preferences.edit().remove(key).commit()
  }

  def find(f: (((String, Any)) => Boolean)) = {
    preferences.getAll.asScala.find(f)
  }
}

object Preferences {
  def apply(ctx: ContextWrapper): Preferences = new Preferences(ctx)
}
