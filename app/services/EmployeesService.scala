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
}


