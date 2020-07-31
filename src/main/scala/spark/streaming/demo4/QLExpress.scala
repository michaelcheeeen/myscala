package spark.streaming.demo4

import com.ql.util.express.{DefaultContext, ExpressRunner}

/**
  *
  *
  * @author michael
  * @version 1.0 Created on 2017/12/19 12:11
  */
class QLExpress {
  def ql(express: String, context: DefaultContext[String, AnyRef]): Object = {
    val runner = new ExpressRunner(false, true)
    val r1 = runner.execute(express, context, null, true, true)
//    System.out.println("表达式计算： " + express + " = " + r1)
    r1
  }

}
