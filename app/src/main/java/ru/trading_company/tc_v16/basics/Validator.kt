package com.example.publishinghousekotlin.basics

class Validator {
    fun isValidName(name:String): String?{
        if (name.length == 0){
            return "Необходимо ввести"
        }

        if(name.length > 40) {
            return "Максимальная длина: 40 символов"
        }

        return null
    }

    fun isValidCategory(category:String): String?{
        if (category.length == 0){
            return "Введите категорию товара"
        }

        if(category.length > 30) {
            return "Максимальная длина: 30 символов"
        }

        return null
    }

    fun isValidDesc(desc:String): String?{
        if(desc.length > 40){
            return "Максимальная длина: 40 символов"
        }


        return null
    }

    fun isValidUnit(unit:String): String?{
        if(unit.length == 0){
            return "Необходимо ввести единицу измерения"
        }

        return null
    }

    fun isValidVat(vat:String): String?{
        if(vat.length == 0){
            return "Необходимо ввести НДС"
        }

        if(Integer.valueOf(vat) < 10){
            return "НДС должен быть больше 10%"
        }

        if(Integer.valueOf(vat) > 20){
            return "НДС должен быть меньше 20%"
        }

        return null
    }

    fun isValidPrice(price:String): String?{
        if(price.length == 0){
            return "Введите цену закупки"
        }

        return null
    }
}