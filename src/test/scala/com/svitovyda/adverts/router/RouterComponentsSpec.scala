package com.svitovyda.adverts.router

import com.svitovyda.adverts.BaseAppSpec
import play.api.libs.json._
import play.api.test._

class RouterComponentsSpec extends BaseAppSpec {

  "GET / should return 404" in new WithServer(app, port) {

    whenReady(wsApi.url(url).get()) { result =>
      result.status shouldBe 404
    }
  }

  "GET /caradverts should return 200" in new WithServer(app, port) {

    whenReady(wsApi.url(s"${url}caradverts").get()) { result =>
      result.status shouldBe 200
    }
  }

  "GET /caradverts/unknown should return 404" in new WithServer(app, port) {

    whenReady(wsApi.url(s"${url}caradverts/abcdefg").get()) { result =>
      result.status shouldBe 404
    }
  }

  "POST, PUT, GET and DELETE /caradverts should return 200" in new WithServer(app, port) {

    val result = whenReady(wsApi.url(s"${url}caradverts").post(
      Json.parse(
        """{"title":"test1","fuel":"gasoline","price":13,"isNew":false,"mileage":5,"firstRegistration":2004}"""
      ))) { result =>
      result.status shouldBe 200

      val advert = Json.parse(result.body)

      (advert \ "title").as[String] shouldBe "test1"

      (advert \ "id").as[String]
    }

    whenReady(wsApi.url(s"${url}caradverts").get()) { result =>
      result.status shouldBe 200

      val adverts = Json.parse(result.body).as[List[JsValue]]
      adverts.length shouldBe 1
    }

    whenReady(wsApi.url(s"${url}caradverts/$result").put(
      Json.parse(
        """{"title":"test1 updated","fuel":"gasoline","price":11,"isNew":false,"mileage":5,"firstRegistration":2004}"""
      ))) { result =>
      result.status shouldBe 200
    }

    whenReady(wsApi.url(s"${url}caradverts/$result").get()) { result =>
      result.status shouldBe 200

      val advert = Json.parse(result.body)
      (advert \ "title").as[String] shouldBe "test1 updated"
      (advert \ "price").as[Int] shouldBe 11
    }

    whenReady(wsApi.url(s"${url}caradverts/$result").delete()) { result =>
      result.status shouldBe 200

      whenReady(wsApi.url(s"${url}caradverts").get()) { result =>
        result.status shouldBe 200

        val adverts = Json.parse(result.body).as[List[JsValue]]
        adverts.length shouldBe 0
      }
    }

  }
}
