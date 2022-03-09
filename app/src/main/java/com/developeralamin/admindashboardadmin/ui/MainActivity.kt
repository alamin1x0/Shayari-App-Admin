package com.developeralamin.admindashboardadmin.ui

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.developeralamin.admindashboardadmin.R
import com.developeralamin.admindashboardadmin.databinding.ActivityMainBinding
import com.developeralamin.admindashboardadmin.databinding.DialogAddCatBinding
import com.developeralamin.loveuserapp.adapter.CategoryAdapter
import com.developeralamin.loveuserapp.model.CategoryModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        db = FirebaseFirestore.getInstance()


        val settings = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        db.firestoreSettings = settings


        binding.btnAddCat.setOnClickListener {

            val addCatDialog = Dialog(this)
            val binding = DialogAddCatBinding.inflate(layoutInflater)
            addCatDialog.setContentView(binding.root)

            if (addCatDialog.window != null) {
                addCatDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }


            binding.dialogaddCatBtn.setOnClickListener {
                val name = binding.dialogeditCatName.text.toString()
                val id = db.collection("Love").document().id
                val data = CategoryModel(id, name)
                db.collection("Love").document(id).set(data).addOnCompleteListener {
                    Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show()
                    addCatDialog.dismiss()
                }.addOnCanceledListener {
                    Toast.makeText(this, "" + it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            addCatDialog.show()
        }

        db.collection("Love").addSnapshotListener { value, error ->

            val list = arrayListOf<CategoryModel>()
            val data = value?.toObjects(CategoryModel::class.java)
            list.addAll(data!!)

            binding.revCategroy.layoutManager = LinearLayoutManager(this)
            binding.revCategroy.adapter = CategoryAdapter(this, list)

        }
    }
}