package com.example.sun13.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sun13.R
import com.example.sun13.data.room.SunUser
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