package services

import models.{Employees,EmployeesData}


/**
 * Created by MRM
 */
class EmployeesService {

    def find(id: Int): EmployeesData  = {
        Employees.find(id) match {
        case Some(employee ) => EmployeesData.fromEmployees(employee)
        case None =>  throw new IllegalArgumentException("The employee does not exist")
        }
    }

    def createInBatch (employeesDataList: List[EmployeesData]): List[EmployeesData]  = {
        val employeesList: List[Employees] = employeesDataList.map(_.dataToModel)
        Employees.batchInsert(employeesList)
        employeesDataList
    } 
}