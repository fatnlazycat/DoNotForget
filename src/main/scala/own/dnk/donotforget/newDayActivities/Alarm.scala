package own.dnk.donotforget.newDayActivities

import java.util.Calendar

import android.app.{AlarmManager, PendingIntent}
import android.content.{Context, Intent}

/**
  * Created by Dima on 05.12.2016.
  */
object Alarm{
  def setAlarm(context: Context, setForTomorrow: Boolean): Unit ={
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE).asInstanceOf[AlarmManager]
    val intent = new Intent(context, classOf[AlarmReceiver])
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

    val midnight = {
      val result = Calendar.getInstance()
      result.set(Calendar.HOUR_OF_DAY, 23)
      result.set(Calendar.MINUTE, 50)
      result.add(Calendar.DAY_OF_MONTH, setForTomorrow.compare(false))
      result
    }

    alarmManager.set(AlarmManager.RTC, midnight.getTimeInMillis, pendingIntent)
  }
}
