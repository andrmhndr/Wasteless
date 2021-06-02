package com.example.wasteless.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wasteless.R
import com.example.wasteless.model.OrderModel
import com.example.wasteless.utils.Helper
import java.text.SimpleDateFormat

class OrderAdapter(private var listOrderModel: ArrayList<OrderModel>, private val role: String): RecyclerView.Adapter<OrderAdapter.OrderHolder>() {
    interface OnItemClickCallback{
        fun onItemClicked(data: OrderModel)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class OrderHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txt_title)
        val txtDate: TextView = itemView.findViewById(R.id.txt_date)
        val txtAddress: TextView = itemView.findViewById(R.id.txt_address)
        val txtInfo: TextView = itemView.findViewById(R.id.txt_info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_order, parent, false)
        return OrderHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val item = listOrderModel[position]

        if (role == Helper.USER){
            holder.txtTitle.text = item.userName
            holder.txtAddress.text = item.userAddress
        }else if(role == Helper.STAKEHOLDER){
            holder.txtTitle.text = item.stakeholderName
            holder.txtAddress.text = item.stakeholderAddress
        }

        holder.txtDate.text = Helper.dateToString(item.date)
        holder.txtInfo.text = item.info

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(item) }
    }

    override fun getItemCount(): Int {
        return listOrderModel.size
    }
}