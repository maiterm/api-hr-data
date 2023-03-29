package services

import org.apache.spark.sql._
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import models.{Jobs,JobsData}
import com.typesafe.config.ConfigFactory
import utils.SparkUtils



/**
 * Created by MRM
 */
class RestoreService  @Inject()(val sparkUtils : SparkUtils) {

    
    def restoreTable(tableName: String): String = {


        val tableNamesMap = Map(
            "employees" -> "hired_employees",
            "jobs" -> "jobs",
            "departments" -> "departments"
        )
        
        val config = ConfigFactory.load()

        val realTableName =  tableNamesMap.get(tableName)
            .getOrElse(throw new NoSuchElementException(s"The table $tableName is not valid"))

        val session = sparkUtils.createSparkSession()

        val path = config.getString("path.backup")+"/"+realTableName+"/*"
        // conf database from application.conf

        val restoreDF = session.read.format("avro").load(path)
            
        sparkUtils.dfToDataBaseTable(realTableName, restoreDF)
        

        session.stop()

        "Succesfull restore"
    }
}