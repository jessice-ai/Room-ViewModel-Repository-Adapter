package com.example.sun13.data.repository

import com.example.sun13.data.room.SunUser
import com.example.sun13.data.room.SunUserDao
import com.example.sun13.data.room.SunUserDataBase


class SunRepository(private val SunUserDao: SunUserDao) {

    var database:SunUserDataBase?= null

    fun getAlldata(): List<SunUser>? {
        //println("JessiceDao"+SunUserDao)
        var xdun = SunUserDao?.getAll()
        // println("Jessice---xxx---"+xdun)
        return xdun
    }
    fun getstrind():String{
        return "jessice:aaaa";
    }

    fun delete(id: Int) {
        SunUserDao?.deleteUserById(id)
    }
    fun insertData(SunUser:SunUser){
        SunUserDao?.insertData(SunUser)
    }
    fun update_first_name(id: Int, value: String) {
        SunUserDao?.update_first_name(id,value)
    }

}
