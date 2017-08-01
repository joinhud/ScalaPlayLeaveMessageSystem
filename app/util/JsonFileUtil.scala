package util

import java.io.{File, FileWriter, IOException, RandomAccessFile}
import java.util.concurrent.locks.{ReadWriteLock, ReentrantReadWriteLock}

import error.ErrorMessages
import play.api.Logger
import play.api.libs.json.{JsArray, JsValue}

import scala.io.Source

object JsonFileUtil {
  private final val JsonFile = new File("dist/messages.json")
  private final val JsonMessageSeparator = ", "
  private final val JsonArrayEnd = "]"
  private final val NewJsonFileLength = 2L
  private final val Lock: ReadWriteLock = new ReentrantReadWriteLock()

  private def fillNewFile: Unit = {
    var writer = None: Option[FileWriter]

    try {
      writer = Some(new FileWriter(JsonFile))
      val emptyArray = JsArray(Seq()).toString()
      writer.get.write(emptyArray)
    } catch {
      case e: IOException => Logger.error(ErrorMessages.WRITE_TO_FILE_ERROR, e)
    } finally {
      if(writer.isDefined) {
        writer.get.close()
      }
    }
  }

  private def isNewJsonFile(fileLength: Long): Boolean = {
    fileLength == NewJsonFileLength
  }

  def appendJsonObjectToFile(jsObject: JsValue): Boolean = {
    var result = false

    if (jsObject != null) {
      Lock.writeLock().lock()

      if (!JsonFile.exists()) {
        fillNewFile
      }

      var jsonWriter: RandomAccessFile = null

      try {
        jsonWriter = new RandomAccessFile(JsonFile, "rw")

        var jsonString = jsObject.toString() + JsonArrayEnd

        if (!isNewJsonFile(jsonWriter.length())) {
          jsonString = JsonMessageSeparator + jsonString
        }

        jsonWriter.seek(jsonWriter.length() - 1)
        jsonWriter.writeBytes(jsonString)
        result = true
      } catch {
        case e: IOException =>
          Logger.error(ErrorMessages.WRITE_TO_FILE_ERROR, e)
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

  def getFileContent: Option[String] = {
    Lock.readLock().lock()
    var result = None: Option[String]

    if (!JsonFile.exists()) {
      fillNewFile
    }

    val source = Source.fromFile(JsonFile)

    try {
      result = Some(source.mkString)
    } catch {
      case e: Exception => Logger.error(ErrorMessages.READ_FROM_FILE_ERROR, e)
    } finally {
      source.close()
      Lock.readLock().unlock()
    }

    result
  }
}
