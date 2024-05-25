package com.example.crudfirebase1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyListData : AppCompatActivity(), RecyclerViewAdapter.dataListener {
    // deklarasi variable untuk recyclerView
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    //Deklarasi Variable Database Reference & ArrayList dengan Parameter Class Model kita.
    val database = FirebaseDatabase.getInstance()
    private var dataMahasiswa = ArrayList<data_mahasiswa>()
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list_data)
        recyclerView = findViewById(R.id.datalist)
        supportActionBar!!.title = "Data Mahasiswa"
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()
    }

    private fun GetData() {
        Toast.makeText(applicationContext, "Mohon tunggu sebentar...", Toast.LENGTH_SHORT).show()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        getReference.child("Admin").child(getUserID).child("Mahasiswa")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        dataMahasiswa.clear()
                        for (snapshot in dataSnapshot.children) {
                            // mapping data pada DataSnapshot ke dalam objek mahasiswa
                            val mahasiswa = snapshot.getValue(data_mahasiswa::class.java)
                            // mengambil primary key, digunakan untuk proses update/delete
                            mahasiswa?.key = snapshot.key
                            dataMahasiswa.add(mahasiswa!!)
                        }
                        // inisialisasi adapter dan data mahasiswa dalam bentuk array
                        adapter = RecyclerViewAdapter(dataMahasiswa, this@MyListData)
                        // memasang adapter pada RecyclerView
                        recyclerView?.adapter = adapter
                        (adapter as RecyclerViewAdapter).notifyDataSetChanged()
                        Toast.makeText(applicationContext, "data berhasil dimuat", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseerror: DatabaseError) {
                    // kode ini akan dijalankan ketika ada error, simpan ke LogCat
                    Toast.makeText(applicationContext, "data gagal dimuat", Toast.LENGTH_SHORT).show()
                    Log.e("MyListActivity", databaseerror.details + " " + databaseerror.message)
                }
            })
    }
    // methode yang berisi kumpulan baris kode untuk mengatur RecyclerView
    private fun MyRecyclerView() {
        // menggunakan Layout Manager, dan membuat list secara vertical
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)

        // membuat underline pada setiap item di dalam list
        val itemDecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.line)!!)
        recyclerView?.addItemDecoration(itemDecoration)
    }

    override fun onDeleteData(data: data_mahasiswa?, position: Int) {
        /* kode ini akan dipanggil ketika method onDeleteData dipanggil dari adapter pada RecyclerView
        * melalui interface. kemudian akan menghapus data berdasarkan primary key dari data tersebut
        * Jika berhasil, maka akan memunculkan Toast */
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        if(getReference !=null) {
            getReference.child("Admin")
                .child(getUserID)
                .child("Mahasiswa")
                .child(data?.key.toString())
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@MyListData, "data berhasil dihapus", Toast.LENGTH_SHORT).show()
                }
        }
    }
}