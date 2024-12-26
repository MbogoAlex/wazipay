package com.escrow.wazipay.utils

import java.text.NumberFormat
import java.util.Locale

fun formatMoneyValue(amount: Double): String {
    return  NumberFormat.getCurrencyInstance(Locale("en", "KE")).format(amount)
}