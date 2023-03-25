package controllers

import services.JobsService
import models.JobsData
import play.api.libs.json.Json
import play.api.mvc._
import utils.JsonUtils
import javax.inject.Inject

/**
 * Created by MRM
 */
class JobsController @Inject()(val controllerComponents: ControllerComponents, val jobsService:JobsService) extends BaseController with JsonUtils{


  def getJob(id: Int) = Action { implicit request =>
   try {
        Ok(Json.toJson(jobsService.find(id)))
    } catch {
        case e: Exception => NotFound(errorJson("The requested resource could not be found"))
    }
     
  }

  def createJobs = Action(parse.json) { implicit request =>
    request.body.validate[List[JobsData]].fold(
       errors => BadRequest(errorsJson(errors)),      
      jobsDataList => {
        jobsService.createInBatch(jobsDataList)
        val resultJson = Json.obj("message" -> "Jobs created successfully")
        Ok(resultJson)
        
      }
    )
  }

}