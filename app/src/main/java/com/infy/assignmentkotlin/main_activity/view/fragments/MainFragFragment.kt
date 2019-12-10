package com.infy.assignmentkotlin.main_activity.view.fragments


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.infy.assignmentkotlin.R
import com.infy.assignmentkotlin.common.Utility
import com.infy.assignmentkotlin.main_activity.model.Row
import com.infy.assignmentkotlin.main_activity.presenter.implementation.MainInteractImpl
import com.infy.assignmentkotlin.main_activity.presenter.interfaces.IMainFragView
import com.infy.assignmentkotlin.main_activity.presenter.interfaces.IMainInteracter
import com.infy.assignmentkotlin.main_activity.view.activity.MainActivity
import com.infy.assignmentkotlin.main_activity.view.adapter.TitlesAdapter
import com.infy.assignmentkotlin.room_database.RoomEntity
import com.infy.assignmentkotlin.room_database.TitlesRoomDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.ArrayList

/*
 * IMainInteractor will Interacts with Business Logic class(MainInteractImpl)
 * Room Database is used to store in Local Database
 * Room Database actions will execute in AsyncTask
 * If we run Room Database operations in Main Thread it leads to ANR Exception
 * for that AsyncTask used for Run in New Threads*/

class MainFragFragment : Fragment(), IMainFragView {

    internal lateinit var mRcvTitlesList: RecyclerView
    internal lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    internal lateinit var mTvNetWorkStatus: TextView

    internal lateinit var interpreter: IMainInteracter
    internal lateinit var titlesAdapter: TitlesAdapter

    internal var networkReceiver: BroadcastReceiver? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.inflater_main_fragment, container, false)
        mRcvTitlesList = rootView.findViewById(R.id.mRcvTitlesList)
        mSwipeRefreshLayout = rootView.findViewById(R.id.mSwipeRefreshLayout)
        mTvNetWorkStatus = rootView.findViewById(R.id.mTvNetWorkStatus)
        interpreter = MainInteractImpl(this)
//        interpreter.getTitles()
        checkInternetStatus()
        mSwipeRefreshLayout.setOnRefreshListener {
            checkInternetStatus()
        }
        return rootView
    }

    /*method calls checkInternetConnection() in Utility
     * by calling checkInternetConnection() it will check mobile network or wifi is enabled or not
     * if any of the mobile network and wifi is enabled it will return true otherwise false*/
    override fun checkIntentConnection(): Boolean {
        return Utility.checkInternetConnection(activity as Context)
    }

    /*method for updating action bar title*/
    override fun setActionBarTitle(title: String) {
        (this.activity as MainActivity).supportActionBar?.setTitle(title)
    }

    /*method for setting list in recycler view*/
    override fun setList(rowArrayList: ArrayList<Row>) {
        titlesAdapter =
            TitlesAdapter(this, rowArrayList, object : TitlesAdapter.IOnRowClickListener {
                override fun onRowClick(position: Int) {}
            })
        mRcvTitlesList.layoutManager = LinearLayoutManager(this.activity)
        mRcvTitlesList.itemAnimator = DefaultItemAnimator()
        mRcvTitlesList.adapter = titlesAdapter
        mRcvTitlesList.hasFixedSize()
        mRcvTitlesList.setItemViewCacheSize(20)
        mRcvTitlesList.isDrawingCacheEnabled = true
        mRcvTitlesList.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
    }

    /*method for showing circular loading progress*/
    override fun showLoading() {
        mSwipeRefreshLayout.isRefreshing = true
    }

    /*method for hiding circular loading progress*/
    override fun hideRefreshing() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    /*method for showing toast message*/
    override fun showToast(message: String) {
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
    }

    /*method executes AsyncTask  to Get list of Titles table from Room Data base
     * */
    override fun getTitlesFromLocal() {
        class GetTasks : AsyncTask<Void, Void, List<RoomEntity>>() {

            override fun onPreExecute() {
                super.onPreExecute()
            }

            override fun doInBackground(vararg voids: Void): List<RoomEntity> {
                return TitlesRoomDatabase
                    .getInstance(activity!!.applicationContext).titlesDao().titlesList
            }

            override fun onPostExecute(tasks: List<RoomEntity>) {
                super.onPostExecute(tasks)
                interpreter.prepareList(tasks)
            }
        }

        val gt = GetTasks()
        gt.execute()
    }

    /*
     * method executes AsyncTask  to insert list into Titles table in Room Data base
     * */
    override fun setList(roomEntityList: List<RoomEntity>) {

        class InsertTitles : AsyncTask<Void, Void, Void>() {

            override fun onPreExecute() {
                super.onPreExecute()
            }
            override fun doInBackground(vararg params: Void?): Void? {
                TitlesRoomDatabase.getInstance(activity!!.applicationContext).titlesDao()
                    .insertListOfUsers(roomEntityList.requireNoNulls())
                return null
            }
        }


        val st = InsertTitles()
        st.execute()

    }

    /*
     * method executes AsyncTask to delete rows in Titles table from Room Data base
     * */
    override fun clearLocalDb(roomEntityList: List<RoomEntity>) {

        class DeleteTitle : AsyncTask<Void, Void, Void>(){

            override fun onPreExecute() {
                super.onPreExecute()
            }

            override fun doInBackground(vararg params: Void?): Void? {
                TitlesRoomDatabase.getInstance(activity!!.applicationContext).titlesDao()
                    .deleteTitle()
                return null
            }

            override fun onPostExecute(result: Void?) {
                if (roomEntityList.isNotEmpty()) {
                    setList(roomEntityList)
                }
            }
        }

        val dt = DeleteTitle()
        dt.execute();
/*        GlobalScope.launch {
            TitlesRoomDatabase.getInstance(activity!!.applicationContext).titlesDao()
                .deleteTitle()
            setList(roomEntityList)
        }*/

    }

    fun checkInternetStatus() {
        if (networkReceiver == null) {
            networkReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val extras = intent.extras

                    val info = extras!!
                        .getParcelable<Parcelable>("networkInfo") as NetworkInfo?

                    val state = info!!.state
                    if (state == NetworkInfo.State.CONNECTED) {
                        mTvNetWorkStatus.setVisibility(View.GONE)
                        interpreter.getTitles()
                    } else {
                        mTvNetWorkStatus.setVisibility(View.VISIBLE)
                        mTvNetWorkStatus.setText("Your internet Connection is Disabled")
                        interpreter.getTitles()
                    }
                }
            }
            val intentFilter = IntentFilter()
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            this.activity!!.registerReceiver(networkReceiver as BroadcastReceiver, intentFilter)
        } else {
            interpreter.getTitles()
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (networkReceiver != null) {
            this.activity!!.unregisterReceiver(networkReceiver)
        }
    }
}