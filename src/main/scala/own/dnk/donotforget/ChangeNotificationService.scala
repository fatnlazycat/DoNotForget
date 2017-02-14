package own.dnk.donotforget

import android.app.IntentService
import android.content.Intent
import own.dnk.donotforget.managers.Notifications

/**
  * Created by Dima on 12.12.2016.
  */
class ChangeNotificationService extends IntentService("IntentService") {
  override def onHandleIntent(intent: Intent): Unit = {
    val notificationId = intent.getIntExtra(Notifications.NOTIFICATION_ID_EXTRA, 0)
    MainController(this).updateNotification(notificationId)
  }
}
