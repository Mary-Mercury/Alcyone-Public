package com.mercury.alcyone.Presentation.recyclerview_2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mercury.alcyone.Data.TableTestDto
import com.example.alcyone.R

class SecondSubRecyclerView2(var list: List<TableTestDto>): RecyclerView.Adapter<SecondSubRecyclerView2.SecondViewHolder2>() {

    class SecondViewHolder2 (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvAud: TextView = itemView.findViewById(R.id.tvAud)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondViewHolder2 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        return SecondViewHolder2(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SecondViewHolder2, position: Int) {
        val item = list[position]
        holder.tvName.text = item.SubName
        holder.tvAud.text = item.AudName
        holder.tvTime.text = item.time
    }

    fun updateData(newData: List<TableTestDto>) {
        list = newData.takeLast(6)
        notifyDataSetChanged()
    }
}