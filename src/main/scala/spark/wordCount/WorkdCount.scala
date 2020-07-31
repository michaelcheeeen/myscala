package wordCount

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  *
  *
  * @author michael
  * @version 1.0 Created on 2017/11/30 10:17
  */
object WorkdCount {
  def main(args: Array[String]): Unit = {
    println("this is my first word count")

    val conf = new SparkConf()
    conf.setMaster("local[2]").setAppName("WordCount")

//        val sc = new SparkContext(conf)
//        val rdd = sc.textFile("file:///E://OnKeyDetector.log")
//        println("count OnKeyDetector.log = " + rdd.count())


    //设置duration
    val ssc = new StreamingContext(conf, Seconds(5))

    /**
      * 1.设置数据来源
      */
    //from socket, port= 9999
//    val receiverInputStreaming = ssc.socketTextStream("localhost", 9999, StorageLevel.MEMORY_AND_DISK)
    //from File
    val dStreaming = ssc.textFileStream("E://OnKeyDetector.log")

    println("DStreaming............is " + dStreaming)
    dStreaming.print()
    //from db, hdfs ...

    /**
      * 2.flatMap
      *  通过，分割
      */
    val words = dStreaming.flatMap{_.split(",")}
      //每个单词记为1
    val pairs = words.map(word=>(word, 1))

    /**
      * 3.reduce
      * 通过将局部样本相同单词的个数相加， 得到总体的单词个数
      */
    val word_count =pairs.reduceByKey(_+_)

    /**
      * 4.打印、启动
      */
    word_count.print()
    //print不会触发JOB， 因为目前代码仍处于Streaming控制下，具体的触发取决于设置duration的时间间隔
    ssc.start()
    //等待程序结束
    ssc.awaitTermination()

  }

}
