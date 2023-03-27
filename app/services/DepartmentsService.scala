package services

import models.{Departments,DepartmentsData}


/**
 * Created by MRM
 */
class DepartmentsService {

    def find(id: Int): DepartmentsData  = {
        Departments.find(id) match {
        case Some(department ) => DepartmentsData.fromDepartments(department)
        case None =>  throw new IllegalArgumentException("The department does not exist")
        }
    }
    
    def createInBatch (departmentsDataList: List[DepartmentsData]): List[Int]  = {

        val departmentsList: List[Departments] = departmentsDataList.map(_.dataToModel)
        Departments.batchInsert(departmentsList)
    }
}