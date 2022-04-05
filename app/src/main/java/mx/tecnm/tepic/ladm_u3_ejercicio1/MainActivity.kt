package mx.tecnm.tepic.ladm_u3_ejercicio1

import android.content.ContentValues
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import mx.tecnm.tepic.ladm_u3_ejercicio1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.insertar.setOnClickListener{
            val baseDatos = BaseDatos(this,"ejemplo1",null,1)
            try {
                var datos = ContentValues()
                val tablaPersona = baseDatos.writableDatabase

                datos.put("NOMBRE",binding.nom.text.toString())
                datos.put("DOMICILIO",binding.dom.text.toString())
                datos.put("TELEFONO",binding.tel.text.toString())

                val resultado = tablaPersona.insert("PERSONA,","ID",datos)
                if(resultado == -1L){
                    AlertDialog.Builder(this)
                        .setTitle("ERROR")
                        .setMessage("NO SE PUDO INSERTAR")
                        .show()
                }else{
                    AlertDialog.Builder(this)
                        .setTitle("EXITO")
                        .setMessage("SE INSERTO CORRECTAMENTE")
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
    }
}
