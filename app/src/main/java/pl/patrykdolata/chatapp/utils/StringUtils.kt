package pl.patrykdolata.chatapp.utils

import java.text.SimpleDateFormat
import java.util.*

object StringUtils {

    fun isEmpty(str: String?): Boolean {
        return str == null || str == ""
    }

    fun isNotEmpty(str: String?): Boolean {
        return !isEmpty(str)
    }

    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        var pattern: String
        pattern = if (isSameDay(date, Date())) {
            "HH:mm"
        } else {
            "dd.MM"
        }
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return formatter.format(date1) == formatter.format(date2)
    }
}