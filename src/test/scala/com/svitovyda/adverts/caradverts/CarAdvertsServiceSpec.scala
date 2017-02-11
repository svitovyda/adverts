package com.svitovyda.adverts.caradverts

import com.svitovyda.adverts.BaseAppSpec
import com.svitovyda.adverts.caradverts.CarAdvertsService.CarAdvert.Fuel
import com.svitovyda.adverts.caradverts.CarAdvertsService.{CarAdvert, CarAdvertId}

class CarAdvertsServiceSpec extends BaseAppSpec {
  "return all car adverts" in {
    val service = new CarAdvertsService()

    service.getAllAdverts() shouldBe List()
  }

  "add new car advert" in {
    val service = new CarAdvertsService()

    service.addAdvert(CarAdvert(CarAdvertId.newId, "", Fuel.Diesel, 1)) shouldBe a[Some[_]]
  }

  "modify existing car advert" in {
    val service = new CarAdvertsService()

    service.getAdvert(CarAdvertId.newId) shouldBe a[Left[_, _]]
    service.modifyAdvert(CarAdvert(CarAdvertId.newId, "", Fuel.Diesel, 1)) shouldBe a[Left[_, _]]
  }
}
