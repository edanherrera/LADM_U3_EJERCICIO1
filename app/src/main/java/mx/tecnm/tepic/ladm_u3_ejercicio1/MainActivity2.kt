package mx.tecnm.tepic.ladm_u3_ejercicio1

import android.content.ContentValues
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import mx.tecnm.tepic.ladm_u3_ejercicio1.databinding.ActivityMain2Binding
import mx.tecnm.tepic.ladm_u3_ejercicio1.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {
    lateinit var binding : ActivityMain2Binding

    var idActualizar = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val extra = intent.extras!!
        idActualizar = extra.getString("idactualizar")!!

        cargarDatos()
        binding.actualizar.setOnClickListener {
            val baseDatos = BaseDatos(this,"ejemplo1",null,1)
            try {
                val tablaPersona = baseDatos.writableDatabase
                val datos = ContentValues()

                datos.put("NOMBRE",binding.nom.text.toString())
                datos.put("DOMICILIO",binding.dom.text.toString())
                datos.put("TELEFONO",binding.tel.text.toString())

                var resultado = tablaPersona.update("PERSONA", datos,"ID=?", arrayOf(idActualizar))
                if(resultado!=0){
                    Toast.makeText(this,"SE ACTUALIZÓ CON EXITO!",Toast.LENGTH_LONG)
                        .show()
                }else{
                    Toast.makeText(this,"ERROR! no se pudo actualizar",Toast.LENGTH_LONG)
                        .show()
                }
            }catch (err:SQLiteException){
                AlertDialog.Builder(this)
                    .setMessage(err.message)
                    .show()
            }finally {
                baseDatos.close()
            }
        }
        binding.regresar.setOnClickListener {
            finish()
        }
    }

    fun cargarDatos(){
        val baseDatos = BaseDatos(this,"ejemplo1",null,1)
        var resultado =""
        try {
            val tablaPersona = baseDatos.readableDatabase

            var cursor = tablaPersona.query("PERSONA", arrayOf("*"),"ID=?", arrayOf(idActualizar),null,null,null)
            if(cursor.moveToFirst()){
                binding.nom.setText(cursor.getString(1).toString())
                binding.dom.setText(cursor.getString(2).toString())
                binding.tel.setText(cursor.getString(3).toString())

            }else{
                AlertDialog.Builder(this)
                    .setMessage("ERROR NO SE ENCONTRÓ DATOS DEL ID ACTUALIZAR")
                    .show()
            }
        }catch (err: SQLiteException){
            AlertDialog.Builder(this)
                .setMessage(err.message)
                .show()
        }finally {
            baseDatos.close()
        }
    }
}