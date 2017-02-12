package com.svitovyda.adverts.router

import com.svitovyda.adverts.BaseAppSpec
import play.api.test._

class RouterComponentsSpec extends BaseAppSpec {

  "GET / should return 404" in new WithServer(app, port) {

    whenReady(wsApi.url(s"http://localhost:$port/").get()) { result =>
      result.status shouldBe 404
    }
  }



}
