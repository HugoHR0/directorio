package com.example.directorio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.directorio.db.UserEntity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.RowClickListener {

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerViewAdapter = RecyclerViewAdapter(this@MainActivity)
            adapter = recyclerViewAdapter
            val divider = DividerItemDecoration(applicationContext, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(divider)
        }

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.getAllUsersObservers().observe(this, Observer {
            recyclerViewAdapter.setListData(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        })


        saveButton.setOnClickListener {
            val nombre  = txtNombre.text.toString()
            val apellido  = txtApellido.text.toString()
            val telefono = txtTelfono.text.toString()
            if(saveButton.text.equals("Guardar")) {
                val user = UserEntity(0, nombre, apellido, telefono)
                viewModel.insertUserInfo(user)
            } else {
                val user = UserEntity(txtNombre.getTag(txtNombre.id).toString().toInt(), nombre, apellido, telefono)
                viewModel.updateUserInfo(user)
                saveButton.setText("Guardar")
            }
            txtNombre.setText("")
            txtApellido.setText("")
            txtTelfono.setText("")
        }
    }



    override fun onDeleteUserClickListener(user: UserEntity) {
        viewModel.deleteUserInfo(user)
    }

    override fun onItemClickListener(user: UserEntity) {
        txtNombre.setText(user.nombre)
        txtApellido.setText(user.email)
        txtTelfono.setText(user.phone)
        txtNombre.setTag(txtNombre.id, user.id)
        saveButton.setText("Actualizar")
    }
}