package ru.trading_company.tc_v16.models

import java.io.Serializable

data class Product(
    val id: Int,
    var name: String,
    var vat: Int,
    var characteristic: String,
    var unit: String,
    var category: String,
    var price: Int,
    var countOnWarehouse: Int,
    val company: Company?,
    var photo: String,):Serializable {
}