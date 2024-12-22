package com.dvc.vitamind.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class User(
    @ColumnInfo("name")
    var name : String?,
    @ColumnInfo("age")
    var age : String?,
    @ColumnInfo("gender")
    var gender : String?,
    @ColumnInfo("weight")
    var weight : Int?,
    @ColumnInfo("height")
    var height : Int?,
    @ColumnInfo("activityLevel")
    var activityLevel : String?,
    @ColumnInfo("healthConditions")
    var healthConditions : String?
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}