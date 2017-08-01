package util

import error.ValidationError
import models.Message
import error.ErrorMessages

/*
* Util singleton class for validation Message instance
*/
object MessageValidator {
  private final val UserNamePointer = "userName"
  private final val MessageTextPointer = "messageText"

  private final val UserNamePattern = """^[A-Z]'?[-a-zA-Z]+$""".r

  def validate(message: Message): Option[Seq[ValidationError]] = {
    var errors = List[ValidationError]()
    var result = None: Option[Seq[ValidationError]]

    if (message.userName.isEmpty) {
      errors :+= ValidationError(constraint = ValidationError.Required, pointer = UserNamePointer, detail = ErrorMessages.REQUIRED_FIELD_ERROR)
    }

    if (!message.userName.matches(UserNamePattern.regex)) {
      errors :+= ValidationError(constraint = ValidationError.Invalid, pointer = UserNamePointer, detail = ErrorMessages.INCORRECT_NAME_VALUE_ERROR)
    }

    if (message.messageText.isEmpty) {
      errors :+= ValidationError(constraint = ValidationError.Required, pointer = MessageTextPointer, detail = ErrorMessages.REQUIRED_FIELD_ERROR)
    }

    if (errors.nonEmpty) {
      result = Some(errors.seq)
    }

    result
  }
}
