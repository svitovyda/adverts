package com.svitovyda.adverts.caradverts

import com.svitovyda.adverts.BaseAppSpec
import com.svitovyda.adverts.caradverts.CarAdvertsService.CarAdvert.Fuel
import com.svitovyda.adverts.caradverts.CarAdvertsService.{CarAdvert, CarAdvertId}
import org.scalatest.BeforeAndAfterAll
import slick.driver.H2Driver.api._

import scala.concurrent.ExecutionContext


class CarAdvertsServiceSpec extends BaseAppSpec with BeforeAndAfterAll {

  val db = Database.forConfig("h2memTest")

  implicit val dbContext: ExecutionContext = app.actorSystem.dispatchers.lookup("db-context")

  val service = new CarAdvertsService(db)

  private val test1 = CarAdvertRow(CarAdvertId.newId.value, "test1", Fuel.Diesel, 10, true, None, None)
  private val test2 = CarAdvertRow(CarAdvertId.newId.value, "test2", Fuel.Gasoline, 20, true, None, None)
  private val test3 = CarAdvertRow(CarAdvertId.newId.value, "test3", Fuel.Diesel, 30, false, Some(4), Some(2001))
  private val test4 = CarAdvertRow(CarAdvertId.newId.value, "test4", Fuel.Gasoline, 13, false, Some(5), Some(2004))

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
    whenReady(service.getAdvert(CarAdvertId(test1.id))) { case Some(result) =>
      result.title shouldBe test1.title
    }

    whenReady(service.getAdvert(CarAdvertId("a"))) { result =>
      result shouldBe None
    }
  }

  "add new car advert" in {
    whenReady(service.addAdvert(CarAdvert(test4))) { _ =>
      whenReady(service.getAllAdverts()) { result =>
        result.length shouldBe 4
      }
    }
  }

  "delete car advert" in {
    whenReady(service.deleteAdvert(CarAdvertId(test4.id))) { amount =>
      amount shouldBe 1
      whenReady(service.getAllAdverts()) { result =>
        result.length shouldBe 3
      }
    }

    whenReady(service.deleteAdvert(CarAdvertId("a"))) { amount =>
      amount shouldBe 0
      whenReady(service.getAllAdverts()) { result =>
        result.length shouldBe 3
      }
    }
  }

  "modify existing car advert" in {
    val modified = CarAdvert(test4.copy(id = test3.id))

    whenReady(service.modifyAdvert(modified)) { result =>
      result shouldBe Some(modified)
      whenReady(service.getAllAdverts()) { result =>
        result.length shouldBe 3
      }
      whenReady(service.getAdvert(CarAdvertId(test3.id))) { result =>
        result shouldBe Some(modified)
      }
    }

    whenReady(service.modifyAdvert(modified.copy(id = CarAdvertId("abc")))) { amount =>
      amount shouldBe None
      whenReady(service.getAllAdverts()) { result =>
        result.length shouldBe 3
      }
      whenReady(service.getAdvert(CarAdvertId("abc"))) { result =>
        result shouldBe None
      }
    }
  }

  override protected def afterAll(): Unit = {
    db.close()
  }
}
