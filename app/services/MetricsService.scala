package services

import org.apache.spark.sql._
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import models.{Jobs,JobsData}
import com.typesafe.config.ConfigFactory
import models.{HiredByQuarterData,HiredByDepartmentData}
import utils.SparkUtils



/**
 * Created by MRM
 */
class MetricsService @Inject()(val sparkUtils : SparkUtils)  {

    
    def createMetricsHiredByQuarter (): List[HiredByQuarterData] = {


        val session = sparkUtils.createSparkSession()

        val employeesDF = sparkUtils.dfFromDataBaseTable(session,"hired_employees")
        employeesDF.createOrReplaceTempView("employees")

        val departmentsDF = sparkUtils.dfFromDataBaseTable(session,"departments")
        departmentsDF.createOrReplaceTempView("departments")


        val jobsDF = sparkUtils.dfFromDataBaseTable(session,"jobs")
        jobsDF.createOrReplaceTempView("jobs")



        val hiredByQuarter= session.sql(
            """SELECT 
                d.department ,
                j.job ,
                COUNT(CASE WHEN quarter(he.datetime) = 1 THEN he.id ELSE NULL END) AS Q1,
                COUNT(CASE WHEN quarter(he.datetime) = 2 THEN he.id ELSE NULL END) AS Q2,
                COUNT(CASE WHEN quarter(he.datetime) = 3 THEN he.id ELSE NULL END) AS Q3,
                COUNT(CASE WHEN quarter(he.datetime) = 4 THEN he.id ELSE NULL END) AS Q4
            FROM 
                employees he
                INNER JOIN departments d ON he.department_id = d.id
                INNER JOIN jobs j ON he.job_id = j.id
            WHERE 
                YEAR(he.datetime) = 2021
            GROUP BY 
                d.department, 
                j.job
            ORDER BY 
                d.department ASC, 
                j.job ASC"""
        )


        val hiredByQuarterDatas = hiredByQuarter.collect().map { row =>
            val department = row.getAs[String]("department")
            val job = row.getAs[String]("job")
            val q1 = row.getAs[Long]("Q1")
            val q2 = row.getAs[Long]("Q2")
            val q3 = row.getAs[Long]("Q3")
            val q4 = row.getAs[Long]("Q4")
            HiredByQuarterData(department, job, q1, q2, q3, q4)
        }.toList

        session.stop()

        hiredByQuarterDatas
    }

    


    def createMetricsHiredByDepartment (): List[HiredByDepartmentData] = {


        val spark = sparkUtils.createSparkSession()


        val employeesDF = sparkUtils.dfFromDataBaseTable(spark,"hired_employees")
        employeesDF.createOrReplaceTempView("employees")

        val departmentsDF = sparkUtils.dfFromDataBaseTable(spark,"departments")
        departmentsDF.createOrReplaceTempView("departments")

        val hiredByDepartment = spark.sql(
            """SELECT 
                d.id, 
                d.department AS department, 
                COUNT(he.id) AS hired
            FROM 
                employees he
                INNER JOIN departments d ON he.department_id = d.id
            GROUP BY 
                d.id, 
                d.department
            HAVING 
                hired > (SELECT AVG(count_hired_employees) FROM (SELECT COUNT(id) AS count_hired_employees FROM employees WHERE YEAR(datetime) = 2021 GROUP BY department_id) AS avg_hired_employees)
            ORDER BY 
                hired DESC"""
        )

        val hiredByDepartmentDatas = hiredByDepartment.collect().map { row =>
            val id = row.getAs[Integer]("id").toInt            
            val department = row.getAs[String]("department")
            val hired = row.getAs[Long]("hired").toInt
            HiredByDepartmentData(id, department, hired)
        }.toList

        spark.stop()

        hiredByDepartmentDatas


    }
}