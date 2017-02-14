package own.dnk.donotforget.EditItem

import android.app.AlertDialog
import android.content.{DialogInterface, SharedPreferences}
import android.widget.EditText
import own.dnk.donotforget.{MainActivity, MainController}

/**
  * Created by Dima on 22.11.2016.
  */
class Controller(controller: MainController, id: Int, editText: EditText)
  extends DialogInterface.OnClickListener {

  override def onClick(dialog: DialogInterface, which: Int): Unit = {
    val text = Option(editText) match {
      case Some(a) => a.getText.toString
      case None => null
    }
    controller.saveEditedItemToBase(id, text)
    dialog.asInstanceOf[AlertDialog].getOwnerActivity.asInstanceOf[MainActivity].switchAdapter()
    dialog.dismiss()
  }
}
