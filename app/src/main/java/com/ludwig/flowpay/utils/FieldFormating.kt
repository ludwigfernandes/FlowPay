package com.ludwig.flowpay.utils

object FieldFormating {

    fun Double.toCleanString(): String =
        toBigDecimal().stripTrailingZeros().toPlainString()
}