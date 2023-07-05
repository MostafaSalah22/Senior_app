package com.project.senior.medicines.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.domain.model.MedicineData
import com.project.senior.R
import com.project.senior.databinding.BookingMedicineItemBinding

class MedicineAdapter(private val context: Context) :
    ListAdapter<MedicineData, MedicineAdapter.MedicineViewHolder>(MedicineDiffCallback()) {

    var onDeleteClick: ((MedicineData) -> Unit)? = null
    var onEditClick: ((MedicineData) -> Unit)? = null

    class MedicineViewHolder(private val binding: BookingMedicineItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            val name: TextView = binding.tvNameMedicine
            val dose: TextView = binding.tvDoseMedicine
            val description:TextView = binding.tvDescriptionMedicine
            val deleteMedicine:ImageView = binding.imgDeleteMedicine
            val editMedicine:ImageView = binding.imgEditMedicine


    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MedicineViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = BookingMedicineItemBinding.inflate(inflater, parent, false)
        return MedicineViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: MedicineAdapter.MedicineViewHolder, position: Int) {
        val item = getItem(position)

        holder.name.text = item.medication
        //holder.description.text = item.description

        if(item.description == null){
            holder.description.text = context.getString(R.string.no_description)
        }
        else holder.description.text = item.description

        holder.dose.text = "${item.medication_dose} times a day"

        holder.deleteMedicine.setOnClickListener {
            onDeleteClick?.invoke(item)
        }

        holder.editMedicine.setOnClickListener {
            onEditClick?.invoke(item)
        }
    }

}

class MedicineDiffCallback() : DiffUtil.ItemCallback<MedicineData>() {
    override fun areItemsTheSame(
        oldItem: MedicineData,
        newItem: MedicineData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: MedicineData,
        newItem: MedicineData
    ): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}