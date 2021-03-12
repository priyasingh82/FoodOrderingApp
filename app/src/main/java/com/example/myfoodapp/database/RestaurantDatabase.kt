package com.example.myfoodapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myfoodapp.database.RestaurantEntity

@Database(entities = [RestaurantEntity::class, OrderEntity::class], version = 1)
abstract class RestaurantDatabase : RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

    abstract fun orderDao(): OrderDao

}
