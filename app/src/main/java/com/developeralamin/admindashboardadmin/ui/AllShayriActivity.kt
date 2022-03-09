package com.developeralamin.admindashboardadmin.ui

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.developeralamin.admindashboardadmin.adapter.AllShayriAdapter
import com.developeralamin.admindashboardadmin.databinding.ActivityAllShayriBinding
import com.developeralamin.admindashboardadmin.databinding.DialogAddCatBinding
import com.developeralamin.admindashboardadmin.databinding.DialogAddShayraiBinding
import com.developeralamin.loveuserapp.model.CategoryModel
import com.developeralamin.loveuserapp.model.ShayriModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class AllShayriActivity : AppCompatActivity() {

    lateinit var binding: ActivityAllShayriBinding
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllShayriBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        db = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        db.firestoreSettings = settings


        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")

        binding.toolbarTitile.text = name.toString()


        db.collection("Love").document(id!!).collection("all")
            .addSnapshotListener { value, error ->

                val shayriList = arrayListOf<ShayriModel>()
                val data = value?.toObjects(ShayriModel::class.java)
                shayriList.addAll(data!!)

                binding.rcvAllShayari.layoutManager = LinearLayoutManager(this)
                binding.rcvAllShayari.adapter = AllShayriAdapter(this, shayriList, id)

            }

        binding.btnAddShayari.setOnClickListener {

            val addCatDialog = Dialog(this)
            val binding = DialogAddShayraiBinding.inflate(layoutInflater)
            addCatDialog.setContentView(binding.root)

            if (addCatDialog.window != null) {
                addCatDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }

            binding.dialogAddShayraiBtn.setOnClickListener {

                val uid = db.collection("Love").document().id
                val edtShayriget = binding.dialogeditShayrai.text.toString()

                val finalValue = ShayriModel(uid, edtShayriget)

                db.collection("Love")
                    .document(id).collection("all")
                    .document(uid).set(finalValue).addOnCompleteListener {
                        if (it.isSuccessful) {
                            addCatDialog.dismiss()
                            Toast.makeText(this, "Shayari Added Succesfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                        Toast.makeText(this,
                            "" + it.exception?.localizedMessage,
                            Toast.LENGTH_SHORT).show()
                    }

            }
            addCatDialog.show()
        }
    }
}