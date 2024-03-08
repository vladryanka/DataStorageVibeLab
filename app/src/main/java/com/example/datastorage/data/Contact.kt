package com.example.datastorage.data

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import com.example.datastorage.MainActivity

data class Contact(
    val name:String,
    val number: String,
    val id: Int
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
var note:String = ""
val REQUEST_CODE_READ_CONTACTS = 1
 var READ_CONTACTS_GRANTED = false
var indexOfNote:Long = 0

