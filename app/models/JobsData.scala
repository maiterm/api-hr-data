package models

import play.api.libs.json._

/**
 * Created by MRM
 * Data Transfer Object for Jobs
 * 
 */
case class JobsData(
  id: Int,
  job: String) {

  def dataToModel: Jobs  = Jobs(id, job)
  def create: Jobs = Jobs.create(id, job)

  def update: Int => Jobs =
    id => Jobs(id, job).save()

}

object JobsData {
  implicit val JobsDataReads = Json.reads[JobsData]
  
  implicit val JobsDataWrites = Json.writes[JobsData]
  
  def fromJobs(j: Jobs): JobsData = JobsData(j.id, j.job)
}
