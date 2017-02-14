package own.dnk.donotforget.newDayActivities

import android.app.IntentService
import android.content.Intent
import own.dnk.donotforget.MainController

/**
  * Created by Dima on 05.12.2016.
  */
class InitDayService extends IntentService("InitDayService"){
  override def onHandleIntent(intent: Intent): Unit = {
    Alarm.setAlarm(this.getApplicationContext, true)
    MainController(this).initNewDay()
  }
}
