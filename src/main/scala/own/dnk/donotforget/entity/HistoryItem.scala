package own.dnk.donotforget.entity

import scala.beans.{BeanProperty, BooleanBeanProperty}
/**
  * Created by Dima on 05.12.2016.
  */
class HistoryItem {
  var _id: Int = _
  @BeanProperty var date: String = _
  @BeanProperty var item_id: Int = _
  @BooleanBeanProperty var done: Boolean = _
}

object HistoryItem {
  def apply(date: String, item_id: Int, done: Boolean): HistoryItem = {
    val result = new HistoryItem()
    result.date = date
    result.item_id = item_id
    result.done = done
    result
  }
}
