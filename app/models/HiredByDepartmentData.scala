package models

import play.api.libs.json._
import java.time.{LocalDate, ZonedDateTime,OffsetDateTime}

/**
 * Created by MRM
 * Data Transfer Object for Employees
 * 
 */
case class HiredByDepartmentData(
  id: Int,
  department: String ,
  hired: Long ) {



}

object HiredByDepartmentData {
  implicit val HiredByDepartmentDataReads = Json.reads[HiredByDepartmentData]
  
  implicit val HiredByDepartmentDataWrites = Json.writes[HiredByDepartmentData]
 
}
