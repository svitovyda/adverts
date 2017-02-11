package com.svitovyda.adverts.router

import com.svitovyda.adverts.BaseAppSpec
import play.api.test._

class RouterComponentsSpec extends BaseAppSpec {

  "GET /hallo" in new WithServer(app, port) {

    whenReady(wsApi.url(s"http://localhost:$port/hallo").get()) { result =>
      result.status shouldBe 200
    }
  }

}
