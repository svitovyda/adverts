package com.svitovyda.adverts

import akka.actor.ActorSystem
import com.svitovyda.adverts.caradverts.CarAdvertsComponents
import com.svitovyda.adverts.router.RouterComponents
import play.api.ApplicationLoader.Context
import play.api.routing.Router
import play.api.{ApplicationLoader, BuiltInComponentsFromContext}

class AppComponents(context: Context) {
  import AppComponents._

  lazy val carAdverts = new CarAdvertsComponents

  lazy val router = new RouterComponents(carAdverts)

  lazy val play = new Play(context, () => router.router)
}
object AppComponents {

  val actorSystem = ActorSystem()

  class Play(
    context: ApplicationLoader.Context,
    createRouter: () => Router)
    extends BuiltInComponentsFromContext(context) {

    lazy val router: Router = createRouter()
    override lazy val actorSystem: ActorSystem = AppComponents.actorSystem
  }
}
