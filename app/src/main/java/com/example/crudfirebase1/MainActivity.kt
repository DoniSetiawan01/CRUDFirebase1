package com.example.crudfirebase1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.crudfirebase1.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var auth: FirebaseAuth? = null
    private var RC_SIGN_IN = 1
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        binding.logout.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        binding.showdata.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(p0: View?) {
        when (p0?.getId()) {
            R.id.save -> {
                //untuk fungsi simpan
                // statement program untuk simpan data
                // mendapatkan userID dari pengguna yang terautentifikasi
                val getUserID = auth!!.currentUser!!.uid

                // mendapatkan instance dari database
                val database = FirebaseDatabase.getInstance()

                // menyimpan data yang diinputkan ke dalam variable
                val getNIM: String = binding.nim.getText().toString()
                val getNama: String = binding.nama.getText().toString()
                val getJurusan: String = binding.jurusan.getText().toString()

                // mendapatkan referensi dari database
                val getReference: DatabaseReference
                getReference = database.reference

                // mengecek apakah ada data yang kosong
                if (isEmpty(getNIM) || isEmpty(getNama) || isEmpty(getJurusan)) {
                    // jika ada, maka akan menampilkan pesan berikut ini
                    Toast.makeText(this@MainActivity, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
                } else {
                    // jika tidak, maka data dapat diproses dan disimpan dalam database
                    getReference.child("Admin").child(getUserID).child("Mahasiswa").push()
                        .setValue(data_mahasiswa(getNIM, getNama, getJurusan))
                        .addOnCompleteListener(this) {// peristiwa ini terjadi saat user berhasil menyimpan data
                            binding.nim.setText("")
                            binding.nama.setText("")
                            binding.jurusan.setText("")
                            Toast.makeText(this@MainActivity, "Data Disimpan", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            R.id.logout -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(p0: Task<Void>) {
                            Toast.makeText(this@MainActivity, "Logout Berhasil", Toast.LENGTH_SHORT).show()
                            intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
            }
            R.id.showdata -> {
                //untuk fungsi tampil data
                startActivity(Intent(this@MainActivity, MyListData::class.java))
            }
        }
    }
}