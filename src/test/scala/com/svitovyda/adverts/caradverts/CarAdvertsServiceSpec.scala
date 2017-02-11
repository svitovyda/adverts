package com.svitovyda.adverts.caradverts

import com.svitovyda.adverts.BaseAppSpec
import com.svitovyda.adverts.caradverts.CarAdvertsService.CarAdvert.Fuel
import com.svitovyda.adverts.caradverts.CarAdvertsService.{CarAdvert, CarAdvertId}
import org.scalatest.BeforeAndAfterAll
import slick.driver.H2Driver.api._

import scala.concurrent.ExecutionContext


class CarAdvertsServiceSpec extends BaseAppSpec with BeforeAndAfterAll {

  val db = Database.forConfig("h2mem1")

  implicit val dbContext: ExecutionContext = app.actorSystem.dispatchers.lookup("db-context")

  val service = new CarAdvertsService(db)

  private val test1 = CarAdvertRow(CarAdvertId.newId.value, "test1", Fuel.Diesel, 10, false, None, None)
  private val test2 = CarAdvertRow(CarAdvertId.newId.value, "test2", Fuel.Gasoline, 20, false, None, None)
  private val test3 = CarAdvertRow(CarAdvertId.newId.value, "test3", Fuel.Diesel, 30, true, Some(4), Some(2001))
  private val test4 = CarAdvertRow(CarAdvertId.newId.value, "test4", Fuel.Gasoline, 13, true, Some(5), Some(2004))

  private val caradverts = TableQuery[CarAdvertTable]

  override protected def beforeAll(): Unit = {
    service.init()
  }

  "return all car adverts" in {
    whenReady(service.getAllAdverts()) { result =>
      result.length shouldBe 0
    }

    val initDB = caradverts ++= Seq(test1, test2, test3)

    whenReady(db.run(initDB).flatMap(_ => service.getAllAdverts())) { result =>
      result.length shouldBe 3
    }
  }

  "get one car advert" in {
    whenReady(service.getAdvert(CarAdvertId(test1.id))) { result =>
      result.title shouldBe test1.title
    }
  }

  "add new car advert" in {
    val service = new CarAdvertsService(db)

    whenReady(service.addAdvert(CarAdvert(test4))) { _ =>
      whenReady(service.getAllAdverts()) { result =>
        result.length shouldBe 4
      }
    }
  }

  "modify existing car advert" ignore {
    val service = new CarAdvertsService(db)

    service.getAdvert(CarAdvertId.newId) shouldBe a[Left[_, _]]
    service.modifyAdvert(CarAdvert(CarAdvertId.newId, "", Fuel.Diesel, 1)) shouldBe a[Left[_, _]]
  }

  override protected def afterAll(): Unit = {
    db.close()
  }
}
