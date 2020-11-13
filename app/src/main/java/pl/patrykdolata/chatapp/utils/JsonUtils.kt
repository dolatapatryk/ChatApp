package pl.patrykdolata.chatapp.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

object JsonUtils {

    private val gson: Gson = Gson()

    fun toJson(obj: Any): String {
        if (obj is String)
            return obj
        return gson.toJson(obj)
    }

    fun <T> toObject(json: String, klazz: Class<T>): T? {
        return try {
            gson.fromJson(json, klazz)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}