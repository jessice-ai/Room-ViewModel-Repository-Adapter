##### Kotlin 1.3.72 Android SDK 30.0.0 Jetpack 1.3.0 - 基础模块 - Room 2.2.5 （Entity Dao Database） 数据库 增删改查 ViewModel Repository Adapter 

一、在一下文档的基础上操作

```
/**
 * Repository 保证数据来源的唯一和正确性
 * 不管数据来自本地或网络，ViewModel层不需要知道数据来自哪里
 *
 *           View（Activity/Fragment）
 *                     |
 *                 databinding
 *                     |
 *         ViewModel(livedata或RxJava)
 *                     |
 *                 Repository
 *                     |
 *   local data(Room) 或 Remote Data(Fuel)
 *
 */
```

二、包结构

adapter
data
----repository
----room
----viewmodel

三、依赖安装

```
dependencies {
    /**
     * 添加ViewModel 依赖
     */
    def lifecycle_version = "2.2.0"
    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    // LiveData
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
    //     AndroidX libraries use this lightweight import for Lifecycle
    implementation "androidx.lifecycle:lifecycle-runtime:$lifecycle_version"
    // Annotation processor 注释处理器
    kapt "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
    // optional - ReactiveStreams support for LiveData
    implementation "androidx.lifecycle:lifecycle-reactivestreams:$lifecycle_version" // For Kotlin use lifecycle-reactivestreams-ktx

    /**
     * Adapter 是 RecyclerView 中一个类
     * 安装 RecyclerView 依赖
     */
    implementation "androidx.recyclerview:recyclerview:1.1.0"
    // For control over item selection of both touch and mouse driven selection
    implementation "androidx.recyclerview:recyclerview-selection:1.1.0-rc01"
    implementation "androidx.cardview:cardview:1.0.0"
}
```

四、SunRepository.kt  #内容如下：

```
import com.example.sun9.data.room.SunUser
import com.example.sun9.data.room.SunUserDao
import com.example.sun9.data.room.SunUserDataBase


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
```

四、SunUser.kt

```
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


/**
 * SunUser 实体类
 * @Entity()
 * 也可以写成
 * @Entity(tableName = "SunUser")
 */

@Entity()
data class SunUser (
    /**
     * 主键自动增加
     * 注意：主键，自动增加，不能使用 val 声明，可用 var
     */
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,

    @ColumnInfo(name="first_name")
    var firstName:String,

    @ColumnInfo(name="last_name")
    var lastName:String,

    @ColumnInfo(name="birthday")
    var birthday:String,
    @ColumnInfo(name="picture")
    var picture:String,
    /**
     * 如果不希望某个变量生成表中的属性列，可以使用注解 @Ignore
     */
    @Ignore
    var nationality:String

    /**
     * 说明：
     * 1、实体类中有不想存储的字段，可用@Ignore注解
     * 2、ColumnInfo 字段
     * 3、PrimaryKey 主键
     * 4、@ForeignKey 指定外键 详情，查看 Kotlin 1.3.72 Android SDK 30.0.0 Jetpack 1.3.0 - 基础模块 - Room 2.2.5 .txt
     */
){
    constructor() : this(1, "111", "111","", "", "")
}
```

五、SunUserDao.kt

```
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
```

六、SunUserDataBase.kt

```
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * entities 映射 SunUser 实体类
 * version 指明当前数据库的版本号
 * companion object 单例模式，返回Database，以防止新建过多的实例造成内存的浪费
 */
@Database(version = 1, entities = [SunUser::class])  //生命实体类（Entity）的名字 User 为跟数据库的版本号
abstract class SunUserDataBase : RoomDatabase() {
    abstract fun getUserDao(): SunUserDao //创建DAO的抽象类
    companion object {
        private var INSTANCE: SunUserDataBase? = null  //创建单例
        fun getInstance(context: Context): SunUserDataBase? {
            if (INSTANCE == null) {
                /**
                 * Room.databaseBuilder(context,klass,name).build()来创建Database
                 * 第一个参数 context：上下文
                 * 第二个参数 为当前Database的class字节码文件
                 * 第三个参数为数据库名称
                 * allowMainThreadQueries() #加上这个方法是允许 Room 在主线程上操作，默认是拒绝的，因为操作数据库都还算是比较耗时的动作
                 * 测试的时候，可以加上，正式的时候去掉，直接去掉这个方法
                 */
                INSTANCE = Room.databaseBuilder(
                    context,
                    SunUserDataBase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries().build()
                /**
                 * 数据库升级和降级
                 * 目的，是修改表结构，这很正常，经常会用到
                 * 比如：数据库从版本1升级到版本2，并在版本2上增加了age一列
                 * 使用 addMigrations 函数实现 数据升级跟降级，
                 * addMigrations 的 Migration 参数说明：
                 * 1、Migration 有两个参数，第一个参数：数据库老版本号；第二个参数，数据库新版本号
                 * 2、同时将 @Database注解中的version的值 修改为新数据库的版本号
                 * 3、database.execSQL 中包含 修改表的 SQL 语句
                 */
//                INSTANCE = Room.databaseBuilder(
//                    context,
//                    SunUserDataBase::class.java,
//                    DATABASE_NAME
//                ).addMigrations(object : Migration(1,2){
//                    override fun migrate(database: SupportSQLiteDatabase) {
//                        database.execSQL("ALTER TABLE user ADD age INTEGER Default 0 not null ")
//                    }
//
//                }).build()
            }
            /**
             * 返回数据库
             */
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
        private const val DATABASE_NAME = "sunuser.db"
    }
}
```

七、MyViewModel.kt

