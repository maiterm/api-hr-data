package models

import scalikejdbc._

case class Departments(
  id: Int,
  department: String) {

  def save()(implicit session: DBSession = Departments.autoSession): Departments = Departments.save(this)(session)

  def destroy()(implicit session: DBSession = Departments.autoSession): Int = Departments.destroy(this)(session)

}


object Departments extends SQLSyntaxSupport[Departments] {

  override val tableName = "departments"

  override val columns = Seq("id", "department")

  def apply(d: SyntaxProvider[Departments])(rs: WrappedResultSet): Departments = apply(d.resultName)(rs)
  def apply(d: ResultName[Departments])(rs: WrappedResultSet): Departments = new Departments(
    id = rs.get(d.id),
    department = rs.get(d.department)
  )

  val d = Departments.syntax("d")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Departments] = {
    withSQL {
      select.from(Departments as d).where.eq(d.id, id)
    }.map(Departments(d.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Departments] = {
    withSQL(select.from(Departments as d)).map(Departments(d.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Departments as d)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Departments] = {
    withSQL {
      select.from(Departments as d).where.append(where)
    }.map(Departments(d.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Departments] = {
    withSQL {
      select.from(Departments as d).where.append(where)
    }.map(Departments(d.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Departments as d).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    id: Int,
    department: String)(implicit session: DBSession = autoSession): Departments = {
    withSQL {
      insert.into(Departments).namedValues(
        column.id -> id,
        column.department -> department
      )
    }.update.apply()

    Departments(
      id = id,
      department = department)
  }

  def batchInsert(entities: collection.Seq[Departments])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(String, Any)]] = entities.map(entity =>
      Seq(
        "id" -> entity.id,
        "department" -> entity.department))
    SQL("""insert into departments(
      id,
      department
    ) values (
      {id},
      {department}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Departments)(implicit session: DBSession = autoSession): Departments = {
    withSQL {
      update(Departments).set(
        column.id -> entity.id,
        column.department -> entity.department
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Departments)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Departments).where.eq(column.id, entity.id) }.update.apply()
  }

}
