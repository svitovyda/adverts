package com.svitovyda.adverts.caradverts

import com.svitovyda.adverts.BaseAppSpec
import com.svitovyda.adverts.caradverts.CarAdvertsController._
import play.api.libs.json._

class CarAdvertsControllerSpec extends BaseAppSpec {
  "correctly parse input data" in {
    val input1 = Json.parse(
      """{"title":"test1","fuel":"diesel","price":10,"isNew":true}"""
    )
    input1.validate[CarAdvertRequest] shouldBe a[JsSuccess[_]]

    val input2 = Json.parse(
      """{"title":"test4","fuel":"gasoline","price":13,"isNew":false,"mileage":5,"firstRegistration":2004}"""
    )
    input2.validate[CarAdvertRequest] shouldBe a[JsSuccess[_]]

    val input3 = Json.parse(
      """{"title":"test3","fuel":"diesel","price":30,"isNew":true,"mileage":4,"firstRegistration":2001}"""
    )
    input3.validate[CarAdvertRequest] shouldBe a[JsError]

    val input4 = Json.parse(
      """{"title":"test2","fuel":"gasoline","price":20,"isNew":false}"""
    )
    input4.validate[CarAdvertRequest] shouldBe a[JsError]
  }

}
