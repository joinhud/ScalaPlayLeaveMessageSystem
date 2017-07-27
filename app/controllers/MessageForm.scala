package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._

/*
* Form object for creating messages
*/
object MessageForm {
  /*
  * Form data class.
  * Data for this class receives from form.
  */
  case class Data(userName: String, messageText: String)

  /*
  * Regex patter for checking user name which
  * user enter into a form field.
  */
  private final val UserNamePattern = """^\p{Upper}?\p{Alpha}+$""".r

  /*
  * Constant for storing 'Invalid name' error message.
  * If user enter name which is not matching the UserNamePattern
  * or user enter empty string then the user will see error which
  * contain this message.
  */
  private final val UserNameErrorMsg = "Invalid name: name must consist from alphabetic characters"

  /*
  * Form form input user data: user name and password.
  */
  val form = Form(
    mapping(
      "userName" -> nonEmptyText.verifying(pattern(UserNamePattern, error = UserNameErrorMsg)),
      "messageText" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )
}
