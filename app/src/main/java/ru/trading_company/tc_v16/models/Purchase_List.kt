package ru.trading_company.tc_v16.models

import java.io.Serializable

data class Purchase_List (val id: Int,
    var product: Product,
    var count: Int,
    val purchase: Purchase,): Serializable {
}