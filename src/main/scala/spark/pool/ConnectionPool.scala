package spark.pool

import java.sql.Connection

import com.jolbox.bonecp.{BoneCP, BoneCPConfig}
import org.slf4j.LoggerFactory

/**
  * 参考资料 http://www.myexception.cn/mysql/1934808.html
  *
  * @author michael
  * @version 1.0 Created on 2017/12/27 11:05
  */
object ConnectionPool {

  val logger = LoggerFactory.getLogger(this.getClass)

  val jdbcUrl = "jdbc:mysql://192.168.9.174:3306/sparkDB"
  val userName = "root"
  val password = "123456"

  private val connectionPool = {
    try{
      Class.forName("com.mysql.jdbc.Driver")
      val config = new BoneCPConfig()
      config.setJdbcUrl(jdbcUrl)
      config.setUsername(userName)
      config.setPassword(password)
      config.setLazyInit(true)

      config.setMinConnectionsPerPartition(3)
      config.setMaxConnectionsPerPartition(5)
      config.setPartitionCount(5)
      config.setCloseConnectionWatch(true)
      config.setLogStatementsEnabled(false)

      Some(new BoneCP(config))
    } catch {
      case exception:Exception=>
        logger.warn("Error in creation of connection pool"+exception.printStackTrace())
        None
    }
  }
  def getConnection:Option[Connection] ={
    connectionPool match {
      case Some(connPool) => Some(connPool.getConnection)
      case None => None
    }
  }
  def closeConnection(connection:Connection): Unit = {
    if(!connection.isClosed) {
      logger.info("----close connection pool")
      connection.close()
    }
  }
}
