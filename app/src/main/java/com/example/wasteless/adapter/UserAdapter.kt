package com.example.wasteless.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wasteless.R
import com.example.wasteless.model.UserModel

class UserAdapter(private val listUser: ArrayList<UserModel>): RecyclerView.Adapter<UserAdapter.UserHolder>() {
    interface OnItemClickCallback{
        fun onItemClicked(data: UserModel)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class UserHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txt_name)
        val txtPhone: TextView = itemView.findViewById(R.id.txt_phone)
        val txtAddress: TextView = itemView.findViewById(R.id.txt_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_user, parent, false)
        return UserHolder(view)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val item = listUser[position]

        holder.txtName.text = item.name
        holder.txtPhone.text = item.phone
        holder.txtAddress.text = item.address

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(item) }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}