package services

import java.io.{File, FileWriter, IOException, RandomAccessFile}
import java.time.format.DateTimeFormatter
import java.util.concurrent.locks.{ReadWriteLock, ReentrantReadWriteLock}
import javax.inject.Singleton

import models.Message
import play.api.Logger
import play.api.libs.json.{JsArray, Json, Writes}

import scala.io.Source

@Singleton
object MessageService {
  private final val JsonFile = new File("dist/messages.json")
  private final val JsonMessageSeparator = ", "
  private final val JsonArrayEnd = "]"
  private final val NewJsonFileLength = 2L
  private final val Lock: ReadWriteLock = new ReentrantReadWriteLock()
  private final val DatePattern = "yyyy-MM-dd HH:mm:ss"

  private final val WriteErrorMessage = "Can not write message to file."
  private final val ReadErrorMessage = "Can not read messages from file"

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

  private def isNewJsonFile(fileLength: Long): Boolean = {
    fileLength == NewJsonFileLength
  }

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

        implicit val messageWrites = new Writes[Message] {
          def writes(message: Message) = Json.obj(
            "userName" -> message.userName,
            "messageText" -> message.messageText,
            "date" -> message.date.format(DateTimeFormatter.ofPattern(DatePattern))
          )
        }

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
