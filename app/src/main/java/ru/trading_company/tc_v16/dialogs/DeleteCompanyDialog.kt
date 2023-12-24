package ru.trading_company.tc_v16.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.publishinghousekotlin.basics.Messages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.trading_company.tc_v16.controllers.CompaniesActivity
import ru.trading_company.tc_v16.controllers.PurchaseActivity
import ru.trading_company.tc_v16.repositories.CompanyRepository
import ru.trading_company.tc_v16.repositories.PurchaseRepository

class DeleteCompanyDialog (private var companyId: Int, private var root: ViewGroup): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
            .setTitle("Удаление компании")
            .setMessage("Вы подтверждаете удаление этой компании?")
            .setPositiveButton("Подтвердить", null)
            .setNegativeButton("Отмена", {_,_->})
            .create().apply {

                setOnShowListener {
                    val positiveButton = getButton(AlertDialog.BUTTON_POSITIVE)
                    positiveButton.setTextColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_error))

                    positiveButton.setOnClickListener {
                        positiveButton.isEnabled = false
                        val messages = Messages()

                        lifecycleScope.launch(Dispatchers.IO) {
                            try{
                                val messageResponse = CompanyRepository().delete(companyId)
                                if(messageResponse != null){

                                    if(messageResponse.code == 500 || messageResponse.code == 404){
                                        messages.showError(messageResponse.message, root)

                                        delay(500)
                                        dismiss()
                                    }else if(messageResponse.code == 200){
                                        messages.showSuccess("Компания успешно удалена!",root)

                                        delay(500)
                                        val intent = Intent(activity, CompaniesActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            }catch (e:Exception){
                                messages.showError("Ошибка удаления компании. Повторите попытку", root)

                                delay(500)
                                dismiss()
                            }
                        }
                    }
                }
            }
    }
}
