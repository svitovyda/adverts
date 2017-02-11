package com.svitovyda.adverts.router

import com.svitovyda.adverts.AppComponents
import com.svitovyda.adverts.caradverts.CarAdvertsComponents
import play.api.routing._
import play.api.routing.sird._

import scala.concurrent.ExecutionContext

class RouterComponents(carAdverts: CarAdvertsComponents) {

  implicit val context: ExecutionContext = AppComponents.actorSystem.dispatcher

  lazy val router: Router = Router.from {

    case GET(p"/hallo") => carAdverts.controller.get
  }

}
