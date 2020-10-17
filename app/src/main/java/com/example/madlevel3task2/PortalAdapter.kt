package com.example.madlevel3task2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel3task2.databinding.ItemPortalBinding

class PortalAdapter(
    private val portals: List<Portal>,
    private val clickListener: (Portal) -> Unit
) :
    RecyclerView.Adapter<PortalAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemPortalBinding.bind(itemView)

        fun dataBind(portal: Portal, clickListener: (Portal) -> Unit) {
            binding.tvPortalName.text = portal.portalTitle
            binding.tvPortalUrl.text = portal.portalURL
            binding.cardView.setOnClickListener { clickListener(portal) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_portal, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(portals[position], clickListener = clickListener)
    }

    override fun getItemCount(): Int {
        return portals.size
    }
}