package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._

object MessageForm {
  case class Data(userName: String, messageText: String)

  private final val UserNameErrorMsg = "Invalid name: name must consist from alphabetic characters"

  val form = Form(
    mapping(
      "userName" -> nonEmptyText.verifying(pattern("""^\p{Upper}?\p{Alpha}+$""".r, error = UserNameErrorMsg)),
      "messageText" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )
}
