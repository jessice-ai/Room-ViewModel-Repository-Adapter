package com.example.sun13

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sun13.data.viewmodel.MyViewModel
import com.example.sun13.adapter.SunMyAdapter
import com.example.sun13.data.room.SunUser
import com.example.sun13.data.room.SunUserDataBase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity() : AppCompatActivity() {
    /**
     * lateinit 延迟初始化，一个Viewmodel
     */
    //private lateinit var binding: ActivityMainBinding
    lateinit var myViewModel: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        //println("Jessice的:"+xundl)
        /**
         * 插入数据方法二：
         * 这种用的少，基础
         */
        //insert数据
        val dao = SunUserDataBase.getInstance(application)?.getUserDao()
        for (i in (30 until 40)) {
            val user = SunUser(i,firstName = "李四$i", lastName = "110$i",birthday="06052",picture="img",nationality="niun")
            if (dao != null) {
                dao.insertData(user)
            }
        }
        /**
         * 实例化一个 ViewModel 实例
         */
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        /**
         * 删除数据方式一：
         * 这种用的多，需熟悉
         */
        myViewModel.delete(23)
        /**
         * 更新数据方式一：
         * 这种用的多，需熟悉
         */
        myViewModel.update_first_name(22,"平安")
        /**
         * 查询数据方式一：
         * 实例化一个ViewModel
         * 在 ViewModel 中使用 Repository 跟Dao结合，查询数据
         * 这种用的多，需熟悉
         */
        var xundl = myViewModel.getAllWords()

        /**
         * 添加数据方式一：
         * 这种用的多，需熟悉
         */
        val user = SunUser(11,firstName = "小黄鹂", lastName = "110",birthday="06052",picture="img",nationality="niun")
        myViewModel.insert(user)
        /**
         * 查询数据方式二：
         * 读取出所有的数据库中数据
         * 普通方式，在Activity 中使用数据库操作方法，查询数据
         * 这种用的少，基础
         */
//
//        var userDataArray = dao?.loadAllUsers()
//        val list = ArrayList<SunUser>()
//        userDataArray?.let {
//            for (sun in it) {
//                val item = SunUser(sun.id,sun.firstName, sun.lastName,sun.birthday,sun.picture,sun.nationality)
//                list += item
//            }
//        }
        //println("MMMM"+list);

        /**
         * 查询出来的数据，通过Adapter 适配器，应用到View层
         */
        sun_list_item_Recyclerview_id.adapter = xundl?.let { SunMyAdapter(it) }
        sun_list_item_Recyclerview_id.layoutManager = LinearLayoutManager(this)
        sun_list_item_Recyclerview_id.setHasFixedSize(true)
    }
}