```
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sun9.data.repository.SunRepository
import com.example.sun9.data.room.SunUser
import com.example.sun9.data.room.SunUserDataBase


class MyViewModel(application: Application)  : AndroidViewModel(application) {
    var xun:Int = 1
    var mAllWords: List<SunUser>? = null
    var SunRepository:SunRepository? = null
    /**
     * 延迟初始化，一个 Repository 管理数据库操作
     * Repository 是 ViewModel 与 Room 之间的，中间层
     */
//    fun MyViewModel(application: Application){
//        SunRepository = SunRepository()
//    }
    /**
     * 添加数据
     */
    fun insert(SunUser: SunUser) {
        SunRepository?.insertData(SunUser)
    }
//
    /**
     * 更新数据
     */
    fun update_first_name(id:Int, value:String){
        SunRepository?.update_first_name(id,value)
    }
    /**
     * 查询数据库中所有数据
     */
    fun getAllWords(): List<SunUser>? {
        var listSun = SunRepository?.getAlldata()
        return listSun;
    }
    /**
     * 删除数据
     */
    fun delete(id:Int){
        SunRepository?.delete(id)
    }
    //var inputAge = MutableLiveData<Int>()
    /**
     * 初始化模块
     */
    init {
        /**
         * 构造函数（constructor） 初始化代码块（init） 伴生对象（companion object），执行顺序
         *  1、首先执行伴生对象（Companion object）
         *  2、其次执行初始化代码块 （init）
         *  3、再执行构造函数 （constructor）
         */
        val dao = SunUserDataBase.getInstance(application)?.getUserDao()
        SunRepository = dao?.let { SunRepository(it) }

    }
}
```

八、MainActivity.kt

```
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sun9.data.viewmodel.MyViewModel
import com.example.sun9.adapter.SunMyAdapter
import com.example.sun9.data.room.SunUser
import com.example.sun9.data.room.SunUserDataBase
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
```

九、SunMyAdapter.kt

```
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sun9.R
import com.example.sun9.data.room.SunUser
import kotlinx.android.synthetic.main.sun_list_item.view.*

class SunMyAdapter(val sunMyDataList: List<SunUser>) : RecyclerView.Adapter<SunMyAdapter.SunMyViewHolder>() {
    /**
     * 内部类
     * 作用：指定每个View 也就是Item 的数据类型，并声明相对应的变量
     * 下面还有个方法，要使用这个变量，给 View 层赋值
     */
    class SunMyViewHolder(var sunHolder_View : View) : RecyclerView.ViewHolder(sunHolder_View){
        //sunHolder_textView.sun_layout_text1.text = sunholder!!.title
        //val imageView: ImageView = sunHolder_View.image_view
        val textView1: TextView = sunHolder_View.text_view_1
        val textView2: TextView = sunHolder_View.text_view_2
    }

    /**
     * 重写方法一
     * 负责管理每个子项的布局
     * 目的创建 viewHolder（viewHolder 负责 recyclerView 的缓存管理，创建一次，重复使用）
     * viewHolder 被回收（回收不等于注销，是重复利用）时间，是在 item 不可见或者消失时
     * 如果数据只有一页，就不会回收，大于等于两页，回收重复使用
     * 当手机屏幕滑动到底部时，最上面的viewHolder已经被回收，可重复利用
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : SunMyViewHolder {
        val sunView = LayoutInflater.from(parent.context)
            .inflate(R.layout.sun_list_item, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return SunMyViewHolder(sunView)
    }

    /**
     * 重写方法二
     * 向 viewHolder 中填充 item 数据
     * 调用时间：屏幕上 item 出现或者即将出现的时候 负责每个子项（item）的数据赋值（绑定）
     * 当手机屏幕滑动到底部时，最上面的viewHolder已经被回收，可重复利用，只向 填充
     * 可给每一项添加点击事件侦听器
     */
    override fun onBindViewHolder(holder: SunMyViewHolder, position: Int) {
        val sunholder = sunMyDataList[position]
        //holder.imageView.setImageResource(sunholder.imageResuorce)  //赋值一个图片
        holder.textView1.text = sunholder.id.toString()
        holder.textView2.text = sunholder.firstName
        /**
         * 图标，点击事件侦听器
         */
//        holder?.imageView?.setOnClickListener {
//            println("jessice:"+sunholder.text1)
//        }
    }

    /**
     * 重写方法三
     * 获取item的条数
     */
    override fun getItemCount() = sunMyDataList.size
}
```

十、activity_main.xml

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/button1"
        app:layout_constraintVertical_bias="0.061" />

    <Button
        android:id="@+id/button1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Button"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:ignore="MissingConstraints" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/sun_list_item_Recyclerview_id"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:clipToPadding="false"
        android:padding="4dp"
        tools:listitem="@layout/sun_list_item"
        />
</LinearLayout>
```

十一、sun_list_item.xml

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="60dp"
    android:layout_margin="4dp">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="8dp">

<!--        <ImageView-->
<!--            android:id="@+id/image_view"-->
<!--            android:layout_width="50dp"-->
<!--            android:layout_height="50dp"-->
<!--            android:layout_marginEnd="8dp"-->
<!--            android:src="@mipmap/ic_launcher" />-->

        <TextView
            android:id="@+id/text_view_1"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_toEndOf="@+id/image_view"
            android:text="line 1"
            android:textColor="@android:color/black"
            android:textSize="18sp"
            android:textStyle="bold"
            tools:ignore="UnknownId" />


        <TextView
            android:id="@+id/text_view_2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_below="@id/text_view_1"
            android:layout_toEndOf="@id/image_view"
            android:text="line 2"
            tools:ignore="UnknownId" />

    </RelativeLayout>
</androidx.cardview.widget.CardView>
```

