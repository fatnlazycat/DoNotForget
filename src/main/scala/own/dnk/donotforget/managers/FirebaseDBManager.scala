package own.dnk.donotforget.managers

import android.util.Log
import com.google.android.gms.tasks.{OnCompleteListener, OnFailureListener, OnSuccessListener, Task}
import com.google.firebase.database.{FirebaseDatabase, Logger}
import own.dnk.donotforget.entity.HistoryItem

/**
  * Created by Dima on 13.02.2017.
  */
class FirebaseDBManager {
  val TAG = "FirebaseDBManager"

  FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG)
  val database = FirebaseDatabase.getInstance().getReference

  def method() {
    val item = HistoryItem("2017-03-03", 1, done = true)

    database.child("history").setValue(item)
      .addOnCompleteListener(new OnCompleteListener[Void] {
        override def onComplete(task: Task[Void]): Unit = {
          Log.d(TAG, "task completed")
        }
      })
      .addOnFailureListener(new OnFailureListener {
        override def onFailure(e: Exception): Unit = {
          Log.d(TAG, "task failed with " + e)
        }
      })
        .addOnSuccessListener(new OnSuccessListener[Void] {
          override def onSuccess(tResult: Void): Unit = {
            Log.d(TAG, "task successful")
          }
        })

  }
}
