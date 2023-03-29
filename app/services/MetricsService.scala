package services

import org.apache.spark.sql._
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import models.{Jobs,JobsData}
import com.typesafe.config.ConfigFactory
import models.{HiredByQuarterData,HiredByDepartmentData}



/**
 * Created by MRM
 */
class MetricsService {

    
    def createMetricsHiredByQuarter (): List[HiredByQuarterData] = {


        val session = SparkSession.builder()
            .appName("metrics")
            .master("local[*]")
            .getOrCreate()



        val config = ConfigFactory.load()

        // conf database from application.conf
        val driver = config.getString("db.default.driver")
        val url = config.getString("db.default.url")
        val username = config.getString("db.default.username")
        val password = config.getString("db.default.password")

        val employeesDF = session.read.format("jdbc")
                .option("url", url)
                .option("driver", driver)
                .option("dbtable", "hired_employees")
                .option("user", username)
                .option("password", password)
                .load()
        employeesDF.createOrReplaceTempView("employees")

        val jobsDF = session.read.format("jdbc")
                .option("url", url)
                .option("driver", driver)
                .option("dbtable", "jobs")
                .option("user", username)
                .option("password", password)
                .load()     
        jobsDF.createOrReplaceTempView("jobs")

        val departmentsDF = session.read.format("jdbc")
                .option("url", url)
                .option("driver", driver)
                .option("dbtable", "departments")
                .option("user", username)
                .option("password", password)
                .load()        
        departmentsDF.createOrReplaceTempView("departments")

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


        val spark = SparkSession.builder()
            .appName("metrics")
            .master("local[*]")
            .getOrCreate()


        val config = ConfigFactory.load()

        // conf database from application.conf
        val driver = config.getString("db.default.driver")
        val url = config.getString("db.default.url")
        val username = config.getString("db.default.username")
        val password = config.getString("db.default.password")

        val employeesDF = spark.read.format("jdbc")
                .option("url", url)
                .option("driver", driver)
                .option("dbtable", "hired_employees")
                .option("user", username)
                .option("password", password)
                .load()
        employeesDF.createOrReplaceTempView("employees")

        val departmentsDF = spark.read.format("jdbc")
                .option("url", url)
                .option("driver", driver)
                .option("dbtable", "departments")
                .option("user", username)
                .option("password", password)
                .load()        
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