package own.dnk.donotforget.newDayActivities

import android.content.{BroadcastReceiver, Context, Intent}

/**
  * Created by Dima on 05.12.2016.
  */
class AlarmReceiver extends BroadcastReceiver{
  override def onReceive(context: Context, intent: Intent): Unit = {
    context.startService(new Intent(context, classOf[InitDayService]))
  }
}
