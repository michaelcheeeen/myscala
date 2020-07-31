package spark.streaming.demo4

import java.util

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import scala.collection.mutable.ArrayBuffer

/**
  * 自定义的kafka消息生产机
  *
  * @author michael
  * @version 1.0 Created on 2017/12/21 15:58
  */
class MyProducer {
  val brokers = "localhost:9092"
  // Zookeeper connection properties
  val props = new util.HashMap[String, Object]()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, Object](props)

//  val topic = "user_events"

  /**
    * 内容发送
    * @param topic 接受目标
    * @param args 内容
    */
  def send(topic : String, args: ArrayBuffer[String]) {
    if (args != null) {
      // Send some messages
      for (arg <- args) {
        println("发送消息[" + arg + "]")
        val message = new ProducerRecord[String, Object](topic, null, arg)
        producer.send(message)
      }
      Thread.sleep(500)
    }
  }

  /**
    * 键值内容发送
    * @param topic
    * @param msgs
    */
  def send1(topic : String, msgs: Map[String, Object]) {
    for (msg<-msgs){
      println("发送消息[" + msg._1 +":" + msg._2 + "]")
      val message = new ProducerRecord[String, Object](topic, msg._1, msg._2)
      producer.send(message)
    }
  }

  /**
    * 关闭生产机
    */
  def close: Unit ={
    producer.close()
  }

}
