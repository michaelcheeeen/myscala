package streaming.demo4

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import spark.streaming.demo4.MyProducer

import scala.collection.mutable.ArrayBuffer

/**
  *
  *
  * @author michael
  * @version 1.0 Created on 2017/12/13 13:41
  */
object KafkaConsumer {

  // 屏蔽不必要的日志显示在终端上
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
  Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)

  def main(args: Array[String]): Unit = {
    run()
  }

  def run(): Unit = {
    //    val zkQuorum = "192.168.100.48:2181"
    val zkQuorum = "localhost:2181"
    val group = "spark-streaming-test"
    val topics = "user_events"
    val numThreads = 1

    val sparkConf = new SparkConf().setAppName("KafkaConsumerDemo")
    sparkConf.setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    ssc.checkpoint("checkpoint")

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val lines =
      KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
    val array = ArrayBuffer[String]()
    lines.foreachRDD(rdd => {
      val count = rdd.count().toInt;
      rdd
        .take(count + 1)
        .take(count)
        .foreach(x => {
          array += x + "--read"
        })

      println("接受消息123:" + array)

      // 注意这行会循环发送
      val kafkaProducer  = new MyProducer
      kafkaProducer.send(topics, array)

      array.clear()
    })
    ssc.start()
    ssc.awaitTermination()
  }

}
