package services

import java.io.{File, FileWriter, IOException, RandomAccessFile}
import java.time.format.DateTimeFormatter
import java.util.concurrent.locks.{ReadWriteLock, ReentrantReadWriteLock}
import javax.inject.Singleton

import models.Message
import play.api.Logger
import play.api.libs.json.{JsArray, Json, Writes}

import scala.io.Source

/*
* Service class for working with user's messages.
* Singleton class.
*/
@Singleton
object MessageService {
  /*
  * JSON file for storing messages.
  */
  private final val JsonFile = new File("dist/messages.json")
  /*
  * The string which separates JSON objects in the JSON file.
  */
  private final val JsonMessageSeparator = ", "
  /*
  * End character for closing array of objects in the JSON file.
  */
  private final val JsonArrayEnd = "]"
  /*
  * Constant for storing length of new JSON file with empty array.
  */
  private final val NewJsonFileLength = 2L
  /*
  * Lock object.
  * Using for read-write locking.
  * In saveMessage(message: Message): Boolean method used write lock.
  * In listJsonMessages: Option[String] method used read lock.
  */
  private final val Lock: ReadWriteLock = new ReentrantReadWriteLock()
  /*
  * Constant for storing error message which can appeared,
  * when the user cannot write data into the file.
  */
  private final val WriteErrorMessage = "Can not write message to file."
  /*
  * Constant for storing error message which can appeared,
  * when the user cannot read data from the file.
  */
  private final val ReadErrorMessage = "Can not read messages from file"

  /*
  * Private method for creating the file and filling
  * into it an empty JSON array.
  */
  private def fillNewFile: Unit = {
    var writer = None: Option[FileWriter]

    try {
      writer = Some(new FileWriter(JsonFile))
      val emptyArray = JsArray(Seq()).toString()
      writer.get.write(emptyArray)
    } catch {
      case e: IOException => Logger.error(WriteErrorMessage, e)
    } finally {
      if(writer.isDefined) {
        writer.get.close()
      }
    }
  }

  /*
  * Private method for checking is the file
  * haven't got any JSON objects except empty array.
  */
  private def isNewJsonFile(fileLength: Long): Boolean = {
    fileLength == NewJsonFileLength
  }

  /*
  * Method for saving Message instance to the file.
  * If message instance is null return false.
  * If any IOException is accrued return false.
  * If method successfully write message instance to the file
  * than return true.
  */
  def saveMessage(message: Message): Boolean = {
    var result = false

    if (message != null) {
      Lock.writeLock().lock()

      if (!JsonFile.exists()) {
        fillNewFile
      }

      var jsonWriter: RandomAccessFile = null

      try {
        jsonWriter = new RandomAccessFile(JsonFile, "rw")

        var jsonMessage = Json.toJson(message).toString() + JsonArrayEnd

        if (!isNewJsonFile(jsonWriter.length())) {
          jsonMessage = JsonMessageSeparator + jsonMessage
        }

        jsonWriter.seek(jsonWriter.length() - 1)
        jsonWriter.writeBytes(jsonMessage)
        result = true
      } catch {
        case e: IOException =>
          Logger.error(WriteErrorMessage, e)
          result = false
      } finally {
        if(jsonWriter != null) {
          jsonWriter.close()
        }
        Lock.writeLock().unlock()
      }
    }

    result
  }

  /*
  * Method for receiving string which consist of
  * JSON array with JSON objects.
  * If exceptions is accrued return None else
  * return Option[String] instance.
  */
  def listJsonMessages: Option[String] = {
    Lock.readLock().lock()
    var result = None: Option[String]

    if (!JsonFile.exists()) {
      fillNewFile
    }

    val source = Source.fromFile(JsonFile)

    try {
      result = Some(source.mkString)
    } catch {
      case e: Exception => Logger.error(ReadErrorMessage, e)
    } finally {
      source.close()
      Lock.readLock().unlock()
    }

    result
  }
}
