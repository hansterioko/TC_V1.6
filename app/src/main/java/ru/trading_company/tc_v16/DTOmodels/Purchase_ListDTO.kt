package ru.trading_company.tc_v16.DTOmodels

import ru.trading_company.tc_v16.models.Product
import ru.trading_company.tc_v16.models.Purchase
import java.io.Serializable

data class Purchase_ListDTO (val id: Int,
                          var product: Product,
                          var count: Int, ): Serializable {
}