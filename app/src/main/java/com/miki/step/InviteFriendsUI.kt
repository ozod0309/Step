package com.miki.step

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Textsms
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.miki.step.lib.PhoneContact
import com.miki.step.lib.PhoneContactNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InviteFriendsUI(val context: Context?) {
    @SuppressLint("NotConstructor")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI(
        onBackPressed: () -> Unit,
        onInvite: (number: String) -> Unit
    ) {
        val openNumbersList = remember { mutableStateOf(false) }
        val phoneContactList = remember {
            mutableStateListOf<PhoneContact>()
        }
        val filteredContactList = remember {
            mutableStateListOf<PhoneContact>()
        }
        val phoneContactSize = remember {
            mutableIntStateOf(0)
        }
        val enableSearch = remember {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()
        LaunchedEffect(key1 = Unit) {
            scope.launch(Dispatchers.IO) {
                loadContacts(
                    context!!,
                    phoneContactList,
                    filteredContactList,
                    phoneContactSize,
                    enableSearch
                )
            }
        }
        val selectedContactIndex = remember {
            mutableIntStateOf(-1)
        }
        var searchContact by rememberSaveable { mutableStateOf("") }
        val listState = rememberLazyListState()
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Text(text = stringResource(id = R.string.invite_friends))
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    onBackPressed()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    contentDescription = ""
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            titleContentColor = MaterialTheme.colorScheme.secondary,
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    LazyColumn(
                        state = listState
                    ) {
                        items(phoneContactSize.intValue) { index ->
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(5.dp, 10.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedContactIndex.intValue = index
                                        openNumbersList.value = !openNumbersList.value
                                    }
                            ) {
                                Spacer(modifier = Modifier.width(15.dp))
                                Icon(
                                    imageVector = Icons.Filled.Phone,
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(15.dp))
                                Text(
                                    text = filteredContactList[index].name,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actions = {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(),
                            value = searchContact,
                            readOnly = !enableSearch.value,
                            onValueChange = { textValue ->
                                searchContact = textValue
                                filteredContactList.clear()
                                phoneContactList.map {
                                    if (it.name.contains(textValue, true)) filteredContactList.add(
                                        it
                                    )
                                }
                                phoneContactSize.intValue = filteredContactList.size
                            },
                            label = { Text(stringResource(id = R.string.contact_search)) }
                        )
                    }
                )
            }
        )
        if (openNumbersList.value) {
            ShowNumbers(
                showNumbersList = openNumbersList,
                numbers = phoneContactList[selectedContactIndex.intValue].number,
                filteredContactList[selectedContactIndex.intValue].name,
                onInvite = {
                    onInvite(it)
                }
            )
        }
    }

    @SuppressLint("Range")
    private fun loadContacts(
        context: Context,
        phoneContactList: MutableList<PhoneContact>,
        filteredContactList: MutableList<PhoneContact>,
        phoneContactSize: MutableState<Int>,
        enableSearch: MutableState<Boolean>
    ) {
        val contentResolver: ContentResolver = context.contentResolver
        val cursor =
            contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )
        if (cursor?.count!! > 0) {
            while (cursor.moveToNext()) {
                val id =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val numbers: ArrayList<PhoneContactNumber> = arrayListOf()
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur!!.moveToNext()) {
                        val phoneNo =
                            pCur.getString(
                                pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )
                                .filter { !it.isWhitespace() }
                        if (!numbers.contains(
                                PhoneContactNumber(
                                    phoneNo
                                )
                            )
                        ) {
                            numbers.add(PhoneContactNumber(phoneNo))
                        }
                    }
                    pCur.close()
                }
                phoneContactList.add(PhoneContact(name, numbers))
                filteredContactList.add(PhoneContact(name, numbers))
                phoneContactSize.value = phoneContactList.size
            }
        }
        cursor.close()
        enableSearch.value = true
    }

    @ExperimentalMaterial3Api
    @Composable
    fun ShowNumbers(
        showNumbersList: MutableState<Boolean>,
        numbers: MutableList<PhoneContactNumber>,
        contactName: String,
        onInvite: (number: String) -> Unit
    ) {
        if (showNumbersList.value) {
            BasicAlertDialog(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true

                ),
                onDismissRequest = { showNumbersList.value = false }
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    tonalElevation = AlertDialogDefaults.TonalElevation,
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                titleContentColor = MaterialTheme.colorScheme.secondary,
                                containerColor = MaterialTheme.colorScheme.primary
                            ),

                            title = {
                                Text(text = contactName)
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        showNumbersList.value = !showNumbersList.value
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        contentDescription = ""
                                    )
                                }
                            }
                        )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(numbers.size) { index ->
                                Spacer(modifier = Modifier.width(20.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(5.dp, 10.dp)
                                        .fillMaxWidth()
                                        .clickable {
//                                openNumbersList.value = !openNumbersList.value
                                        }
                                ) {
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(
                                        text = numbers[index].number,
                                        modifier = Modifier.weight(1f),
                                    )
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Icon(
                                        imageVector = Icons.Filled.Textsms,
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = null,
                                        modifier = Modifier.clickable {
                                            onInvite(numbers[index].number)
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        TextButton(
                            onClick = {
                                showNumbersList.value = false
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(text = stringResource(id = R.string.close))
                        }
                    }
                }
            }
        }
    }
}
