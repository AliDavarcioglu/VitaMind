package com.dvc.vitamind.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dvc.vitamind.model.User

@Dao
interface UserDao {

    @Query("SELECT name,id FROM user")
    fun getUserWithNameAndId(): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id:Int):User?

    @Query("SELECT * FROM User ORDER BY id DESC LIMIT 1")
    suspend fun getLastUser(): User?

    @Insert
    suspend fun insert (user: User)

    @Delete
    suspend fun delete (user: User)

}