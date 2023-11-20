package com.example.pokedexapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexapp.databinding.ActivityMainBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var p_recyclerView: RecyclerView
    private lateinit var p_mainAdapter: MainAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var addRec = binding.floatAbtnId
        p_recyclerView= findViewById(R.id.rv_id)

        p_recyclerView.layoutManager = LinearLayoutManager(this)
        val options: FirebaseRecyclerOptions<MainModel> = FirebaseRecyclerOptions.Builder<MainModel>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("values"), MainModel::class.java)
            .build()

        p_mainAdapter = MainAdapter(options)
        p_recyclerView.adapter = p_mainAdapter


        addRec.setOnClickListener {
            Toast.makeText(applicationContext, "Add a Pokemon", Toast.LENGTH_SHORT).show()
            val intentAddNewRecord = Intent(applicationContext, AddNewRecord::class.java)
            startActivity(intentAddNewRecord)
        }
    }
    override fun onStart() {
        super.onStart()
        p_mainAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        p_mainAdapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val item: MenuItem = menu!!.findItem(R.id.search)
        val searchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                txtSearch(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                txtSearch(query)
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun txtSearch(str:String?){
        val options: FirebaseRecyclerOptions<MainModel> = FirebaseRecyclerOptions.Builder<MainModel>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("values").orderByChild("B_name").startAt(str).endAt("$str~"), MainModel::class.java)
            .build()

        p_mainAdapter = MainAdapter(options)
        p_mainAdapter.startListening()
        p_recyclerView.adapter = p_mainAdapter
    }
}