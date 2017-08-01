package error

import play.api.libs.json.Json

case class ValidationError(constraint: String, pointer: String, title: String = "Validation Error", detail: String)

object ValidationError {
  final val Required = "required"
  final val Invalid = "invalid"

  implicit val validationErrorFormat = Json.format[ValidationError]
}
