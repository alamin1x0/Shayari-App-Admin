package com.developeralamin.admindashboardadmin.adapter


import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.developeralamin.admindashboardadmin.databinding.ItemAllShayriBinding
import com.developeralamin.admindashboardadmin.ui.AllShayriActivity
import com.developeralamin.loveuserapp.model.ShayriModel
import com.google.firebase.firestore.FirebaseFirestore


class AllShayriAdapter(
    val allShayriActivity: AllShayriActivity,
    val shayriList: ArrayList<ShayriModel>,
    val catid: String,
) : RecyclerView.Adapter<AllShayriAdapter.ShayriViewHolder>() {

    class ShayriViewHolder(val binding: ItemAllShayriBinding) :
        RecyclerView.ViewHolder(binding.root)

    val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShayriViewHolder {

        return ShayriViewHolder(ItemAllShayriBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ShayriViewHolder, position: Int) {


        holder.binding.itemShayri.text = shayriList[position].data.toString()


        holder.binding.btnDelete.setOnClickListener {

            val dialog = AlertDialog.Builder(allShayriActivity)
            dialog.setMessage("Are you sure want to delete?")
            dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                db.collection("Love").document(catid).collection("all")
                    .document(shayriList[position].id!!).delete().addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(allShayriActivity,
                                "Shayrai Deleted Successfully",
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(allShayriActivity,
                                it.exception?.localizedMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                dialog.dismiss()
            })
            dialog.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            dialog.show()
        }

    }

    override fun getItemCount() = shayriList.size

}