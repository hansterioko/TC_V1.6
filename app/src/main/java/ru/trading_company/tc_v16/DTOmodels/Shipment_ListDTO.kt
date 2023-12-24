package ru.trading_company.tc_v16.DTOmodels

import ru.trading_company.tc_v16.models.Product
import ru.trading_company.tc_v16.models.Shipment
import java.io.Serializable

data class Shipment_ListDTO (val id: Int,
                             var product: Product,
                             var count: Int,
                             var price: Int, ): Serializable {

}