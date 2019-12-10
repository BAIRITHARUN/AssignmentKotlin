package com.infy.assignmentkotlin.main_activity.presenter.implementation

import com.infy.assignmentkotlin.main_activity.model.Row
import com.infy.assignmentkotlin.network.APIUtils
import com.infy.assignmentkotlin.room_database.RoomEntity
import org.junit.Test

import org.junit.Assert.*
import java.util.ArrayList

class MainInteractImplTest {

    @Test
    fun getTitles() {
        val call = APIUtils.getAPI("s/2iodh4vg0eortkl/facts.json")
        try {
            val response = call.execute()
            assertTrue(response.isSuccessful())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Test
    fun prepareLocalDbList() {
        val titlesArrayList = ArrayList<RoomEntity>()
        for (i in 0..2) {
            val titles = RoomEntity(
                "Test",
                "Test",
                "http://1.bp.blogspot.com/_VZVOmYVm68Q/SMkzZzkGXKI/AAAAAAAAADQ/U89miaCkcyo/s400/the_golden_compass_still.jpg"
            )
            titlesArrayList.add(titles)
        }
        assertEquals(3, titlesArrayList.size.toLong())
    }

    @Test
    fun prepareList() {
        val titlesModelArrayList = ArrayList<Row>()
        for (i in 0..4) {
            val row = Row()
            row.title = "Test"
            row.description = "Test"
            row.imageHref = "http://1.bp.blogspot.com/_VZVOmYVm68Q/SMkzZzkGXKI/AAAAAAAAADQ/U89miaCkcyo/s400/the_golden_compass_still.jpg"
            titlesModelArrayList.add(row)
        }
        assertEquals(5, titlesModelArrayList.size.toLong())
    }
}