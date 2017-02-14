package own.dnk.donotforget.EditItem

import android.app.{AlertDialog, Dialog}
import android.content.{Context, SharedPreferences}
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.{LayoutInflater, View}
import android.widget.EditText
import own.dnk.donotforget.{MainController, R}

/**
 * Created by Dima on 22.11.2016.
 */
class DialogEditItem(mainController: MainController, id: Int) extends DialogFragment {

  override def onCreateDialog(savedInstanceState: Bundle): Dialog = {
    val text  = mainController.getTodoItems.find(_._id == id).map(_.name).getOrElse("")
    val builder = new AlertDialog.Builder(getActivity)
    val inflater = getContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]
    val view = inflater.inflate(R.layout.dialog_with_edit_text, null)
    view.findViewById(R.id.etEditItem).asInstanceOf[EditText].setText(text)
    builder.setView(view)
      .setPositiveButton(R.string.ok, new Controller(mainController, id, view.findViewById(R.id.etEditItem).asInstanceOf[EditText]))
      .setNegativeButton("Delete", new Controller(mainController, id, null))
      .create()
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
  }
}