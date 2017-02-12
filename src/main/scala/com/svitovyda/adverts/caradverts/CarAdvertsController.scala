package com.svitovyda.adverts.caradverts

import java.time.LocalDateTime

import akka.util.Timeout
import com.svitovyda.adverts.caradverts.CarAdvertsController._
import com.svitovyda.adverts.caradverts.CarAdvertsService.CarAdvert.Fuel
import com.svitovyda.adverts.caradverts.CarAdvertsService.{CarAdvert, CarAdvertId}
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class CarAdvertsController(
  dbService: CarAdvertsService
)(implicit dbContext: ExecutionContext) extends Controller {

  implicit val timeout: Timeout = 15.seconds

  def get = Action.async {
    dbService.getAllAdverts().map { result =>
      Ok(Json.toJson(result))
    }
  }

  def get(id: String) = Action.async {
    dbService.getAdvert(CarAdvertId(id)).map {
      case Some(result) => Ok(Json.toJson(result))
      case _            => NotFound(s"Element $id not found")
    }
  }

  def create() = Action.async(parse.json[CarAdvertRequest]) { implicit request =>
    dbService.addAdvert(request.body.toModel).map(result => Ok(Json.toJson(result)))
  }

  def update(id: String) = Action.async(parse.json[CarAdvertRequest]) { implicit request =>
    dbService.modifyAdvert(request.body.toModel(CarAdvertId(id))).map {
      case Some(result) => Ok(Json.toJson(result))
      case _            => ExpectationFailed("Something went wrong")
    }
  }

  def delete(id: String) = Action.async(parse.json[CarAdvertRequest]) { implicit request =>
    dbService.deleteAdvert(CarAdvertId(id)).map {
      case 1 => Ok
      case _ => ExpectationFailed("Something went wrong")
    }
  }
}

object CarAdvertsController {
  case class CarAdvertRequest(
    title: String,
    fuel: CarAdvert.Fuel,
    price: Int,
    isNew: Boolean,
    mileage: Option[Int],
    firstRegistration: Option[Int]
  ) {
    def toModel: CarAdvert = CarAdvert(
      id = CarAdvertId.newId,
      title = title,
      fuel = fuel,
      price = price,
      isNew = isNew,
      mileage = mileage,
      firstRegistration = firstRegistration
    )
    def toModel(id: CarAdvertId): CarAdvert = CarAdvert(
      id = id,
      title = title,
      fuel = fuel,
      price = price,
      isNew = isNew,
      mileage = mileage,
      firstRegistration = firstRegistration
    )
  }

  implicit val formatFuel: Format[Fuel] = Format(
    Reads{ js => js.validate[String].map(str => Fuel(str.toLowerCase))},
    Writes{ f => JsString(f.toString.toLowerCase)}
  )

  implicit val readsRequest: Reads[CarAdvertRequest] = Reads { json =>
    for {
      title <- (json \ "title").validate[String]
      fuel <- (json \ "fuel").validate[Fuel]
      price <- (json \ "price").validate[Int](Reads.min(1))
      isNew <- (json \ "isNew").validate[Boolean]
      mileage <- (json \ "mileage").validateOpt[Int]
      if mileage.isEmpty == isNew && mileage.forall(_ > 0)
      firstRegistration <- (json \ "firstRegistration").validateOpt[Int]
      if firstRegistration.isEmpty == isNew && firstRegistration.forall { year =>
        year > 1900 && year < LocalDateTime.now.getYear
      }
    } yield CarAdvertRequest(title, fuel, price, isNew, mileage, firstRegistration)
  }

  implicit val writesId: Writes[CarAdvertId] = Writes { id => Json.toJson[String](id.value) }

  implicit val writesAdvert: Writes[CarAdvert] = Json.writes[CarAdvert]
}
