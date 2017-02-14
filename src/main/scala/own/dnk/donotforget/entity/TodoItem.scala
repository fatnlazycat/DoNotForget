package own.dnk.donotforget.entity

/**
  * Created by Dima on 23.11.2016.
  */
class TodoItem {

  var _id: Int = _
  var name: String = _
  var done: Boolean = false
}

object TodoItem {
  def apply(_id: Int, name: String, done: Boolean = false) = {
    val result = new TodoItem
    result._id = _id
    result.name = name
    result.done = done
    result
  }
}
