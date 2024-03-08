package com.example.datastorage.data


data class Contact(
    val name:String,
    val number: String,
    val id: Int
)

var contacts: ArrayList<Contact> = ArrayList()
var note:String = ""
const val REQUEST_CODE_READ_CONTACTS = 1
 var READ_CONTACTS_GRANTED = false
var noteText = ""

