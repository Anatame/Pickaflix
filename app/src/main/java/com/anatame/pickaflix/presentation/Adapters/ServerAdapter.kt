package com.anatame.pickaflix.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.databinding.ItemRvServerBinding
import com.anatame.pickaflix.databinding.SearchItemMovie2Binding

class ServerAdapter(
    val serverList: List<String>
)  : RecyclerView.Adapter<ServerAdapter.ServerViewHolder>() {
    inner class ServerViewHolder(val binding: ItemRvServerBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerViewHolder {
        val binding = ItemRvServerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ServerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServerViewHolder, position: Int) {
        holder.binding.apply {
            serverBtn.text = serverList[position]
        }
    }

    override fun getItemCount(): Int {
        return serverList.size
    }

}
