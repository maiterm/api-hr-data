package models

import play.api.libs.json._

/**
 * Created by MRM
 * Data Transfer Object for Departments
 * 
 */
case class DepartmentsData(
  id: Int,
  department: String) {

  def dataToModel: Departments  = Departments(id, department)

  def create: Departments = Departments.create(id, department)

  def update: Int => Departments =
    id => Departments(id, department).save()

}

object DepartmentsData {
  implicit val departmentsDataReads = Json.reads[DepartmentsData]
  
  implicit val departmentsDataWrites = Json.writes[DepartmentsData]
  
  def fromDepartments(d: Departments): DepartmentsData = DepartmentsData(d.id, d.department)
}
