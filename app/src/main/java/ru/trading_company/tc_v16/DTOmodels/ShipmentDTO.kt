package ru.trading_company.tc_v16.DTOmodels

import ru.trading_company.tc_v16.models.Shop
import java.io.Serializable
import java.time.LocalDate

data class ShipmentDTO (val id: Int,
                   var price: Int,
                   var date: LocalDate,
                   var shipment_list: List<Shipment_ListDTO>,
                   var discount: Int,
                   var shop: Shop,): Serializable {
}