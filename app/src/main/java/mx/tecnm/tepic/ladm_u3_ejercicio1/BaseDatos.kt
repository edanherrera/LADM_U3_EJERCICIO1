package mx.tecnm.tepic.ladm_u3_ejercicio1

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        /*
        se ejecuta en cuanto se instala la aplicación y se abre x 1ra vez
        dentro del terminal del cliente.
        Se encarga de construir la estructura de la base de datos (tablas
        y las relaciones).
        */
        db.execSQL("CREATE TABLE PERSONA( ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR(200), DOMICILIO VARCHAR(200), TELEFONO VARCHAR(50) )")

    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        /*
        se ejecuta cuando hay un cambio de la versión
        y realiza una modficación a la estructura de la BASE DATOS
        (por ejemplo, agregar o quitar columnas. Agreagr o quitar Tablas)
         */
    }
}