package services

import models.{Jobs,JobsData}


/**
 * Created by MRM
 */
class JobsService {

    def find(id: Int): JobsData  = {
        Jobs.find(id) match {
        case Some(employee ) => JobsData.fromJobs(employee)
        case None =>  throw new IllegalArgumentException("The employee does not exist")
        }
    }
    
    def createInBatch (jobsDataList: List[JobsData]): List[Int]  = {

        val jobsList: List[Jobs] = jobsDataList.map(_.dataToModel)
        Jobs.batchInsert(jobsList)
    }
}