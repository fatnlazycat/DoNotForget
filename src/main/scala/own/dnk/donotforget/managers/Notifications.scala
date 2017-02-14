package own.dnk.donotforget.managers

import android.app.{Activity, NotificationManager, PendingIntent}
import android.content.{Context, ContextWrapper, Intent}
import android.support.v7.app.NotificationCompat
import own.dnk.donotforget.{ChangeNotificationService, MainActivity, R}

/**
  * Created by Dima on 23.11.2016.
  */
class Notifications(ctx: ContextWrapper) {
  val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
  val appContext = ctx.getApplicationContext

  def show(id: Int, title: String) = {
    val intentAction = new Intent(appContext, classOf[MainActivity])
    val pendingIntent = PendingIntent.getActivity(appContext, 0 /* Request code */, intentAction, PendingIntent.FLAG_ONE_SHOT)

    val intentDelete = new Intent(appContext, classOf[ChangeNotificationService])
        .putExtra(Notifications.NOTIFICATION_ID_EXTRA, id)
    val deletePendingIntent = PendingIntent.getService(
      appContext,
      10 /* Request code should be different*/,
      intentDelete,
      PendingIntent.FLAG_UPDATE_CURRENT)

    val nBuilder = new NotificationCompat.Builder(ctx)
        .setSmallIcon(R.drawable.ic_stat_name)
        .setContentTitle(title)
        .setContentText("Do Not Forget!")
        .setContentIntent(pendingIntent)
        .setDeleteIntent(deletePendingIntent)

    notificationManager.notify(Notifications.NOTIFICATION_ID, nBuilder.build())
  }

  def remove(): Unit ={
    notificationManager.cancelAll()
  }
}

object Notifications {
  def apply(ctx: ContextWrapper): Notifications = new Notifications(ctx)

  val NOTIFICATION_ID = 0
  val NOTIFICATION_ID_EXTRA = "NOTIFICATION_ID_EXTRA"
}
