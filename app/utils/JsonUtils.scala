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

      def errorJson(errors: Seq[(JsPath, Seq[JsonValidationError])]) = errors.foldLeft(JsArray()) { (acc, c) =>
    acc :+ Json.obj("error" -> s"${c._1.toString.drop(1)} ${c._2.foldLeft("")((acc, c) => acc + c.message)}")
  }

  def postErrorJson(message: String) ={
    if (message.contains("PRIMARY KEY")) {
      errorJson(s"At least one of the ids was already in the data base." +
            s" The rest have been inserted. The first error is : ${message.split("\\.").lastOption.getOrElse("")} ")
    } else {//message.contains("FOREIGN KEY")
      val foreingKey = message.split("\\.").lastOption.getOrElse("").split("\\W+").headOption.getOrElse("") 
      errorJson(s"${foreingKey}Id is not defined :" +
        s" At least one of the employees was not inserted because the ${foreingKey}Id " +
        s"is not defined in it's own table." +
            s" The rest have been inserted.") 
    }
  }
}