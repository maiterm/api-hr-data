package models

import scalikejdbc._

case class Jobs(
  id: Int,
  job: String) {

  def save()(implicit session: DBSession = Jobs.autoSession): Jobs = Jobs.save(this)(session)

  def destroy()(implicit session: DBSession = Jobs.autoSession): Int = Jobs.destroy(this)(session)

}


object Jobs extends SQLSyntaxSupport[Jobs] {

  override val tableName = "jobs"

  override val columns = Seq("id", "job")

  def apply(j: SyntaxProvider[Jobs])(rs: WrappedResultSet): Jobs = apply(j.resultName)(rs)
  def apply(j: ResultName[Jobs])(rs: WrappedResultSet): Jobs = new Jobs(
    id = rs.get(j.id),
    job = rs.get(j.job)
  )

  val j = Jobs.syntax("j")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Jobs] = {
    withSQL {
      select.from(Jobs as j).where.eq(j.id, id)
    }.map(Jobs(j.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Jobs] = {
    withSQL(select.from(Jobs as j)).map(Jobs(j.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Jobs as j)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Jobs] = {
    withSQL {
      select.from(Jobs as j).where.append(where)
    }.map(Jobs(j.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Jobs] = {
    withSQL {
      select.from(Jobs as j).where.append(where)
    }.map(Jobs(j.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Jobs as j).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    id: Int,
    job: String)(implicit session: DBSession = autoSession): Jobs = {
    withSQL {
      insert.into(Jobs).namedValues(
        column.id -> id,
        column.job -> job
      )
    }.update.apply()

    Jobs(
      id = id,
      job = job)
  }

  def batchInsert(entities: collection.Seq[Jobs])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(String, Any)]] = entities.map(entity =>
      Seq(
        "id" -> entity.id,
        "job" -> entity.job))
    SQL("""insert into jobs(
      id,
      job
    ) values (
      {id},
      {job}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Jobs)(implicit session: DBSession = autoSession): Jobs = {
    withSQL {
      update(Jobs).set(
        column.id -> entity.id,
        column.job -> entity.job
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Jobs)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Jobs).where.eq(column.id, entity.id) }.update.apply()
  }

}
