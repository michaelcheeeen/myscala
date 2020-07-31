name := "myscala"

version := "1.0"

scalaVersion := "2.10.4"

// libraryDependencies += "net.sf.json-lib" % "json-lib" % "2.4" from "http://repo1.maven.org/maven2/net/sf/json-lib/json-lib/2.4/json-lib-2.4-jdk15.jar"

libraryDependencies += "org.codehaus.jettison" % "jettison" % "1.3.8"

libraryDependencies += "org.apache.commons" % "commons-pool2" % "2.2"

libraryDependencies += "redis.clients" % "jedis" % "2.5.2"

libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "2.0.0"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "2.0.0"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.6.3"
libraryDependencies += "org.apache.spark" % "spark-mllib_2.10" % "1.6.3" % "provided"

libraryDependencies += "com.taobao.util" % "taobao-express" % "3.1.7"


libraryDependencies += "joda-time" % "joda-time" % "2.9.2"
libraryDependencies += "com.jolbox" % "bonecp" % "0.8.0.RELEASE"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.29"

// 添加测试代码编译或者运行期间使用的依赖
// libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "1.8" % "test")

resolvers += " Maven Repository [nexus]        " at "http://192.168.9.200:8002/nexus/content/groups/public/"
resolvers += " Maven Repository [ibiblio]      " at "http://maven.ibiblio.org/maven2/"
resolvers += " Maven Repository [mvnrepository]" at "https://mvnrepository.com/artifact/"

