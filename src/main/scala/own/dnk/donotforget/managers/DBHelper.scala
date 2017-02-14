package own.dnk.donotforget.managers

import android.content.{ContentValues, Context}
import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}
import android.util.Log
import own.dnk.donotforget.MainActivity
import own.dnk.donotforget.entity.{HistoryItem, TodoItem}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Dima on 21.11.2016.
  */
class DBHelper(val context: Context, name: String, version: Int) extends
  SQLiteOpenHelper(context, name, null, version) {
  override def onCreate(db: SQLiteDatabase): Unit = {
    db.execSQL("CREATE TABLE "
      + DBHelper.TODO_ITEMS_TABLE
      + " ("
      + "_id"
      + " INTEGER PRIMARY KEY AUTOINCREMENT,"
      + "name" + " TEXT"
      + ");"
    )

    db.execSQL("CREATE TABLE "
      + DBHelper.HISTORY_TABLE
      + " ("
      + "_id"
      + " INTEGER PRIMARY KEY AUTOINCREMENT,"
      + "date"
      + " TEXT,"
      + "item_id" + " INTEGER,"
      + "done" + " INTEGER" //no boolean type - use 0 or 1
      + ");"
    )

  }

  def getTodoItems = {
    var result = new ArrayBuffer[TodoItem]()
    val db = getReadableDatabase

    val table = DBHelper.TODO_ITEMS_TABLE
    val columns: Array[String] = null
    val where = null
    val selectionArgs: Array[String] = null
    val cursor = db.query(table, columns, where, selectionArgs, null, null, null)
    while (cursor.moveToNext()) {
      val id  = cursor.getInt(cursor.getColumnIndex("_id"))
      val name = cursor.getString(cursor.getColumnIndex("name"))
      val todoItem = TodoItem(id,name)
      result += todoItem
    }
    db.close()
    result
  }

  def getHistoryTail(numLines: Int) = {
    var result = new ArrayBuffer[HistoryItem]()
    val db = getReadableDatabase
    val table = DBHelper.HISTORY_TABLE
    val columns: Array[String] = null
    val where = null //"date=?"
    val selectionArgs: Array[String] = null// = Array[String](today)
    val cursor = db.query(table, columns, where, selectionArgs, null, null, null)
    if (cursor.moveToLast() && numLines > 0) {
      var i = 0
      do {
        val date = cursor.getString(cursor.getColumnIndex("date"))
        val item_id = cursor.getInt(cursor.getColumnIndex("item_id"))
        val done = cursor.getInt(cursor.getColumnIndex("done")) != 0 //to produce boolean
        val historyItem = HistoryItem(date, item_id, done)
        result += historyItem
        i += 1
      } while (cursor.moveToPrevious() && i < numLines)
    }
    result
  }

  def tableNotEmpty(table: String) = {
    val db = getReadableDatabase
    val columns: Array[String] = null
    val selectionArgs: Array[String] = null
    val cursor = db.query(table, columns, null, selectionArgs, null, null, null)
    val notEmpty = cursor.getCount > 0
    db.close()
    notEmpty
  }

  def saveHistoryLineToBase(cv: ContentValues) = {
    val db = getWritableDatabase
    db.insert(DBHelper.HISTORY_TABLE, null, cv)
    db.close()
  }

  def saveEditedItemToBase(id: Int, newValue: String) = {
    var insertedId = id
    val db = getWritableDatabase
    val table = DBHelper.TODO_ITEMS_TABLE
    val whereClause = "_id=?"
    val whereArgs: Array[String] = Array(id.toString)
    if (newValue == null) {
      db.delete(table, whereClause, whereArgs)
    } else {
      val cv = new ContentValues()
      cv.put("name", newValue)
      if (id < 0) {
        insertedId = db.insert(table, null, cv).toInt
      } else {
        db.update(table, cv, whereClause, whereArgs)
      }
    }
    db.close()
    insertedId
  }

  override def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int): Unit = {}
}

object DBHelper {
  val DATABASE = "donotforgetdatabase"
  val TODO_ITEMS_TABLE = "todo_items_table"
  val HISTORY_TABLE = "history_table"

  //var instance: DBHelper = _
}
