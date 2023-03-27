package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import org.apache.spark.sql.SparkSession
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import services.BackupService
import utils.JsonUtils

/**
 * This controller creates a back up of the tables
 * 
 */
@Singleton
class BackupController @Inject()(val controllerComponents: ControllerComponents, val backupService:BackupService) extends BaseController with JsonUtils{

  /**
   * 
   */
  def backupFromTableToAvro(tableName:String) = Action { implicit request =>
    try{
      backupService.createBackUpFromTable(tableName)
      Ok(s"Backup $tableName successfull!")
    }catch{
      case e : NoSuchElementException => BadRequest(errorJson(s"The table $tableName is not valid"))
    }
  }
}
