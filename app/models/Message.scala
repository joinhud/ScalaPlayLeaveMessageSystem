package models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import play.api.libs.json._
import play.api.libs.functional.syntax._

/*
* Model for message
*/
case class Message(userName: String, messageText: String, date: LocalDateTime)

object Message {
  private final val DatePattern = "yyyy-MM-dd HH:mm:ss"

  implicit val messageWrites = new Writes[Message] {
    def writes(message: Message) = Json.obj(
      "userName" -> message.userName,
      "messageText" -> message.messageText,
      "date" -> message.date.format(DateTimeFormatter.ofPattern(DatePattern))
    )
  }

  implicit val messageReads: Reads[Message] = (
    (JsPath \ "userName").read[String] and
      (JsPath \ "messageText").read[String] and
      (JsPath \ "date").readWithDefault[LocalDateTime](LocalDateTime.now())
    )(Message.apply _)
}
