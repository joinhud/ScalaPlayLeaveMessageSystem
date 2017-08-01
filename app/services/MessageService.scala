package services

import javax.inject.Singleton

import models.Message
import play.api.libs.json.Json
import util.JsonFileUtil

/*
* Service class for working with user's messages.
* Singleton class.
*/
@Singleton
class MessageService {

  def saveMessage(message: Message): Boolean = {
    var result = false

    if (message != null) {
      val jsonMessage = Json.toJson(message)

      result = JsonFileUtil.appendJsonObjectToFile(jsonMessage)
    }

    result
  }

  /*
  * Method for receiving string which consist of
  * JSON array with JSON objects.
  * If exceptions is accrued return None else
  * return Option[String] instance.
  */
  def getMessagesAsJsonString: Option[String] = {
    JsonFileUtil.getFileContent
  }
}
