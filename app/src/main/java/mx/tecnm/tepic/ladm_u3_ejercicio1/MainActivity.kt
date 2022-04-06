package mx.tecnm.tepic.ladm_u3_ejercicio1

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import mx.tecnm.tepic.ladm_u3_ejercicio1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    var listaIDs = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mostrar()
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
                    Toast.makeText(this,"Se insertó correctamente",Toast.LENGTH_LONG)
                        .show()
                    /*AlertDialog.Builder(this)
                        .setTitle("EXITO")
                        .setMessage("SE INSERTO CORRECTAMENTE")
                        .show()*/
                    mostrar()
                    binding.nom.setText("")
                    binding.dom.setText("")
                    binding.tel.setText("")
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
    fun mostrar(){
        val baseDatos = BaseDatos(this, "ejemplo1",null,1)
        var arreglo = ArrayList<String>()

        listaIDs.clear()
        try{
            val tablaPersona = baseDatos.readableDatabase
            var cursor  = tablaPersona.query("PERSONA", arrayOf("NOMBRE","DOMICILIO","TELEFONO"),null,null,null,null,null)
            /*
            Un objeto CURSOR es un VECTOR de resultados, similar a un ARRAYLIST <String> <Int>
            cursor es un vector de resultado ESTATICO, en cada ESPACIO de memoria contiene un registro
            del tipo de la tabla en este caso contendrá un registro tipo PERSONA (nombre (0), domicilio(1), telefono(2))
             */
            if (cursor.moveToFirst()){
                do{
                    arreglo.add(cursor.getString(0))
                    listaIDs.add(cursor.getInt(1).toString())
                }while (cursor.moveToNext())
            }else{
                arreglo.add("No hay resultados")
            }
        }catch (err:SQLiteException){
            AlertDialog.Builder(this)
                .setMessage(err.message)
                .show()
        }finally {
            baseDatos.close()
        }
        binding.lista.adapter=ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, arreglo)

        binding.lista.setOnItemClickListener { adapterView, view, i, l ->
            val idRecuperado = listaIDs.get(i)


            val datosPersona = mostrarUnaPersona(idRecuperado)
            AlertDialog.Builder(this)
                .setTitle("Información")
                .setMessage("DATOS COMPLETOS DEL CONTACTO: \n${datosPersona}")
                .setPositiveButton("Aceptar"){d, i->}
                .setPositiveButton("ELIMINAR"){d, i->confirmarEliminar(idRecuperado)}
                .setNegativeButton("ACTUALIZAR"){d,i->
                    var otraVentana = Intent(this,MainActivity2::class.java)

                    otraVentana.putExtra("idactualizar",idRecuperado)
                    startActivity(otraVentana)
                }
                .show()
        }
    }

    fun eliminar(idEliminar: String){
        val baseDatos = BaseDatos(this,"ejemplo1",null,1)
        try {
            val tablaPersona = baseDatos.writableDatabase
            var resultado = tablaPersona.delete("PERSONA","ID=?", arrayOf(idEliminar))
            if(resultado !=0){
                Toast.makeText(this,"SE ELIMINÓ CORRECTAMENTE",Toast.LENGTH_LONG)
                    .show()
                mostrar()
            }else{
                AlertDialog.Builder(this)
                    .setTitle("ATENCIÓN")
                    .setMessage("NO SE PUDO ELIMINAR EL ID ${idEliminar}")
                    .setPositiveButton("Aceptar"){d, i->}
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

    fun confirmarEliminar(idEliminar:String){
        AlertDialog.Builder(this)
            .setTitle("CONFIRMAR ELIMINACIÓN")
            .setMessage("¿ESTÁS SEGURO QUE\nDESEAS ELIMINAR A ${idEliminar}")
            .setPositiveButton("SI"){d,i->eliminar(idEliminar)}
            .setPositiveButton("NO"){d,i->}
            .show()
    }

    fun mostrarUnaPersona(idBuscado:String):String{
        val baseDatos = BaseDatos(this,"ejemplo1",null,1)
        var resultado =""
        try {
            val tablaPersona = baseDatos.readableDatabase

            var cursor = tablaPersona.query("PERSONA", arrayOf("*"),"ID=?", arrayOf(idBuscado),null,null,null)
            if(cursor.moveToFirst()){
                resultado = "ID: "+cursor.getInt(0).toString() + "\nNOMBRE: "+
                        cursor.getString(1).toString()+"\nDOMICILIO: "+cursor.getString(2)+
                        "\nTELEFONO: "+cursor.getString(3)
            }else{
                resultado = "NO SE ENCONTRÓ DATOS DE LA CONSULTA"
            }
        }catch (err:SQLiteException){
            AlertDialog.Builder(this)
                .setMessage(err.message)
                .show()
        }finally {
            baseDatos.close()
        }
        return resultado
    }

    override fun onRestart() {
        mostrar()
        super.onRestart()
    }
}
