package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class EmployeesSpec extends Specification {

  "Employees" should {

    val he = Employees.syntax("he")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Employees.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Employees.findBy(sqls.eq(he.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Employees.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Employees.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Employees.findAllBy(sqls.eq(he.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Employees.countBy(sqls.eq(he.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Employees.create(id = 123)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Employees.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Employees.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Employees.findAll().head
      val deleted = Employees.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Employees.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Employees.findAll()
      entities.foreach(e => Employees.destroy(e))
      val batchInserted = Employees.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
