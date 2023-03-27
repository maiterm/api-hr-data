package controllers

import services.JobsService
import models.JobsData
import play.api.libs.json.Json
import play.api.mvc._
import utils.JsonUtils
import javax.inject.Inject
import java.sql.BatchUpdateException

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
       errors => BadRequest(errorJson(errors)),  
      jobsDataList => {
        try{
          val numberOfInserts = jobsService.createInBatch(jobsDataList)
          Ok(okJson(s"Jobs created successfully. ${numberOfInserts.sum} is the amount of inserted jobs."))
        }catch{
          case e : BatchUpdateException => Conflict(postErrorJson(e.getMessage()))
        }        

      }
    )
  }

}