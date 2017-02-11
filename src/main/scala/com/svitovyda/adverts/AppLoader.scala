package com.svitovyda.adverts

import java.util.TimeZone

import play.api.ApplicationLoader.Context
import play.api._

class AppLoader extends ApplicationLoader {

  System.setProperty("user.timezone", "UTC")
  TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

  def load(context: Context): Application = {
    new AppComponents(context).play.application
  }
}
