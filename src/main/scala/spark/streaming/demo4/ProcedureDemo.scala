package spark.streaming.demo4

import java.sql.Connection
import java.text.SimpleDateFormat
import java.util.Date

import com.ql.util.express.DefaultContext
import kafka.serializer.StringDecoder
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext, Time}
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import spark.pool.ConnectionPool

/**
  * 测试数据格式 ： __clientip=10.10.9.153&paymentstatus=0&__opip=&memberid=89385239&iamount=1&itype=16&oper_res=1&channeltype=8&__timestamp=1457252427&productid=112&selectbank=&icount=0&ordersrc=web&paymentip=61.159.104.134&orderdate=2016-03-06 16:19:55&subjecttype=zheanaiMessenger&oper_type=1&paydate=&orderamount=259.0&paymentchannel=16&oper_time=2016-03-06 16:20:27&orderid=127145727&iunit=month&bussinessid=80125727&isuse=0
  *
  *
  * @author michael
  * @version 1.0 Created on 2017/12/13 13:38
  */
object ProcedureDemo {
  val logger = LoggerFactory.getLogger(this.getClass)
  val simpleformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  val dateFormat = new SimpleDateFormat("yyyyMMdd")
  val timeFormat = new SimpleDateFormat("HH:mm")

  /* case class result(ftime: String, hour: String, orderid: Long, memberid: Long, platform: String, iamount: Double, orderamount: Double) extends Serializable {
     override def toString: String = "%s\t%s\t%d\t%d\t%s\t%.2f\t%.2f".format(ftime, hour, orderid, memberid, platform, iamount, orderamount)
   }*/

  case class result(ftime: String, no: Long, express : String, result: Object) extends Serializable {
    override def toString: String = "%s\t%d\t%s\t%.2f".format(ftime, no, express, result)
  }

  def getFormatDate(date: Date, format: SimpleDateFormat): String = {
    format.format(date)
  }

  def stringFormatTime(time: String, simpleformat: SimpleDateFormat): Date = {
    simpleformat.parse(time)
  }

  // kafka中的value解析为Map
  def valueSplit(value: String): Map[String, String] = {
    val x = value.split("&")
    var valueMap: Map[String, String] = Map()
    x.foreach { kvs =>
      if (!kvs.startsWith("__")) {
        val kv = kvs.split("=")
        if (kv.length == 2) {
          valueMap += (kv(0) -> kv(1))
        }
      }
    }
    valueMap
  }

  // 数据库写入
  def insertIntoMySQL(con: Connection, sql: String, data: result): Unit = {
    // println(data.toString)
    try {
      val ps = con.prepareStatement(sql)
      ps.setString(1, data.ftime)
      ps.setLong(2, data.no)
      ps.setString(3, data.express)
      ps.setObject(4, data.result)

      ps.executeUpdate()
      ps.close()
    } catch {
      case exception: Exception =>
        logger.error("Error in execution of query " + exception.getMessage + "\n-----------------------\n" + exception.printStackTrace() + "\n-----------------------------")
    }
  }

  def createContext(zkqurm: String, groupId: String, topic: scala.Predef.Map[String, Int], checkPointDir: String): StreamingContext = {
    val sql = "insert into compute(ftime,serialNo,express,result) values(?,?,?,?);"
    val sprakConf = new SparkConf().setMaster("local[3]").setAppName("Scala Streaming read kafka")
//    val sc = new SparkContext(sprakConf)
    val ssc = new StreamingContext(sprakConf, Seconds(2))
//    val totalcounts = sc.accumulator(0L, "Total count")

    val lines = KafkaUtils.createStream(ssc, zkqurm, groupId, topic).map(_._2)

    val filterRecord = lines.filter(x => !x.isEmpty).map(valueSplit).map { x =>
      val orderdate = x.getOrElse("orderdate", null)
      val serialNo = x.getOrElse("no", null)
//      val day = getFormatDate(orderdate, simpleformat)
//      val hour = getFormatDate(orderdate, timeFormat)
      val a = x.getOrElse("a", null)
      val b = x.getOrElse("b", null)
      val c = x.getOrElse("c", null)
      val express = "a+b*c"
      var context = new DefaultContext[String, AnyRef]
      context.put("a", a)
      context.put("b", b)
      context.put("c", c)
      var qlExpress = new QLExpress()

      val res = new result(
        orderdate, serialNo.toLong, express, qlExpress.ql(express, context)
      )
      println(" res: "+res.toString)

      res
    }

    filterRecord.foreachRDD((x: RDD[result], time: Time) => {
      if (!x.isEmpty()) {
        // 打印一下这一批batch的处理时间段以及累计的有效记录数(不含档次)
        println("--" + new DateTime(time.milliseconds).toString("yyyy-MM-dd HH:mm:ss") + "--totalcounts:" + /*totalcounts.value */ "-----")
        x.foreachPartition { res => {
          if (!res.isEmpty) {
            val connection = ConnectionPool.getConnection.getOrElse(null)
            res.foreach {
              r: result =>
//                totalcounts.add(1L)
                insertIntoMySQL(connection, sql, r)
            }
            ConnectionPool.closeConnection(connection)
          }
        }
        }
      }
    })

    ssc
  }

  // =================================================================
  def main(args: Array[String]): Unit = {
    val zkqurm = "localhost:2181"
    val groupId = "spark-streaming-procedure"
    //    val topic = scala.Predef.Map("user_events" -> 30)
    val topics = "test1"
    val numThreads = 1
    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val checkPointDir = "checkpoint-procedure"

    val ssc = StreamingContext.getOrCreate(checkPointDir,
      () => {
        createContext(zkqurm, groupId, topicMap, checkPointDir)
      })
    ssc.start()
    ssc.awaitTermination()
  }

}
