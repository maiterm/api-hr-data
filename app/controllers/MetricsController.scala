package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import org.apache.spark.sql.SparkSession
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import services.MetricsService
import utils.JsonUtils
import play.api.libs.json._
/**
 * This controller creates a back up of the tables
 * 
 */
@Singleton
class MetricsController @Inject()(val controllerComponents: ControllerComponents, val metricsService:MetricsService) extends BaseController with JsonUtils{

  /**
   * 
   */
  def getHiredByQuarter() = Action { implicit request =>
    val metrics = metricsService.createMetricsHiredByQuarter()
    val metricsJson = Json.obj("HiredByQuarter" ->
      metrics.map {m => Json.toJson(m)})
    Ok(metricsJson)
  }

  def getHiredByDepartment() = Action { implicit request =>

    val metrics = metricsService.createMetricsHiredByDepartment()
    val metricsJson = Json.obj("HiredByDepartment" ->
      metrics.map {m => Json.toJson(m)})
    Ok(metricsJson)

  }
}
