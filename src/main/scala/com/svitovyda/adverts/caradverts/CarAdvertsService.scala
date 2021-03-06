package com.svitovyda.adverts.caradverts

import java.util.UUID

import com.svitovyda.adverts.caradverts.CarAdvertsService._
import slick.driver.H2Driver.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class CarAdvertsService(db: Database)(implicit dbContext: ExecutionContext) {

  private val caradverts = TableQuery[CarAdvertTable]
  private def getById(id: CarAdvertId) = caradverts.filter(_.id === id.value)

  def init(): Unit = Await.ready(
    db.run(caradverts.schema.create),
    5.seconds
  )

  def getAllAdverts(): Future[List[CarAdvert]] = db.run(caradverts.result).map {
    _.map(CarAdvert(_)).toList
  }

  def getAdvert(id: CarAdvertId): Future[Option[CarAdvert]] = {
    db.run(getById(id).take(1).result).map(rows => rows.headOption.map(CarAdvert(_)))
  }

  def addAdvert(carAdvert: CarAdvert): Future[CarAdvert] =
    db.run(caradverts += carAdvert.toRow).map(_ => carAdvert)

  def deleteAdvert(id: CarAdvertId): Future[Int] = {
    db.run(getById(id).delete)
  }

  def modifyAdvert(carAdvert: CarAdvert): Future[Option[CarAdvert]] =
    db.run(getById(carAdvert.id).update(carAdvert.toRow)).map {
      case 1 => Some(carAdvert)
      case _ => None
    }
}

object CarAdvertsService {

  case class CarAdvertId(value: String) extends AnyVal
  object CarAdvertId {
    def newId: CarAdvertId = CarAdvertId(UUID.randomUUID.toString)
  }

  case class CarAdvert(
    id: CarAdvertId,
    title: String,
    fuel: CarAdvert.Fuel,
    price: Int,
    isNew: Boolean = false,
    mileage: Option[Int] = None,
    firstRegistration: Option[Int] = None // for now - only year
  ) {
    def toRow: CarAdvertRow = CarAdvertRow(
      id = id.value,
      title = title,
      fuel = fuel,
      price = price,
      isNew = isNew,
      mileage = mileage,
      firstRegistration = firstRegistration
    )
  }
  object CarAdvert {
    def apply(row: CarAdvertRow): CarAdvert = CarAdvert(
      id = CarAdvertId(row.id),
      title = row.title,
      fuel = row.fuel,
      price = row.price,
      isNew = row.isNew,
      mileage = row.mileage,
      firstRegistration = row.firstRegistration
    )

    sealed trait Fuel
    object Fuel {
      val Values = Set(Gasoline, Diesel)

      def apply(fuel: String): Fuel = Values.find(_.toString.toLowerCase == fuel.toLowerCase).getOrElse {
        sys.error(s"Cannot find the fuel type: $fuel")
      }
      case object Gasoline extends Fuel
      case object Diesel extends Fuel
    }
  }
}
