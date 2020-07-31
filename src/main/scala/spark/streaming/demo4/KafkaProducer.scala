package streaming.demo4

import org.joda.time.DateTime
import spark.streaming.demo4.MyProducer

import scala.util.Random

/**
  * 字符串当作数据发送到Kafka集群中
  *
  * @author michael
  * @version 1.0 Created on 2017/12/13 13:39
  */
object KafkaProducer {
  val kafkaProducer = new MyProducer



  def main(args: Array[String]): Unit = {
    BigProduce()
  }
  def StandAlone():Unit={
    //    val array = ArrayBuffer("AA", "BA")
    //    kafkaProducer.send(array)

    val topic = "test1"
    //    val map = Map("a"->"2.3", "b"->"3")
    val map = Map("a" -> "__clientip=10.10.9.153&paymentstatus=0&__opip=&a=21&b=3&c=41&&orderdate=2017-11-06 16:19:55")
    kafkaProducer.send1(topic, map)
  }

  private val random = new Random()

  def BigProduce():Unit ={
    val topic = "test1"
    var a = 0
    var b = 0
    var c = 0
    var orderdate = ""
    var x = 1
    while(x<1000000){
      a= random.nextInt(10)
      b= random.nextInt(5)
      c= random.nextInt(99)
      orderdate = new DateTime().toString("yyyy-MM-dd HH:mm:ss.sss")
      var msg = "__clientip=10.10.9.153&paymentstatus=0&__opip=&a="+ a +"&b="+ b+ "&c="+c+"&&orderdate="+orderdate+"&no="+x
      val map = Map("a" -> msg)
      kafkaProducer.send1(topic, map)
      println("MG NO = "+ x)
      x+=1
//      Thread.sleep(2000)
    }
    kafkaProducer.close

  }


}
