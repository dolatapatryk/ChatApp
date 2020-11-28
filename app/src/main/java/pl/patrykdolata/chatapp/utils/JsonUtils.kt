package pl.patrykdolata.chatapp.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import pl.patrykdolata.chatapp.models.Friend

object JsonUtils {

    val gson: Gson = Gson()

    fun <T> mapToModel(data: Map<String, String>, klazz: Class<T>): T? {
        val json = toJson(data)
        return fromJson(json, klazz)
    }

    fun toJson(obj: Any): String {
        if (obj is String)
            return obj
        return gson.toJson(obj)
    }

    fun <T> fromJson(json: String, klazz: Class<T>): T? {
        return try {
            gson.fromJson(json, klazz)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    inline fun <reified T> fromJsonArray(json: String): T {
        return gson.fromJson(json, object: TypeToken<T>(){}.type)
    }
}