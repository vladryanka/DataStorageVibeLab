package com.example.datastorage

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.datastorage.data.Contact
import com.example.datastorage.data.READ_CONTACTS_GRANTED
import com.example.datastorage.data.REQUEST_CODE_READ_CONTACTS
import com.example.datastorage.data.contacts
import com.example.datastorage.ui.theme.DataStorageTheme
import android.Manifest
import android.database.Cursor


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DataStorageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val hasReadContactPermission =
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)

                    if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
                        READ_CONTACTS_GRANTED = true
                    } else {
                        // вызываем диалоговое окно для установки разрешений
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_CONTACTS),
                            REQUEST_CODE_READ_CONTACTS
                        )
                    }
                    // если разрешение установлено, загружаем контакты
                    if (READ_CONTACTS_GRANTED) {
                        loadContacts()
                    }
                    ContactsListApp()
                }
            }
        }
    }

    private fun loadContacts() {
        val contentResolver: ContentResolver = getContentResolver()
        val cursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                val phoneNumberIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                if (displayNameIndex >= 0 && phoneNumberIndex >= 0) {
                    val name = cursor.getString(displayNameIndex)
                    val hasPhoneNumber = cursor.getString(phoneNumberIndex).toInt()

                    if (hasPhoneNumber > 0) {
                        val index = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                        val id = if (index >= 0) {
                            cursor.getString(index)
                        } else {
                            0
                        }
                        val phoneNumbers = this.contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            arrayOf(id.toString()),
                            null
                        )

                        phoneNumbers?.use { phonesCursor: Cursor ->
                            val phoneNumberIndex = phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            while (phonesCursor.moveToNext()) {
                                val phoneNumber = if (phoneNumberIndex != -1) {
                                    phonesCursor.getString(phoneNumberIndex)
                                } else {
                                    "Phone number not found"
                                }
                                contacts.add(Contact(name, phoneNumber))
                            }
                        }
                    }
                }
            }
            cursor.close()
        }
    }
}

@Composable
fun ContactsListApp() {

    Scaffold(
        topBar = {
            ContactsListTopAppBar()
        }
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = it
        ) {
            itemsIndexed(
                contacts
            ) { _, item ->
                ContactItem(
                    contact = item,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsListTopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = TextStyle(
                        fontSize = 50.sp
                    )
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactItem(
    contact: Contact,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        onClick = { expanded = !expanded },
        modifier = modifier
    ) {
        Column(modifier = modifier) {
            Text(
                text = contact.name,
                style = TextStyle(
                    fontSize = 32.sp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = contact.number,

                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DataStorageTheme {
        ContactsListApp()
    }
}