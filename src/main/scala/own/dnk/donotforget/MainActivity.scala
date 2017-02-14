package own.dnk.donotforget

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.{Button, ListView}
import own.dnk.donotforget.main.TodoListAdapter
import own.dnk.donotforget.managers.{FirebaseDBManager, Notifications}

/**
 * Created by Dima on 21.11.2016.
 */
class MainActivity extends AppCompatActivity {
  var controller: MainController = _
  var notifications: Notifications = _

  lazy val lvMain = findViewById(R.id.lvMain).asInstanceOf[ListView]
  lazy val buttonAdd = findViewById(R.id.buttonAdd).asInstanceOf[Button]

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    notifications = new Notifications(this)

    controller = MainController(this)

    controller.initAlarm(false)

    MainController.todoListAdapter = new TodoListAdapter(controller)

    lvMain.setAdapter(MainController.todoListAdapter)

    new FirebaseDBManager().method()

  }

  override def onDestroy(): Unit = {
    controller = null
    notifications = null
    super.onDestroy()
  }

  def itemButtonClick(view: View): Unit = {
    view match {
      case `buttonAdd` => {
        controller.showDialogEditItem(-1)
      }
    }
  }

  def switchAdapter(): Unit ={
    lvMain.setAdapter(new TodoListAdapter(controller))
  }

  //def makeNotification(title: String) = notifications.show(title)
}
