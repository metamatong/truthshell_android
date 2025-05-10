package com.example.truthshellapp.ui.results

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.truthshellapp.databinding.ItemSourceBinding

class SourcesAdapter(private val sources: List<String>) :
    RecyclerView.Adapter<SourcesAdapter.SourceViewHolder>() {

    inner class SourceViewHolder(private val binding: ItemSourceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sourceUrl: String) {
            binding.textViewSourceUrl.text = sourceUrl
            // The autoLink="web" property in XML handles making it clickable
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        val binding = ItemSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        holder.bind(sources[position])
    }

    override fun getItemCount(): Int = sources.size
}

