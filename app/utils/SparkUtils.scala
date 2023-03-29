package utils

//import play.api.libs.json._
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql._
import java.util.Properties
import java.sql.DriverManager
/**
 * Created by MRM
 */
class SparkUtils {

  def createSparkSession(): SparkSession = {
     SparkSession.builder()
            .appName("metrics")
            .master("local[*]")
            .getOrCreate()
  }

  def getUrlAndProperties(): (String,Properties) = {
    val config = ConfigFactory.load()
    val url = config.getString("db.default.url")
    val properties = new Properties()
    properties.put("user", config.getString("db.default.username"))
    properties.put("password", config.getString("db.default.password"))
    properties.put("driver", config.getString("db.default.driver"))
    (url, properties)
  }



  def dfFromDataBaseTable( session : SparkSession, tableName: String) : DataFrame = {

    val (url, properties) = getUrlAndProperties()
    session.read.jdbc(url,tableName,properties)
  }

  def dfToDataBaseTable(  tableName: String, restoreDF :DataFrame) :  String = {
    val (url, properties) = getUrlAndProperties()
    val conection = DriverManager.getConnection(url, properties)
    conection.prepareStatement(s"DROP TABLE IF EXISTS $tableName").executeUpdate()
    conection.close()

    restoreDF.write.jdbc(url,tableName,properties)
    "Done"
  }

}
