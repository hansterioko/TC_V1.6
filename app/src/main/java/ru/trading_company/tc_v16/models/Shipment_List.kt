package ru.trading_company.tc_v16.models

import java.io.Serializable

data class Shipment_List (val id: Int,
                     var product: Product,
                     var count: Int,
                     var price: Int,
                     val shipment: Shipment,): Serializable {
}