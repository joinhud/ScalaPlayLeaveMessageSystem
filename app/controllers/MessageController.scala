package controllers

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}

import models.Message
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.libs.json.JsValue
import play.api.mvc._
import services.MessageService

/*
* Main application controller.
* */
@Singleton
class MessageController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {
  import MessageForm._

  /*
  * Immutable constant for storing error message which
  * may be appeared during saving new message.
  */
  private final val createMessageError = "The message can not be saved because a server error has occurred. Try repeat again later."

  /*
  * Action for saving and handling data from the form
  * for sending new messages.
  */
  def createMessage = Action { request: Request[AnyContent] =>
    val body: AnyContent = request.body
    val jsonBody: Option[JsValue] = body.asJson

    jsonBody.map { json =>
      Ok("Got: " + (json \ "userName").as[String]).as(TEXT)
    }.getOrElse {
      BadRequest("Expecting application/json request body")
    }
  }

  /*
  * Action for receiving JSON string
  * with messages.
  */
  def messages = Action {
    MessageService.listJsonMessages match {
      case Some(messages) => Ok(messages)
      case None => NotFound
    }
  }
}
