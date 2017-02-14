package own.dnk.donotforget

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import android.content.{ContentValues, ContextWrapper}
import android.preference.PreferenceManager
import android.support.v4.app.FragmentActivity
import android.util.Log

import own.dnk.donotforget.EditItem.DialogEditItem
import own.dnk.donotforget.entity.TodoItem
import own.dnk.donotforget.main.TodoListAdapter
import own.dnk.donotforget.managers.{DBHelper, Notifications, Preferences}
import own.dnk.donotforget.newDayActivities.Alarm

/**
  * Created by Dima on 22.11.2016.
  */
class MainController(val ctx: ContextWrapper) {
  val TAG = "MainController"
  val CURRENT_NOTIFICATION = "current_notification"
  val FIRST_RUN_OVER = "not_a_first_run"
  val INITIALIZED = "initialized"

  val preferences = Preferences(ctx)

  val defaultPreferences = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext)

  val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")

  def yesterday = {
    val calendar = Calendar.getInstance()
    calendar.setTime(new Date())
    calendar.add(Calendar.DATE, -1)
    simpleDateFormat.format(calendar.getTime)
  }

  val dbHelper = new DBHelper(ctx, DBHelper.DATABASE, 1)

  def newDay = {
    val lastRecord = dbHelper.getHistoryTail(1)
    lastRecord.headOption.exists(_.date != yesterday)
  }

  def saveLastDayToBase() = {
    val cv = new ContentValues()
    preferences.getAll.foreach((arg) => {
      cv.put("date", yesterday)
      cv.put("item_id", arg._1)
      cv.put("done", arg._2)
      dbHelper.saveHistoryLineToBase(cv)
    })
  }

  def initNewDayPreferences() = {
    if (!firstRun) preferences.clear

    val todoItems = dbHelper.getTodoItems
    todoItems.foreach(i => preferences.saveOne(i._id.toString, value = false))

    todoItems.headOption.foreach(i => {
      defaultPreferences.edit().putInt(CURRENT_NOTIFICATION, i._id).commit()
      updateNotification(i._id)
    })
  }

  def firstRun = {
    if (defaultPreferences.contains(FIRST_RUN_OVER)) false
    else {
      val result = dbHelper.tableNotEmpty(DBHelper.HISTORY_TABLE)
      if (result) defaultPreferences.edit().putBoolean(FIRST_RUN_OVER, true)
      result
    }
  }

  def initNewDay() = {
      saveLastDayToBase()
      initNewDayPreferences()
  }

  def initAlarm(setForTomorrow: Boolean) = {
    if (!defaultPreferences.getBoolean(INITIALIZED, false)) {
      Alarm.setAlarm(ctx.getApplicationContext, setForTomorrow)
      defaultPreferences.edit().putBoolean(INITIALIZED, true).commit()
    }
  }

  def getTodoItems = {
    dbHelper.getTodoItems.map(
      (item: TodoItem) => {
        val doneOption = preferences.find(
          mapEntry => {
            item._id == mapEntry._1.toInt
          }
        )
        item.done = doneOption match {
          case Some(a) =>  a._2.asInstanceOf[Boolean]
          case None => false
        }
        item
      }
    )
  }

  def saveOnePreference(data: (String, Any)) = {
    preferences.saveOne(data._1, data._2.asInstanceOf[Boolean])
    ctx.asInstanceOf[MainActivity].switchAdapter()
  }

  def removePreference(keyToRemove: String) = {
    preferences.remove(keyToRemove)
    ctx.asInstanceOf[MainActivity].switchAdapter()
  }

  def updateNotification(id: Int) = {
    val currentNotificationId = defaultPreferences.getInt(CURRENT_NOTIFICATION, 0)
    if (currentNotificationId == id || currentNotificationId == 0) {
      val newTodoItemFromPreferences = preferences.find((mapEntry) => !mapEntry._2.asInstanceOf[Boolean])
        .getOrElse(("", 0))

      newTodoItemFromPreferences match {
        case ("", 0) => {//all items fulfilled or the item is deleted
          Notifications(ctx).remove()
          defaultPreferences.edit().putInt(CURRENT_NOTIFICATION, 0).commit()
        }
        case (key, _) => {
          val newTodoItemFromDB = getTodoItems.find(_._id == key.toInt).get
          Notifications(ctx).show(newTodoItemFromDB._id, newTodoItemFromDB.name)
          defaultPreferences.edit().putInt(CURRENT_NOTIFICATION, newTodoItemFromDB._id).commit()
        }
        case _ => {}
      }
    }
  }

  def addItemToBase(): Unit = {

  }

  def saveEditedItemToBase(id: Int, newValue: String) = {
    val insertedId = dbHelper.saveEditedItemToBase(id, newValue)
    if (newValue == null) {
      if (id == defaultPreferences.getInt(CURRENT_NOTIFICATION, 0)) {
      Notifications(ctx).remove()
      }
      //save to preferences
      removePreference(id.toString)
    } else {
      //save to preferences
      val preferenceValue = Preferences(ctx).get(insertedId.toString, Boolean.box(false), java.lang.Boolean.TYPE)
      try {
        val oldBool = preferenceValue.asInstanceOf[Boolean]
        saveOnePreference(insertedId.toString, oldBool)
      } catch {
        case ex: Exception => Log.e(TAG, ":", ex)
      }
    }

    if (!defaultPreferences.contains(CURRENT_NOTIFICATION)) {
      defaultPreferences.edit().putInt(CURRENT_NOTIFICATION, insertedId).commit() //do this to trigger updateNotifications because it checks if current id == new id
    }

    ctx.asInstanceOf[MainActivity].switchAdapter()
    updateNotification(insertedId)
  }

  def showDialogEditItem(id: Int) = {
    new DialogEditItem(this, id)
      .show(ctx.asInstanceOf[FragmentActivity].getSupportFragmentManager, "dialogEditItem")
  }
}

object MainController {
  var todoListAdapter: TodoListAdapter = _

  def apply(ctx: ContextWrapper) = new MainController(ctx)
}
