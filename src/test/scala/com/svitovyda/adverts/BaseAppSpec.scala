package com.svitovyda.adverts

import java.io.File

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.testkit.TestKit
import org.scalatest.WordSpecLike
import org.scalatest.concurrent.ScalaFutures
import play.api._
import play.api.inject.DefaultApplicationLifecycle
import play.api.libs.ws.ahc.AhcWSComponents
import org.scalatest.Matchers
import scala.concurrent.duration._


class BaseAppSpec extends TestKit(ActorSystem("MySpec"))
  with WordSpecLike
  with ScalaFutures
  with Matchers
  with AhcWSComponents {

  override def environment: Environment = new Environment(
    rootPath = new File("."),
    classLoader = ApplicationLoader.getClass.getClassLoader,
    mode = Mode.Test)

  lazy val app = new AppComponents(ApplicationLoader.createContext(environment)).play.application

  val port: Int = 9010

  val url: String = s"http://localhost:$port/"

  implicit val defaultPatience = PatienceConfig(10.seconds, 5000.millis)

  override def configuration: Configuration = {
    val config = app.configuration.underlying
    val testDb = config.getValue("h2memTest")
    new Configuration(config.withValue("h2memLive", testDb))
  }

  override def applicationLifecycle = new DefaultApplicationLifecycle()

  override def materializer: Materializer = app.materializer
}

object BaseAppSpec {

}
