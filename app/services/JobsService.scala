package services

import models.{Jobs,JobsData}


/**
 * Created by MRM
 */
class JobsService {

    def find(id: Int): JobsData  = {
        Jobs.find(id) match {
        case Some(job ) => JobsData.fromJobs(job)
        case None =>  throw new IllegalArgumentException("The job does not exist")
        }
    }
    
    def createInBatch (jobsDataList: List[JobsData]): List[Int]  = {

        val jobsList: List[Jobs] = jobsDataList.map(_.dataToModel)
        Jobs.batchInsert(jobsList)
    }
}