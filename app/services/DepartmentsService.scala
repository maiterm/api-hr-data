package services

import models.{Departments,DepartmentsData}


/**
 * Created by MRM
 */
class DepartmentsService {

    def find(id: Int): DepartmentsData  = {
        Departments.find(id) match {
        case Some(employee ) => DepartmentsData.fromDepartments(employee)
        case None =>  throw new IllegalArgumentException("The employee does not exist")
        }
    }
    
    def createInBatch (departmentsDataList: List[DepartmentsData]): List[DepartmentsData]  = {

        val departmentsList: List[Departments] = departmentsDataList.map(_.dataToModel)
        Departments.batchInsert(departmentsList)
        departmentsDataList
    }
}