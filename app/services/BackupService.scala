package services

import org.apache.spark.sql._
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import models.{Jobs,JobsData}
import com.typesafe.config.ConfigFactory



/**
 * Created by MRM
 */
class BackupService {

    
    def createBackUpFromTable (tableName: String): String = {


        val tableNamesMap = Map(
            "employees" -> "hired_employees",
            "jobs" -> "jobs",
            "departments" -> "departments"
        )

        val realTableName =  tableNamesMap.get(tableName)
            .getOrElse(throw new NoSuchElementException(s"The table $tableName is not valid"))


        val spark = SparkSession.builder()
            .appName("backup-to-avro")
            .master("local[*]")
            .getOrCreate()


        val config = ConfigFactory.load()

        val path = config.getString("path.backup")+"/"+realTableName
        // conf database from application.conf
        val driver = config.getString("db.default.driver")
        val url = config.getString("db.default.url")
        val username = config.getString("db.default.username")
        val password = config.getString("db.default.password")
        val backupDF = spark.read.format("jdbc")
                .option("url", url)
                .option("driver", driver)
                .option("dbtable", realTableName)
                .option("user", username)
                .option("password", password)
                .load()
        

        backupDF.write.format("avro")
            .mode(SaveMode.Overwrite)
            .save(path)

        "Succesfull backup"
    }
}