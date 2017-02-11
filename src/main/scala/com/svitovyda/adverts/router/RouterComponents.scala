package com.svitovyda.adverts.router

import com.svitovyda.adverts.AppComponents
import play.api.routing._
import play.api.routing.sird._
import play.api.mvc.{Action, Controller, _}
import scala.concurrent.ExecutionContext

class RouterComponents() extends Controller {

  implicit val context: ExecutionContext = AppComponents.actorSystem.dispatcher

  lazy val router: Router = Router.from {

    case GET(p"/hallo") => Action(Ok("Hallo"))
  }

}
