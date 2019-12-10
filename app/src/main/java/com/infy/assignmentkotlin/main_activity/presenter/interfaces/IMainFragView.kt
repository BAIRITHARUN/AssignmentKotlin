package com.infy.assignmentkotlin.main_activity.presenter.interfaces

import com.infy.assignmentkotlin.main_activity.model.Row
import com.infy.assignmentkotlin.room_database.RoomEntity
import java.util.ArrayList

interface IMainFragView {
    fun checkIntentConnection(): Boolean

    fun setActionBarTitle(title: String)

    fun setList(rowArrayList: ArrayList<Row>)

    fun showLoading()

    fun hideRefreshing()

    fun showToast(message: String)

    fun getTitlesFromLocal()

    fun setList(roomEntityList: List<RoomEntity>)

    fun clearLocalDb(roomEntityList: List<RoomEntity>)
}