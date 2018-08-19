package co.zsmb.staticsite.util

import java.util.*

fun Date.formatForDisplay(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this

    return String.format("%d.%02d.%02d. %dh",
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY)
    )
}
