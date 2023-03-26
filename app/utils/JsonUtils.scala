package utils

//import play.api.libs.json._
import play.api.libs.json.{Json, JsonValidationError, JsArray, JsPath }
import scala.collection.Seq

/**
 * Created by MRM
 */
trait JsonUtils {

    def errorJson(message: String) = Json.obj("error" -> message)

    def okJson(message: String) = Json.obj("message" -> message)    

      def errorsJson(errors: Seq[(JsPath, Seq[JsonValidationError])]) = errors.foldLeft(JsArray()) { (acc, c) =>
    acc :+ Json.obj("error" -> s"${c._1.toString.drop(1)} ${c._2.foldLeft("")((acc, c) => acc + c.message)}")
  }

  def postErrorJson(messege: String) ={
   errorJson(s"At least one of the ids where already in the data base." +
            s" The rest have been insert. The first error is : ${messege.split("\\.").lastOption.getOrElse("") } ")
    }
}