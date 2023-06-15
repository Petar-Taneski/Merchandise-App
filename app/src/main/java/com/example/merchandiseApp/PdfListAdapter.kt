package com.example.merchandiseApp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.merchandiseApp.databinding.ItemPdfBinding

class PdfListAdapter (
    private val pdfFiles: List<String>,
    private var onItemClick: (String) -> Unit
): RecyclerView.Adapter<PdfListAdapter.PdfViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPdfBinding.inflate(inflater, parent, false)
        return PdfViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val pdfFile = pdfFiles[position]
        holder.bind(pdfFile)
    }

    override fun getItemCount(): Int {
        return pdfFiles.size
    }

    inner class PdfViewHolder(private val binding: ItemPdfBinding):
        RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnClickListener{
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    val pdfFile = pdfFiles[position]
                    onItemClick(pdfFile)
                }
            }
        }
        fun bind(pdfFile: String){
            binding.tvPdfName.text = pdfFile
    }
}



}
