package models

import play.api.libs.json._
import java.time.{LocalDate, ZonedDateTime,OffsetDateTime}

/**
 * Created by MRM
 * Data Transfer Object for Employees
 * 
 */
case class HiredByQuarterData(
  department: String ,
  job: String,
  Q1: Long,
  Q2: Long,
  Q3: Long,
  Q4: Long ) {



}

object HiredByQuarterData {
  implicit val HiredByQuarterDataReads = Json.reads[HiredByQuarterData]
  
  implicit val HiredByQuarterDataWrites = Json.writes[HiredByQuarterData]
}
