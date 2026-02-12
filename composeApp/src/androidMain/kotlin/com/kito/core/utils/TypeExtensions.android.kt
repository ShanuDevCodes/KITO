
package com.kito.core.utils

actual fun Double.formatDecimal(digits: Int): String {
    return String.format("%.${digits}f", this)
}
