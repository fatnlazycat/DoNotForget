package own.dnk.donotforget.main

import android.view.View
import android.widget.CompoundButton
import own.dnk.donotforget.MainController
import own.dnk.donotforget.entity.TodoItem

/**
  * Created by Dima on 23.11.2016.
  */
class Controller(val mainController: MainController) extends CompoundButton.OnCheckedChangeListener
  with View.OnLongClickListener {
  override def onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean): Unit = {
    val id = buttonView.getTag.asInstanceOf[TodoItem]._id
    mainController.saveOnePreference((id.toString, isChecked))
    mainController.updateNotification(id)
  }

  override def onLongClick(v: View): Boolean = {
    mainController.showDialogEditItem(v.getTag.asInstanceOf[TodoItem]._id)
    true
  }
}
