package utils

import play.api.libs.json.Json

/**
 * Created by MRM
 */
trait JsonUtils {

    def errorJson(message: String) = Json.obj("error" -> message)

}