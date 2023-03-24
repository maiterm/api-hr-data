package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class JobsSpec extends Specification {

  "Jobs" should {

    val j = Jobs.syntax("j")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Jobs.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Jobs.findBy(sqls.eq(j.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Jobs.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Jobs.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Jobs.findAllBy(sqls.eq(j.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Jobs.countBy(sqls.eq(j.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Jobs.create(id = 123, job = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Jobs.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Jobs.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Jobs.findAll().head
      val deleted = Jobs.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Jobs.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Jobs.findAll()
      entities.foreach(e => Jobs.destroy(e))
      val batchInserted = Jobs.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
