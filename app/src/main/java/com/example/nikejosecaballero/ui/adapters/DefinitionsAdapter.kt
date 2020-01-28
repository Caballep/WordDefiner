package com.example.nikejosecaballero.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nikejosecaballero.R
import com.example.nikejosecaballero.network.UrbanDictionary.Definition
import kotlinx.android.synthetic.main.item_definitions_adapter.view.*

class DefinitionsAdapter(private val definitions: List<Definition>):
    RecyclerView.Adapter<DefinitionsAdapter.DefinitionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DefinitionsViewHolder {
        val viewHolderItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_definitions_adapter, parent, false)
        return DefinitionsViewHolder(viewHolderItem)
    }

    override fun getItemCount(): Int {
        return definitions.size
    }

    override fun onBindViewHolder(holder: DefinitionsViewHolder, position: Int) {
        holder.itemView.authorTextView.text = definitions[position].author
        holder.itemView.definitionTextView.text = definitions[position].definition
        holder.itemView.thumbsUpTextView.text = definitions[position].thumbsUp.toString()
        holder.itemView.thumbsDownTextView.text = definitions[position].thumbsDown.toString()
        holder.itemView.definitionTextView.paintText()
    }

    class DefinitionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}