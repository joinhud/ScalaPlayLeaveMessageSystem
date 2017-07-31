package controllers

import javax.inject.{Inject, Singleton}

import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class HomeController @Inject()(cc : ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def index = Action {
    Ok(views.html.index())
  }
}
