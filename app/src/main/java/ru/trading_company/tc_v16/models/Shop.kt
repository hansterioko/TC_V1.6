package ru.trading_company.tc_v16.models

import java.io.Serializable

data class Shop (val id: Int,
            var name: String,
            var city: String,
            var street: String,
            var house: String,): Serializable {
}