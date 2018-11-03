package org.wit.archaeologicalfieldwork.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_image.view.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.helpers.readImageFromPath

class ImageAdapter constructor(private var images: List<String>,
                                  private val listener: ImageListener) : RecyclerView.Adapter<ImageAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_image, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val image = images[holder.adapterPosition]
        holder.bind(image, listener)
    }

    override fun getItemCount(): Int = images.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(image: String,  listener : ImageListener) {
            itemView.image.setImageBitmap(readImageFromPath(itemView.context, image))
            itemView.setOnClickListener { listener.onImageClick(image) }
        }
    }
}

interface ImageListener {
    fun onImageClick(image: String)
}