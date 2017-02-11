package com.svitovyda.adverts.caradverts

import play.api.mvc.{Action, AnyContent, Controller}

class CarAdvertsController extends Controller {

  def get: Action[AnyContent] = Action {
    Ok("Hallo")
  }

}
