package com.example.sun13.data.room

import androidx.room.*

/**
 * 操作数据库 - 增删改查
 * @Insert、@Delete、@Update 和 @Query
 * Dao 不支持泛型
 * 注意：
 * 1、update , delete 不支持 livedata
 * 2、suspend （挂起）的函数，不支持 livedata，例如：suspend fun getAll(): LiveData<List<VideoInfo>>，这种写法会报错
 * 3、query 支持 LiveData
 */

/**
 * 添加一下代码到build.gradle 文件中：开启并查看表的创建情况

javaCompileOptions {
annotationProcessorOptions {
arguments = [
"room.schemaLocation":"$projectDir/schemas".toString(),
"room.incremental":"true",
"room.expandProjection":"true"]
}
}
 */

@Dao
interface SunUserDao {
    /**
     * 这里返回值是LiveData而不是 MutableLiveData，因为我们不想其他的类能修改它的值
     * 这里的 getAll() 必须指定返回的数据类型，否则报错
     */
    @Query("SELECT * FROM sunuser")
    fun getAll(): List<SunUser>   //希望监听这个表的变化，为其加上LiveData

    @Query("SELECT * FROM sunuser WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): SunUser

    /**
     * 插入一条数据,我们直接定义一个方法并用 @Insert 注解
     * 参数：onConflict 介绍
     * 说明：当插入的数据已经存在时候的处理逻辑
     * onConflict 有3个值：
     * 1、OnConflictStrategy.REPLACE  #替换
     * 2、OnConflictStrategy.ABORT  #终止
     * 3、OnConflictStrategy.IGNORE #忽略，直接插入
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(users: SunUser)


    /**
     * 自定义删除条件
     * 也可以写成一下结构
     */
    @Query("delete from sunuser where id = :id ")
    fun deleteUserById(id:Int)

    /**
     * 默认根据主键更新
     */
    @Update
    fun updateUserByUser(user: SunUser)
    /**
     * 自定义更新条件
     * 也可以写成下面结构
     */
    @Query("update sunuser set first_name = :first_name where id =  :id")
    fun update_first_name(id: Int, first_name: String)

    @Query("SELECT * FROM sunuser")
    fun loadAllUsers(): Array<SunUser>
}
