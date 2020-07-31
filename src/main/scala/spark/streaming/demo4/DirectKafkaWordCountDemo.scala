package streaming.demo4

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils

/**
  *从 Kafka集群中消费原始“message: 我是第n条信息“并把数据进行截断，过滤处理，输出为“我是第n条信息“
  *
  * @author michael
  * @version 1.0 Created on 2017/12/13 13:38
  */
object DirectKafkaWordCountDemo {
  def main(args: Array[String]) {
    //local[2]表示在本地建立2个working线程
    //当运行在本地，如果你的master URL被设置成了“local”，这样就只有一个核运行任务。这对程序来说是不足的，因为作为receiver的输入DStream将会占用这个核，这样就没有剩余的核来处理数据了。
    //所以至少得2个核，也就是local[2]
    val sprakConf = new SparkConf().setMaster("local[2]").setAppName("DirectKafkaWordCountDemo")
    //此处在idea中运行时请保证local[2]核心数大于2
//    sprakConf.setMaster("local[2]")
    val ssc = new StreamingContext(sprakConf, Seconds(5))

    // Create direct kafka stream with brokers and topics
    val brokers = "localhost:9092"
    val topics = "user_events"
    val topicSet = topics.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicSet)

    // Get the lines, split them into words, count the words and print
    val lines = messages.map(_._2)
    val words = lines.flatMap(_.split(" ")).filter(!_.equals("message:"))
    val wordCounts = words.map(x=>(x, 1L)).reduceByKey(_ + _)
    wordCounts.print()

    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }

}
