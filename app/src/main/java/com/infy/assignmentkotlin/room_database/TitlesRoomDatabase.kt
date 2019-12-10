package com.infy.assignmentkotlin.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoomEntity::class], exportSchema = false, version = 1)
abstract class TitlesRoomDatabase : RoomDatabase() {

    abstract fun titlesDao(): TitlesDao

    companion object {

        private val DB_NAME = "Titles.db"
        private var instance: TitlesRoomDatabase? = null
        @Volatile
        private var INSTANCE: TitlesRoomDatabase? = null

        fun getInstance(context: Context): TitlesRoomDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    TitlesRoomDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance as TitlesRoomDatabase

//            val tempInstance = INSTANCE
//            if (tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    TitlesRoomDatabase::class.java,
//                    DB_NAME
//                ).build()
//                INSTANCE = instance
//                return instance
//            }
        }
    }

}