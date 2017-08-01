package controllers

import javax.inject.{Inject, Singleton}

import models.Message
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._
import services.MessageService
import util.MessageValidator

/*
* Main application controller.
* */
@Singleton
class MessageController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  /*
  * Immutable constant for storing error message which
  * may be appeared during saving new message.
  */
  private final val createMessageError = "The message can not be saved because a server error has occurred. Try repeat again later."
  private final val getMessagesError = "Can not receive data from server because a server error has occurred. Try repeat again later."
  private final val invalidJsonError = "Invalid syntax of JSON object."
  private final val successfullySaved = "Message successfully saved."

  /*
  * Action for saving and handling data from the form
  * for sending new messages.
  */
  def createMessage = Action(BodyParsers.parse.json) { request =>
    val messageResult = request.body.validate[Message]

    messageResult.fold(
      errors => {
        BadRequest(invalidJsonError)
      },
      message => {
        MessageValidator.validate(message) match {
          case Some(errors) => UnprocessableEntity(Json.toJson(errors))
          case None =>
            if (MessageService.saveMessage(message)) {
              Ok(successfullySaved).as(TEXT)
            } else {
              InternalServerError(createMessageError)
            }
        }
      }
    )
  }

  /*
  * Action for receiving JSON string
  * with messages.
  */
  def messages = Action {
    MessageService.listJsonMessages match {
      case Some(messages) => Ok(messages)
      case None => NotFound(getMessagesError).as(TEXT)
    }
  }
}
