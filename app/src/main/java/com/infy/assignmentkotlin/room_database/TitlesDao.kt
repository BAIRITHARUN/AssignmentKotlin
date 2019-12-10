package com.infy.assignmentkotlin.room_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TitlesDao {

    @get:Query("Select * from Titles")
    val titlesList: List<RoomEntity>

    @Query("Delete from Titles")
    fun deleteTitle()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(roomEntity: RoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListOfUsers(titlesArrayList: List<RoomEntity>)
}