package pl.patrykdolata.chatapp.utils

object StringUtils {


    fun isEmpty(str: String?): Boolean {
        return str == null || str == ""
    }

    fun isNotEmpty(str: String?): Boolean {
        return !isEmpty(str)
    }
}