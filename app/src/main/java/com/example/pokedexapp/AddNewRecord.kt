package com.example.pokedexapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.pokedexapp.databinding.ActivityAddNewRecordBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNewRecord : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewRecordBinding
    private lateinit var p_Name: EditText
    private lateinit var p_Gen: EditText
    private lateinit var p_Type: EditText
    private lateinit var p_ImgUrl: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        p_Name = binding.txtIdName
        p_Gen = binding.txtIdGen
        p_Type = binding.txtIdType
        p_ImgUrl = binding.txtIdImgurl

        var p_Save = binding.btnIdSave
        var p_Cancel = binding.btnIdCancel

        p_Save.setOnClickListener {
            val name_is = p_Name.text.toString()
            val number_is = p_Gen.text.toString()
            val power_is = p_Type.text.toString()
            val image_is = p_ImgUrl.text.toString()

            when {
                name_is.isEmpty() -> f_ValidateMsg("NAME")
                number_is.isEmpty() -> f_ValidateMsg("GEN")
                power_is.isEmpty() -> f_ValidateMsg("TYPE")
                image_is.isEmpty() -> f_ValidateMsg("IMAGE")
                else -> f_InsertRecord(name_is, number_is, power_is, image_is)
            }
        }
        p_Cancel.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun f_InsertRecord(name_is: String, number_is: String, power_is: String, image_is: String) {
        val dataHashMap = HashMap<String, Any>()
        dataHashMap["B_name"] = name_is
        dataHashMap["C_gen"] = number_is
        dataHashMap["D_type"] = power_is
        dataHashMap["E_imageurl"] = image_is

        val database = FirebaseDatabase.getInstance()
        val tblReference: DatabaseReference = database.getReference("values")

        val idKey = tblReference.push().key
        dataHashMap["A_idno"] = idKey!!

        tblReference.child(idKey).setValue(dataHashMap).addOnCompleteListener {
            Toast.makeText(applicationContext, "Pokemon Data Added SUCCESSFULLY!", Toast.LENGTH_SHORT).show()
            p_Name.text.clear()
            p_Gen.text.clear()
            p_Type.text.clear()
            p_ImgUrl.text.clear()
        }

    }
    private fun f_ValidateMsg(info: String) {
        Toast.makeText(applicationContext, "Please Enter a Pokemon $info", Toast.LENGTH_SHORT)
            .show()
    }
}