package com.kylix.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kylix.core.databinding.ItemUserListBinding
import com.kylix.core.domain.model.User

class UserAdapter(private val user: ArrayList<User>, private val clickListener: (String, View) -> Unit):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    fun setData(items: List<User>?) {
        user.apply {
            clear()
            items?.let { addAll(it) }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) = holder.bind(user[position], clickListener)

    override fun getItemCount(): Int = user.size

    inner class UserViewHolder(private val binding: ItemUserListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, click: (String, View) -> Unit) {
            binding.data = user
            binding.root.transitionName = user.login
            binding.root.setOnClickListener { user.login?.let { it1 -> click(it1, binding.root) } }
        }
    }
}