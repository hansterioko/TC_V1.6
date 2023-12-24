package com.example.publishinghousekotlin.basics

import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Listener {

    private var validator = Validator()
    fun nameListener(nameText: TextInputEditText, nameContainer: TextInputLayout){
        nameText.addTextChangedListener {
            nameContainer.helperText = validator.isValidName(nameText.text.toString())
        }
    }

    fun categoryListener(categoryText: TextInputEditText, categoryContainer: TextInputLayout){
        categoryText.addTextChangedListener {
            categoryContainer.helperText = validator.isValidName(categoryText.text.toString())
        }
    }

    fun descListener(descText: TextInputEditText,descContainer: TextInputLayout){
        descText.addTextChangedListener {
            descContainer.helperText = validator.isValidDesc(descText.text.toString())
        }
    }

    fun unitListener(unitText: TextInputEditText, unitContainer: TextInputLayout){
        unitText.addTextChangedListener {
            unitContainer.helperText = validator.isValidUnit(unitText.text.toString())
        }
    }

    fun vatListener(vatText: TextInputEditText, vatContainer: TextInputLayout){
        vatText.addTextChangedListener {
            vatContainer.helperText = validator.isValidVat(vatText.text.toString())
        }
    }

    fun priceListener(priceText: TextInputEditText, priceContainer: TextInputLayout){
        priceText.addTextChangedListener {
            priceContainer.helperText = validator.isValidPrice(priceText.text.toString())
        }
    }
}