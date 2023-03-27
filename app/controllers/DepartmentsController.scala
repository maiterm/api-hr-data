package controllers

import services.DepartmentsService
import models.DepartmentsData
import play.api.libs.json.Json
import play.api.mvc._
import utils.JsonUtils
import javax.inject.Inject
import java.sql.BatchUpdateException

/**
 * Created by MRM
 */
class DepartmentsController @Inject()(val controllerComponents: ControllerComponents, val departmentsService:DepartmentsService) extends BaseController with JsonUtils{


  def getDepartment(id: Int) = Action { implicit request =>
   try {
        Ok(Json.toJson(departmentsService.find(id)))
    } catch {
        case e: Exception => NotFound(errorJson("The requested resource could not be found"))
    }
     
  }

  def createDepartments = Action(parse.json) { implicit request =>
    request.body.validate[List[DepartmentsData]].fold(
       errors => BadRequest(errorJson(errors)),      
      departmentsDataList => {
        try{        
          val numberOfInserts = departmentsService.createInBatch(departmentsDataList)
          Ok(okJson(s"All the departments (${numberOfInserts.sum}) have been created successfully."))
        }catch{   
          case e : BatchUpdateException => Conflict(postErrorJson(e.getMessage()))
        }         
      }
    )
  }

}