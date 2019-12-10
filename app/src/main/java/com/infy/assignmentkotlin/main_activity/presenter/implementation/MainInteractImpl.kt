package com.infy.assignmentkotlin.main_activity.presenter.implementation

import android.os.Build
import com.google.gson.Gson
import com.infy.assignmentkotlin.main_activity.model.Row
import com.infy.assignmentkotlin.main_activity.model.TitilesModel
import com.infy.assignmentkotlin.main_activity.presenter.interfaces.IMainFragView
import com.infy.assignmentkotlin.main_activity.presenter.interfaces.IMainInteracter
import com.infy.assignmentkotlin.network.APIUtils
import com.infy.assignmentkotlin.room_database.RoomEntity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

/* class will perform business logic of MainActivity
    *constructor with IMainFragView Interface
    * for access methods in which implemets IMainFragView*/
class MainInteractImpl (internal var view: IMainFragView) : IMainInteracter {

    /* Method will calls API if internet connection Available
    * else it will calls getTitlesFromLocal() in MainActivity
    * it will return Records from Titles*/
    override fun getTitles() {
        if (view.checkIntentConnection()) {
            val call = APIUtils.getAPI("s/2iodh4vg0eortkl/facts.json")
            view.showLoading()
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    try {
                        assert(response.body() != null)
                        val titilesModel = Gson().fromJson<TitilesModel>(
                            response.body()!!.string(),
                            TitilesModel::class.java!!
                        )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            titilesModel!!.title?.let { view.setActionBarTitle(it) }
                        } else {
                            view.setActionBarTitle("Assignment")
                        }
                        if (titilesModel.rows!!.size != 0) {
                            titilesModel.rows?.let { view.setList(it) }
                            titilesModel.rows?.let { prepareLocalDbList(it) }
                        }
                        view.hideRefreshing()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        view.hideRefreshing()
                        e.message?.let { view.showToast(it) }
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    view.hideRefreshing()
                    t.message?.let { view.showToast(it) }
                }
            })
        } else {
            view.showToast("Please check your Internet Connection")
            view.hideRefreshing()
            view.getTitlesFromLocal()
        }
    }

    /* method will prepare list for Titles Table */
    override fun prepareLocalDbList(titlesModelArrayList: ArrayList<Row>) {
        val titlesArrayList = ArrayList<RoomEntity>()
        for (i in titlesModelArrayList.indices) {
            val titles = RoomEntity(
                titlesModelArrayList[i].title,
                titlesModelArrayList[i].description, titlesModelArrayList[i].imageHref
            )
            titlesArrayList.add(titles)
        }
        view.clearLocalDb(titlesArrayList)
    }

    /*method will prepare list for recyclerview from  list return by Room Database*/
    override fun prepareList(roomEntityList: List<RoomEntity>) {
        val titlesModelArrayList = ArrayList<Row>()
        for (i in roomEntityList.indices) {
            val row = Row()
            row.title = roomEntityList[i].title;
            row.description = roomEntityList[i].description
            row.imageHref =roomEntityList[i].imageUrl
            titlesModelArrayList.add(row)
        }
        view.setList(titlesModelArrayList)
    }
}