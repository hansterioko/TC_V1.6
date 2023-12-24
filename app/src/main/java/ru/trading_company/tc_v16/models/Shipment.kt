package ru.trading_company.tc_v16.models

import java.io.Serializable
import java.time.LocalDate

data class Shipment (val id: Int,
                var price: Int,
                var date: LocalDate,
                var discount: Int,
                val shop: Shop,): Serializable {
}