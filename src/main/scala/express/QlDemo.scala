import com.ql.util.express.ExpressRunner

/**
  *
  *
  * @author michael
  * @version 1.0 Created on 2017/12/19 12:11
  */
object QlDemo {
  def main(args: Array[String]): Unit = {

//        val express = "10 * 10 + 1 + 2 * 3 + 5 * 2"
//    val express = "3 > 2 and 5 > 3 "
//    val express = "2.20 + 2.20 * 2.20"
    val express = "2.2 + 2 *3 - 3/1"
//    val express = "max(3,4,5)"
//    val express = "round(19.08,1)"
//    val express = "31 in (3,4,5)"
    val runner = new ExpressRunner(false, true)
    val r = runner.execute(express, null, null, true, true)
    System.out.println("表达式计算：" + express + " = " + r)
  }

}
