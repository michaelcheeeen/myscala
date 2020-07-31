import com.ql.util.express.{DefaultContext, ExpressRunner}

import scala.runtime.Nothing$

/**
  *
  *
  * @author michael
  * @version 1.0 Created on 2017/12/19 12:11
  */
object Demo1 {
  def main(args: Array[String]): Unit = {

    /* var m1=scala.collection.mutable.Map[String,Int](("tom",12),("jim",21))
     //取值
     println("m1--->"+m1("tom"))
     //添加元素
     m1+=("jony"->2)
     println("m1="+m1)
     //遍历元素
     m1.foreach(x=> {println("m1==>"+x._1)})
     //根据key得到值得遍历
     m1.keys.foreach { x => {println("m1 的key is  【"+x)} }*/
    val runner = new ExpressRunner(false, true)
    val express: String = "a+b*c-d/e"
    var context1 = new DefaultContext[String, AnyRef]
    var a: Any = 2.20
    var b: Any = 2
    var c: Any = 3
    var d: Any = 3
    var e: Any = 1
    context1.put("a", a.toString)
    context1.put("b", b.toString)
    context1.put("c", c.toString)
    context1.put("d", d.toString)
    context1.put("e", e.toString)
    val r1 = runner.execute(express, context1, null, true, true)
    System.out.println("表达式计算： " + express + " = " + r1)
  }

}
