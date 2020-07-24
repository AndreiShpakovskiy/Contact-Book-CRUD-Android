package com.shpakovskiy.contactbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shpakovskiy.contactbook.MainActivity
import com.shpakovskiy.contactbook.R
import com.shpakovskiy.contactbook.entity.Contact
import kotlinx.android.synthetic.main.contacts_list_item.view.*

class ContactItemsAdapter(private val context: Context, private val items: List<Contact>) :
    RecyclerView.Adapter<ContactItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.contacts_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

//        if (position % 2 == 0) {
//            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray))
//        } else {
//            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
//        }

        holder.nameField.text = item.name
        holder.phoneField.text = item.phoneNumber

        holder.editButton.setOnClickListener {
            (context as MainActivity).updateRecordDialog(items[position])
        }

        holder.removeButton.setOnClickListener {
            (context as MainActivity).removeContactAlertDialog(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameField: TextView = view.nameField
        val phoneField: TextView = view.phoneField
        val editButton: ImageView = view.editButton
        val removeButton: ImageView = view.removeButton
    }
}