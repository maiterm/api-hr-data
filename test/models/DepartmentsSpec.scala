package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class DepartmentsSpec extends Specification {

  "Departments" should {

    val d = Departments.syntax("d")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Departments.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Departments.findBy(sqls.eq(d.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Departments.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Departments.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Departments.findAllBy(sqls.eq(d.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Departments.countBy(sqls.eq(d.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Departments.create(id = 123, department = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Departments.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Departments.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Departments.findAll().head
      val deleted = Departments.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Departments.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Departments.findAll()
      entities.foreach(e => Departments.destroy(e))
      val batchInserted = Departments.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
