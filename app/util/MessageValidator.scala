package util

import error.ValidationError
import models.Message

object MessageValidator {
  private final val RequiredFieldError = "Required field"
  private final val IncorrectNameValueError = "Invalid name. Name must contains only alphabetical symbols and starts with capital letter."

  private final val UserNamePointer = "userName"
  private final val MessageTextPointer = "messageText"

  private final val UserNamePattern = """^[A-Z]'?[-a-zA-Z]+$""".r

  def validate(message: Message): Option[Seq[ValidationError]] = {
    var errors = List[ValidationError]()
    var result = None: Option[Seq[ValidationError]]

    if (message.userName.isEmpty) {
      errors :+= ValidationError(constraint = ValidationError.Required, pointer = UserNamePointer, detail = RequiredFieldError)
    }

    if (!message.userName.matches(UserNamePattern.regex)) {
      errors :+= ValidationError(constraint = ValidationError.Invalid, pointer = UserNamePointer, detail = IncorrectNameValueError)
    }

    if (message.messageText.isEmpty) {
      errors :+= ValidationError(constraint = ValidationError.Required, pointer = MessageTextPointer, detail = RequiredFieldError)
    }

    if (!errors.isEmpty) {
      result = Some(errors.seq)
    }

    result
  }
}
