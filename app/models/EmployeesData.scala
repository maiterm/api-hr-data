package models

import play.api.libs.json._
import models.Employees
import java.time.{LocalDate, ZonedDateTime}

/**
 * Created by MRM
 * Data Transfer Object for Employees
 * 
 */
case class EmployeesData(
  id: Int,
  name: Option[String] = None,
  datetime: Option[LocalDate] = None,
  departmentId: Option[Int] = None,
  jobId: Option[Int] = None) {

  def dataToModel: Employees = Employees(id, name, datetime, departmentId, jobId)

  def create: Employees = Employees.create(id, name, datetime, departmentId, jobId)

  def update: Int => Employees =
    id => Employees(id, name, datetime, departmentId, jobId).save()

}

object EmployeesData {
  implicit val EmployeesDataReads = Json.reads[EmployeesData]
  
  implicit val EmployeesDataWrites = Json.writes[EmployeesData]
  
  def fromEmployees(e: Employees): EmployeesData = EmployeesData(e.id, e.name, e.datetime, e.departmentId, e.jobId)
}
