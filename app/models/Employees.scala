package models

import scalikejdbc._
import java.time.{LocalDate, ZonedDateTime}

case class Employees(
  id: Int,
  name: Option[String] = None,
  datetime: Option[LocalDate] = None,
  departmentId: Option[Int] = None,
  jobId: Option[Int] = None) {

  def save()(implicit session: DBSession = Employees.autoSession): Employees = Employees.save(this)(session)

  def destroy()(implicit session: DBSession = Employees.autoSession): Int = Employees.destroy(this)(session)

}


object Employees extends SQLSyntaxSupport[Employees] {

  override val tableName = "hired_employees"

  override val columns = Seq("id", "name", "datetime", "department_id", "job_id")

  def apply(he: SyntaxProvider[Employees])(rs: WrappedResultSet): Employees = apply(he.resultName)(rs)
  def apply(he: ResultName[Employees])(rs: WrappedResultSet): Employees = new Employees(
    id = rs.get(he.id),
    name = rs.get(he.name),
    datetime = rs.get(he.datetime),
    departmentId = rs.get(he.departmentId),
    jobId = rs.get(he.jobId)
  )

  val he = Employees.syntax("he")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Employees] = {
    withSQL {
      select.from(Employees as he).where.eq(he.id, id)
    }.map(Employees(he.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Employees] = {
    withSQL(select.from(Employees as he)).map(Employees(he.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Employees as he)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Employees] = {
    withSQL {
      select.from(Employees as he).where.append(where)
    }.map(Employees(he.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Employees] = {
    withSQL {
      select.from(Employees as he).where.append(where)
    }.map(Employees(he.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Employees as he).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    id: Int,
    name: Option[String] = None,
    datetime: Option[LocalDate] = None,
    departmentId: Option[Int] = None,
    jobId: Option[Int] = None)(implicit session: DBSession = autoSession): Employees = {
    withSQL {
      insert.into(Employees).namedValues(
        column.id -> id,
        column.name -> name,
        column.datetime -> datetime,
        column.departmentId -> departmentId,
        column.jobId -> jobId
      )
    }.update.apply()

    Employees(
      id = id,
      name = name,
      datetime = datetime,
      departmentId = departmentId,
      jobId = jobId)
  }

  def batchInsert(entities: collection.Seq[Employees])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(String, Any)]] = entities.map(entity =>
      Seq(
        "id" -> entity.id,
        "name" -> entity.name,
        "datetime" -> entity.datetime,
        "departmentId" -> entity.departmentId,
        "jobId" -> entity.jobId))
    SQL("""insert into hired_employees(
      id,
      name,
      datetime,
      department_id,
      job_id
    ) values (
      {id},
      {name},
      {datetime},
      {departmentId},
      {jobId}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Employees)(implicit session: DBSession = autoSession): Employees = {
    withSQL {
      update(Employees).set(
        column.id -> entity.id,
        column.name -> entity.name,
        column.datetime -> entity.datetime,
        column.departmentId -> entity.departmentId,
        column.jobId -> entity.jobId
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Employees)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Employees).where.eq(column.id, entity.id) }.update.apply()
  }

}
