package com.example.truthshellapp.ui.saved

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.truthshellapp.R
import com.example.truthshellapp.data.local.SavedResultEntity
import java.util.Date

/**
 * Adapter for displaying saved TruthShell analysis results in a RecyclerView.
 */
class SavedResultsAdapter :
    ListAdapter<SavedResultEntity, SavedResultsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<SavedResultEntity>() {
        override fun areItemsTheSame(
            oldItem: SavedResultEntity,
            newItem: SavedResultEntity
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: SavedResultEntity,
            newItem: SavedResultEntity
        ): Boolean = oldItem == newItem
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val claimView: TextView = itemView.findViewById(R.id.textViewClaim)
        private val scoreView: TextView = itemView.findViewById(R.id.textViewScore)
        private val timestampView: TextView = itemView.findViewById(R.id.textViewTimestamp)

        fun bind(item: SavedResultEntity) {
            claimView.text = item.originalClaim
            scoreView.text = "%d%% %s".format(item.confidenceScore, item.confidenceLabel)
            val date = Date(item.timestamp)
            timestampView.text = DateFormat.format("yyyy-MM-dd HH:mm", date)
        }
    }
}