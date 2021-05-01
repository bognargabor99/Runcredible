package hu.bme.aut.android.runcredible.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.runcredible.database.RunEntity
import hu.bme.aut.android.runcredible.databinding.ItemRunBinding

class RunAdapter : ListAdapter<RunEntity, RunAdapter.ViewHolder>(RunItemCallback) {

    var itemClickListener: RunItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ItemRunBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val run = getItem(position)
        holder.run = run

        holder.binding.tvDate.text = run.time
    }

    companion object{
        object RunItemCallback : DiffUtil.ItemCallback<RunEntity>(){
            override fun areItemsTheSame(oldItem: RunEntity, newItem: RunEntity): Boolean {
                return oldItem.Id == newItem.Id
            }

            override fun areContentsTheSame(oldItem: RunEntity, newItem: RunEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(val binding: ItemRunBinding) : RecyclerView.ViewHolder(binding.root) {
        var run: RunEntity? = null

        init {
            binding.btnRemove.setOnClickListener {
                Log.d("runAdapter", "RemoveButton clicked for Run Id: ${run?.Id}")
                run?.let { itemClickListener?.onDelete(it) }
            }
            itemView.setOnClickListener {
                run?.let { itemClickListener?.onItemClick(it) }
            }
        }
    }

    interface RunItemClickListener {
        fun onItemClick(run: RunEntity)
        fun onDelete(run: RunEntity)
    }
}