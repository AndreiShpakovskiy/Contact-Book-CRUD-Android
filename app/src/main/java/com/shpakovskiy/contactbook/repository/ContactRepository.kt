package com.shpakovskiy.contactbook.repository

import com.shpakovskiy.contactbook.entity.Contact

interface ContactRepository {
    fun getAllContacts(): List<Contact>
    fun getContactById(id: Int): Contact
    fun addContact(contact: Contact): Long
    fun updateContact(contact: Contact): Int
    fun removeContact(contact: Contact): Int
}