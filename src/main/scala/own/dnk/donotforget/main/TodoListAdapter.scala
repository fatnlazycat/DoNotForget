package own.dnk.donotforget.main

import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.{BaseAdapter, CheckBox}
import own.dnk.donotforget.entity.TodoItem
import own.dnk.donotforget.{MainController, R}

/**
  * Created by Dima on 21.11.2016.
  */
class TodoListAdapter(mainController: MainController) extends BaseAdapter{

  var todoItems = getTodoItems

  def getTodoItems = {
    mainController.getTodoItems.sortWith((item1: TodoItem, item2: TodoItem) => item1._id > item2._id)
  }

  override def getCount: Int = todoItems.size

  override def getItemId(position: Int): Long = position

  override def getItem(position: Int): AnyRef = todoItems(position)

  override def getView(position: Int, convertView: View, parent: ViewGroup): View = {
    var resultView = convertView
    if (resultView == null) {
      val inflater = LayoutInflater.from(parent.getContext)
      resultView=inflater.inflate(R.layout.list_item_checkbox, parent, false)
    }

    val thisItem = todoItems(position)

    val checkbox = resultView.findViewById(R.id.checkbox).asInstanceOf[CheckBox]
    checkbox.setText(thisItem.name)
    checkbox.setChecked(thisItem.done)
    checkbox.setTag(thisItem)

    val listener = new Controller(mainController)
    checkbox.setOnCheckedChangeListener(listener)
    checkbox.setOnLongClickListener(listener)

    resultView
  }

  override def notifyDataSetChanged(): Unit = {
    getTodoItems
    super.notifyDataSetChanged()
  }
}
