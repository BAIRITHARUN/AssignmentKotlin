package com.infy.assignmentkotlin.main_activity.presenter.interfaces

import com.infy.assignmentkotlin.main_activity.model.Row
import com.infy.assignmentkotlin.room_database.RoomEntity
import java.util.ArrayList

interface IMainInteracter {
    fun getTitles()

    fun prepareLocalDbList(titilesModelArrayList: ArrayList<Row>)

    fun prepareList(roomEntityList: List<RoomEntity>)
}
