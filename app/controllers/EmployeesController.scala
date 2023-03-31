package controllers

import services.EmployeesService
import models.EmployeesData
import play.api.libs.json.Json
import play.api.mvc._
import utils.JsonUtils
import javax.inject.Inject
import java.sql.BatchUpdateException

/**
 * Created by MRM
 */
class EmployeesController @Inject()(val controllerComponents: ControllerComponents, val employeesService:EmployeesService) extends BaseController with JsonUtils{


  def getEmployee(id: Int) = Action { implicit request =>
   try {
        Ok(Json.toJson(employeesService.find(id)))
    } catch {
        case e: Exception => NotFound(errorJson("The requested resource could not be found"))
    }
     
  }

  def createEmployees = Action(parse.json) { implicit request =>
    request.body.validate[List[EmployeesData]].fold(
      errors => BadRequest(errorJson(errors)),      
      employeesDataList => { 
        try{
          val numberOfInserts = employeesService.createInBatch(employeesDataList)
          Created(okJson(s"All the employees (${numberOfInserts.sum}) have been created successfully."))
        }catch{  
          case e : BatchUpdateException => Conflict(postErrorJson(e.getMessage()))
        }    
      } 
    )
  } 

}