package com.example.datastorage.data

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract

data class Contact(
    val name:String,
    val number: String
)

fun getContacts(context: Context): List<String> {
    val contactsList = mutableListOf<String>()
    val contentResolver: ContentResolver = context.contentResolver
    val cursor = contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        null,
        null,
        null,
        null
    )

    cursor?.use {
        while (it.moveToNext()) {
            val contactName = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            contactsList.add(contactName)
        }
    }

    cursor?.close()

    return contactsList
}

var contacts: ArrayList<Contact> = ArrayList()
val REQUEST_CODE_READ_CONTACTS = 1
 var READ_CONTACTS_GRANTED = false
