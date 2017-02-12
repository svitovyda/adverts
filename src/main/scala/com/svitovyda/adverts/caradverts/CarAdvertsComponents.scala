package com.svitovyda.adverts.caradverts

import com.svitovyda.adverts.AppComponents
import slick.driver.H2Driver.api.Database

import scala.concurrent.ExecutionContext

class CarAdvertsComponents {
  def db = Database.forConfig("h2memLive")

  implicit val dbContext: ExecutionContext = AppComponents.actorSystem.dispatchers.lookup("db-context")

  lazy val controller = {
    val service = new CarAdvertsService(db)
    service.init()
    new CarAdvertsController(service)
  }
}
