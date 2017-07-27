package controllers

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}

import models.Message
import play.api.data.Form
import play.api.i18n.I18nSupport
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
  * Immutable variable for storing action which can be submitted
  * by the form for sending new messages.
  */
  val postUrl: Call = routes.MessageController.createMessage()

  /*
  * Action for displaying index view.
  */
  def index = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(MessageForm.form, postUrl))
  }

  /*
  * Action for saving and handling data from the form
  * for sending new messages.
  */
  def createMessage = Action { implicit request: Request[AnyContent] =>
    val errorFunction = { formWithErrors: Form[Data] =>
      BadRequest(views.html.index(formWithErrors, postUrl))
    }

    val successFunction = { data: Data =>
      val message = Message(userName = data.userName, messageText = data.messageText, LocalDateTime.now())
      if (MessageService.saveMessage(message)) {
        Redirect(routes.MessageController.index())
      } else {
        InternalServerError(views.html.index(MessageForm.form, postUrl, createMessageError))
      }
    }

    val formValidationResult = form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
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
