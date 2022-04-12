package com.sachin.locationapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var edlocation:EditText
    private lateinit var edlongitude:EditText
    private lateinit var edlatitude:EditText

    private lateinit var btnAdd:Button
    private lateinit var btnView:Button

    private lateinit var SQLSetter: SQLSetter
    private lateinit var recyclerView: RecyclerView
    private var adapter : LocationAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecycleView()
        SQLSetter= SQLSetter(this)
        btnAdd.setOnClickListener { addLocation() }
        btnView.setOnClickListener { getLocations() }

        adapter?.setOnClickItem {

            val intent = Intent(this, MapsFragment::class.java)
            //Open Maps Fragment
            intent.putExtra("name",it.loc)
            intent.putExtra("lat",it.latitude)
            intent.putExtra("lng",it.longitude)

            startActivity(intent)

            Toast.makeText(this, it.loc, Toast.LENGTH_SHORT).show()
            Toast.makeText(this, it.latitude, Toast.LENGTH_SHORT).show()
            Toast.makeText(this, it.longitude, Toast.LENGTH_SHORT).show()




        }


        adapter?.setOnClickDeleteItem {
            deleteLocation(it.id)
        }
    }



    private fun getLocations() {
        val locList=SQLSetter.getAllLocations()
        Log.e("pppp", "${locList.size}")

        adapter?.addItems(locList)
    }

    private fun addLocation()
    {
        val loc = edlocation.text.toString()
        val longitude = edlongitude.text.toString()
        val latitude = edlatitude.text.toString()

        if(loc.isEmpty()||longitude.isEmpty()||latitude.isEmpty())
        {

            Toast.makeText(this,"Please enter fields",Toast.LENGTH_SHORT).show()
        }else
        {

            val loc=LocationModel(loc =loc,longitude = longitude,latitude = latitude)
            val status=SQLSetter.insertLocation(loc)

            if(status >-1)
            {
                Toast.makeText(this,"Location Added....",Toast.LENGTH_SHORT).show()
                clearEditText()
            }
            else
            {
                Toast.makeText(this,"Not Added....",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteLocation(id:Int)
    {

        val builder =AlertDialog.Builder(this)
        builder.setMessage("Are you sure to DELETE ?")
        builder.setCancelable(true)
        builder.setPositiveButton("YES")
        {dialog,_->
            SQLSetter.deleteLocationById(id)
            getLocations()

            dialog.dismiss()

        }
        builder.setNegativeButton("No")
        {
                dialog,_->
            dialog.dismiss()

        }

        val alert = builder.create()
        alert.show()
    }

    private fun clearEditText()
    {
        edlocation.setText("")
        edlongitude.setText("")
        edlatitude.setText("")

        edlocation.requestFocus()
    }

    private fun initRecycleView()
    {

        recyclerView.layoutManager= LinearLayoutManager(this)
        adapter = LocationAdapter()
        recyclerView.adapter = adapter
    }
    private fun initView()
    {
        edlocation=findViewById(R.id.edlocation)
        edlongitude=findViewById(R.id.edlongitude)
        edlatitude=findViewById(R.id.edlatitude)

        btnAdd=findViewById(R.id.btnAdd)
        btnView=findViewById(R.id.btnView)
        recyclerView=findViewById(R.id.recyclerView)
    }
}