package com.svitovyda.adverts.router

import java.io.File

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.testkit.TestKit
import com.svitovyda.adverts.AppComponents
import org.scalatest.WordSpecLike
import org.scalatest.concurrent.ScalaFutures
import play.api._
import play.api.inject.DefaultApplicationLifecycle
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.test._
import org.scalatest.Matchers
import scala.concurrent.duration._

class RouterComponentsSpec extends TestKit(ActorSystem("MySpec"))
  with WordSpecLike
  with ScalaFutures
  with Matchers
  with AhcWSComponents {

  override def environment: Environment = new Environment(
    rootPath = new File("."),
    classLoader = ApplicationLoader.getClass.getClassLoader,
    mode = Mode.Test)

  implicit lazy val app = new AppComponents(ApplicationLoader.createContext(environment)).play.application

  implicit lazy val port = 9010

  implicit val defaultPatience = PatienceConfig(10.seconds, 5000.millis)

  override def configuration: Configuration = app.configuration

  override def applicationLifecycle = new DefaultApplicationLifecycle()

  override def materializer: Materializer = app.materializer

  "GET /addresses" in new WithServer(app, port) {

    whenReady(wsApi.url(s"http://localhost:$port/hallo").get()) { result =>
      result.status shouldBe 200
    }
  }

}
