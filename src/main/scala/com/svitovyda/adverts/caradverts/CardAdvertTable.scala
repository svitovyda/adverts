package com.svitovyda.adverts.caradverts

import com.svitovyda.adverts.caradverts.CarAdvertRow._
import com.svitovyda.adverts.caradverts.CarAdvertsService.CarAdvert._
import com.svitovyda.adverts.caradverts.CarAdvertsService.CarAdvertId
import slick.driver.H2Driver.api._
import slick.lifted.ProvenShape

case class CarAdvertRow(
  id: String,
  title: String,
  fuel: Fuel,
  price: Int,
  isNew: Boolean,
  mileage: Option[Int],
  firstRegistration: Option[Int]
)
object CarAdvertRow {

  implicit val columnStringFuel = MappedColumnType.base[Fuel, String](
    fuel => fuel.toString.toLowerCase,
    string => Fuel(string)
  )

  implicit val columnStringId = MappedColumnType.base[CarAdvertId, String](
    id => id.value,
    string => CarAdvertId(string)
  )

  def tupled = (CarAdvertRow.apply _).tupled
}

class CarAdvertTable(tag: Tag)
  extends Table[CarAdvertRow](tag, "CarAdverts") {

  def id: Rep[String] = column[String]("id", O.PrimaryKey)
  def title: Rep[String] = column[String]("title")
  def fuel: Rep[Fuel] = column[Fuel]("fuel")
  def price: Rep[Int] = column[Int]("price")
  def isNew: Rep[Boolean] = column[Boolean]("is_new")
  def mileage: Rep[Option[Int]] = column[Option[Int]]("mileage")
  def firstRegistration: Rep[Option[Int]] = column[Option[Int]]("first_registration")

  def * : ProvenShape[CarAdvertRow] = (id, title, fuel, price, isNew, mileage, firstRegistration) <>
    (CarAdvertRow.tupled, CarAdvertRow.unapply)
}
