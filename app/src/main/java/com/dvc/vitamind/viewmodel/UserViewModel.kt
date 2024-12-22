package com.dvc.vitamind.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.dvc.vitamind.model.User
import com.dvc.vitamind.roomdb.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application:Application): AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        getApplication(),
        UserDatabase::class.java,"Users"
    ).build()

    private val userDao = db.userDao()
    val selectedUser = mutableStateOf<User>(User("","","",0,0,"",""))

    val userList = mutableStateOf<List<User>>(emptyList())


    fun getUserList(){
        userDao.getUserWithNameAndId()
    }

    fun getUser(id:Int){
        viewModelScope.launch (Dispatchers.IO) {
            val user = userDao.getUserById(id)
            user?.let {
                selectedUser.value = it
            }
        }
    }

    fun saveUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insert(user)
        }
    }

    fun getLastUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getLastUser()
            user?.let {
                selectedUser.value = it
            }
        }
    }

    fun deleteUser(user: User){
        viewModelScope.launch {
            userDao.delete(user)
        }
    }





}