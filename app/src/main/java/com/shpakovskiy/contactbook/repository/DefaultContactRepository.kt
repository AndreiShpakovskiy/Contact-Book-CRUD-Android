package com.shpakovskiy.contactbook.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.shpakovskiy.contactbook.entity.Contact
import kotlin.collections.ArrayList

class DefaultContactRepository(private val context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    ContactRepository {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "contacts_database"
        private const val TABLE_CONTACTS = "contacts"

        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_PHONE_NUMBER = "phone_number"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE IF NOT EXISTS $TABLE_CONTACTS($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_PHONE_NUMBER TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery =
            "DROP TABLE IF EXISTS $TABLE_CONTACTS"
        db?.execSQL(dropTableQuery)
    }

    override fun getAllContacts(): List<Contact> {
        val contactsList = ArrayList<Contact>()
        val selectAllContactsQuery = "SELECT * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectAllContactsQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectAllContactsQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                val newContact = Contact()

                newContact._id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                newContact.name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                newContact.phoneNumber = cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER))

                contactsList.add(newContact)
            } while (cursor.moveToNext())
        }

        return contactsList
    }

    override fun getContactById(id: Int): Contact {
        val selectContactWithIdQuery =
                "SELECT * FROM $TABLE_CONTACTS WHERE $KEY_ID = $id"

        val db = this.readableDatabase
        val cursor: Cursor?
        val selectedContact = Contact()

        try {
            cursor = db.rawQuery(selectContactWithIdQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectContactWithIdQuery)
            return selectedContact
        }

        if (cursor.moveToFirst()) {
            selectedContact._id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            selectedContact.name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
            selectedContact.phoneNumber = cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER))
        }

        return selectedContact
    }

    override fun addContact(contact: Contact): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        with(contentValues) {
            put(KEY_NAME, contact.name)
            put(KEY_PHONE_NUMBER, contact.phoneNumber)
        }

        val newContactId = db.insert(TABLE_CONTACTS, null, contentValues)

        db.close()
        return newContactId
    }

    override fun updateContact(contact: Contact): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        with(contentValues) {
            put(KEY_NAME, contact.name)
            put(KEY_PHONE_NUMBER, contact.phoneNumber)
        }

        val resultCode = db.update(TABLE_CONTACTS, contentValues, "$KEY_ID = ${contact._id}", null)
        db.close()
        return resultCode
    }

    override fun removeContact(contact: Contact): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, contact._id)

        Toast.makeText(context, "I'm removing ${contact.name} ${contact._id}", Toast.LENGTH_LONG).show()

        val resultCode = db.delete(TABLE_CONTACTS, "$KEY_ID = ${contact._id}", null)
        db.close()
        return resultCode
    }
}