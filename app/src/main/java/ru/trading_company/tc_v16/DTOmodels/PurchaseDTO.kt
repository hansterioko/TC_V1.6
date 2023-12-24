package ru.trading_company.tc_v16.DTOmodels

import ru.trading_company.tc_v16.models.Company
import ru.trading_company.tc_v16.models.Purchase_List
import java.io.Serializable
import java.time.LocalDate

data class PurchaseDTO (val id: Int,
                        var price: Int,
                        var date: LocalDate,
                        var purchase_list: List<Purchase_ListDTO>,
                        val company: Company,): Serializable {
}