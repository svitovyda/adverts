package com.svitovyda.adverts.router

import com.svitovyda.adverts.AppComponents
import com.svitovyda.adverts.caradverts.CarAdvertsComponents
import play.api.routing._
import play.api.routing.sird._

import scala.concurrent.ExecutionContext

class RouterComponents(carAdverts: CarAdvertsComponents) {

  implicit val context: ExecutionContext = AppComponents.actorSystem.dispatcher

  lazy val router: Router = Router.from {
    case GET(p"/caradverts")                            => carAdverts.controller.get
    case GET(p"/caradverts/$uuid<[-_a-zA-Z0-9]{2,50}>") => carAdverts.controller.get(uuid)

    case POST(p"/caradverts")                              => carAdverts.controller.create()
    case PUT(p"/caradverts/$uuid<[-_a-zA-Z0-9]{2,50}>")    => carAdverts.controller.update(uuid)
    case DELETE(p"/caradverts/$uuid<[-_a-zA-Z0-9]{2,50}>") => carAdverts.controller.delete(uuid)
  }

}
