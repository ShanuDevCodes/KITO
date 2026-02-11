
package com.kito.core.utils

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun Double.formatDecimal(digits: Int): String {
    return NSString.stringWithFormat("%.${digits}f", this)
}
