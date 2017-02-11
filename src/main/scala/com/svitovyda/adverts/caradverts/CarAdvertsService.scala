package com.svitovyda.adverts.caradverts

import java.util.UUID

import com.svitovyda.adverts.caradverts.CarAdvertsService._

class CarAdvertsService() {
  def getAllAdverts(): List[CarAdvert] = List()

  def getAdvert(id: CarAdvertId): Either[String, CarAdvert] = Left("Not implemented yet")

  def addAdvert(carAdvert: CarAdvert): Option[String] = Some("Not implemented yet")

  def modifyAdvert(carAdvert: CarAdvert): Either[String, CarAdvert] = Left("Not implemented yet")
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
    mileadge: Option[Int] = None,
    firstRegistration: Option[Int] = None // for now - only year
  )
  object CarAdvert {
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
