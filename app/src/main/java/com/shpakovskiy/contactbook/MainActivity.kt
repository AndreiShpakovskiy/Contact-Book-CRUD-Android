package com.shpakovskiy.contactbook

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.shpakovskiy.contactbook.adapter.ContactItemsAdapter
import com.shpakovskiy.contactbook.entity.Contact
import com.shpakovskiy.contactbook.repository.DefaultContactRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.nameInputField
import kotlinx.android.synthetic.main.activity_main.numberInputField
import kotlinx.android.synthetic.main.dialog_update.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showContactsList()

        addContactButton.setOnClickListener {
            addContact(it)

            nameInputField.text.clear()
            numberInputField.text.clear()

            showContactsList()
        }
    }

    private fun showContactsList() {
        contactsListView.layoutManager = LinearLayoutManager(this)
        val itemAdapter = ContactItemsAdapter(this, DefaultContactRepository(this).getAllContacts())
        contactsListView.adapter = itemAdapter
    }

    private fun addContact(view: View) {
        if (nameInputField.text.isEmpty() && numberInputField.text.isEmpty()) {
            Toast.makeText(this, "Both fields can't be empty", Toast.LENGTH_SHORT).show()
        } else {
            val newContact = Contact()

            newContact.name = nameInputField.text.toString()
            newContact.phoneNumber = numberInputField.text.toString()

            if (DefaultContactRepository(this).addContact(newContact) > -1) {
                Toast.makeText(this, "Contact ${newContact.name} saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error. Contact was NOT saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateRecordDialog(contact: Contact) {
        val updateDialog = Dialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.nameInputField.setText(contact.name)
        updateDialog.numberInputField.setText(contact.phoneNumber)

        updateDialog.confirmUpdateButton.setOnClickListener {
            if (updateDialog.nameInputField.text.isEmpty() && updateDialog.numberInputField.text.isEmpty()) {
                Toast.makeText(this, "Both fields can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                contact.name = updateDialog.nameInputField.text.toString()
                contact.phoneNumber = updateDialog.numberInputField.text.toString()

                if (DefaultContactRepository(this).updateContact(contact) > -1) {
                    Toast.makeText(this, "Contact ${contact.name} updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error. Contact was NOT updated!", Toast.LENGTH_SHORT).show()
                }

                showContactsList()

                updateDialog.dismiss()
            }
        }

        updateDialog.closeDialogButton.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()
    }

    fun removeContactAlertDialog(contact: Contact) {
        var dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Remove Contact")
        dialogBuilder.setMessage("Do you really want to remove contact ${contact.name}?")
        dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert)

        dialogBuilder.setPositiveButton("Yes") { dialogInterface, _ ->
            if (DefaultContactRepository(this).removeContact(contact) > -1) {
                Toast.makeText(this, "Contact ${contact.name} removed successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error. Contact was NOT removed!", Toast.LENGTH_SHORT).show()
            }

            showContactsList()

            dialogInterface.dismiss()
        }

        dialogBuilder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}
