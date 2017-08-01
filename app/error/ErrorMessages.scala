package error

trait ErrorMessages {
  final val REQUIRED_FIELD_ERROR = "Required field"
  final val INCORRECT_NAME_VALUE_ERROR = "Invalid name. Name must contains only alphabetical symbols and starts with capital letter."
  final val CREATE_MESSAGE_ERROR = "The message can not be saved because a server error has occurred. Try repeat again later."
  final val GET_MESSAGES_ERROR = "Can not receive data from server because a server error has occurred. Try repeat again later."
  final val INVALID_JSON_ERROR = "Invalid syntax of JSON object."
  final val WRITE_TO_FILE_ERROR = "Can not write message to file."
  final val READ_FROM_FILE_ERROR = "Can not read messages from file."
}

object ErrorMessages extends ErrorMessages
