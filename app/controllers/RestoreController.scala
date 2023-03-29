package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import org.apache.spark.sql.SparkSession
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import services.RestoreService
import utils.JsonUtils
import org.apache.spark.sql.AnalysisException

/**
 * This controller creates a back up of the tables
 * 
 */
@Singleton
class RestoreController @Inject()(val controllerComponents: ControllerComponents, val restoreService:RestoreService) extends BaseController with JsonUtils{

  /**
   * 
   */
  def restoreTable(tableName:String) = Action { implicit request =>
    try{
      restoreService.restoreTable(tableName)
      Ok(s"Restore $tableName successfull!")
    }catch{ 
      case e : AnalysisException => BadRequest(errorJson(s"The Avro backup for $tableName does not exist"))
      case e : NoSuchElementException => BadRequest(errorJson(s"The table $tableName is not valid"))
    }
  }
}
