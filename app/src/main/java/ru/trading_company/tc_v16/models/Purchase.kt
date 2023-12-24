package ru.trading_company.tc_v16.models

import ru.trading_company.tc_v16.DTOmodels.Purchase_ListDTO
import java.io.Serializable
import java.time.LocalDate

data class Purchase (val id: Int,
                     var price: Int,
                     var date: LocalDate,
                     val company: Company,): Serializable {
}