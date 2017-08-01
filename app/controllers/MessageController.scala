package controllers

import javax.inject.{Inject, Singleton}

import error.ErrorMessages
import models.Message
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._
import services.MessageService
import util.MessageValidator

/*
* Message controller.
* */
@Singleton
class MessageController @Inject()(cc: ControllerComponents, messageService: MessageService) extends AbstractController(cc) with I18nSupport {

  private final val successfullySaved = "Message successfully saved."

  /*
  * Action for saving and handling data from the form
  * for sending new messages.
  */
  def createMessage = Action(BodyParsers.parse.json) { request =>
    val messageResult = request.body.validate[Message]

    messageResult.fold(
      errors => {
        BadRequest(ErrorMessages.INVALID_JSON_ERROR)
      },
      message => {
        MessageValidator.validate(message) match {
          case Some(errors) => UnprocessableEntity(Json.toJson(errors))
          case None =>
            if (messageService.saveMessage(message)) {
              Ok(successfullySaved).as(TEXT)
            } else {
              InternalServerError(ErrorMessages.CREATE_MESSAGE_ERROR)
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
    messageService.getMessagesAsJsonString match {
      case Some(messages) => Ok(messages)
      case None => NotFound(ErrorMessages.GET_MESSAGES_ERROR).as(TEXT)
    }
  }
}
