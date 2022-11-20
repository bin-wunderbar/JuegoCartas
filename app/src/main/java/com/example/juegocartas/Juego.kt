package com.example.juegocartas


import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import java.util.*

class Juego : Activity() {
    //______________________________________________________________________________________________
    // variables para los componentes de la vista
    private lateinit var imb00: ImageButton
    private lateinit var imb01: ImageButton
    private lateinit var imb02: ImageButton
    private lateinit var imb03: ImageButton
    private lateinit var imb04: ImageButton
    private lateinit var imb08: ImageButton
    private lateinit var imb09: ImageButton
    private lateinit var imb10: ImageButton
    private lateinit var imb11: ImageButton
    private lateinit var imb12: ImageButton
    private var tableroRespuestas = arrayOfNulls<ImageButton>(5)
    private var tableroPreguntas = arrayOfNulls<ImageButton>(5)
    private lateinit var botonReiniciar: Button

    //______________________________________________________________________________________________
    //imagenes
    private lateinit var preguntas: IntArray
    private lateinit var respuestas: IntArray
    var fondo = 0

    //______________________________________________________________________________________________
    //variables del juego
    private lateinit var arrayDesordenadoRespuestas: ArrayList<Int>
    private lateinit var arrayDesordenadoPreguntas: ArrayList<Int>
    private var primero: ImageButton? = null
    private var numeroPrimero = 0
    private var numeroSegundo = 0
    private var bloqueo = false
    private var aciertos = 0
    private val handler = Handler()

    //______________________________________________________________________________________________
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)
        init()
    }

    //______________________________________________________________________________________________
    private fun cargarTablero() {
        imb00 = findViewById(R.id.boton00)
        imb01 = findViewById(R.id.boton01)
        imb02 = findViewById(R.id.boton02)
        imb03 = findViewById(R.id.boton03)
        imb04 = findViewById(R.id.boton04)

        imb08 = findViewById(R.id.boton08)
        imb09 = findViewById(R.id.boton09)
        imb10 = findViewById(R.id.boton10)
        imb11 = findViewById(R.id.boton11)
        imb12 = findViewById(R.id.boton12)

        tableroPreguntas[0] = imb00
        tableroPreguntas[1] = imb01
        tableroPreguntas[2] = imb02
        tableroPreguntas[3] = imb03
        tableroPreguntas[4] = imb04

        tableroRespuestas[0] = imb08
        tableroRespuestas[1] = imb09
        tableroRespuestas[2] = imb10
        tableroRespuestas[3] = imb11
        tableroRespuestas[4] = imb12
    }

    //______________________________________________________________________________________________
    private fun cargarBotones() {
        botonReiniciar = findViewById(R.id.botonJuegoReiniciar)
        botonReiniciar.setOnClickListener { init() }
        aciertos = 0
    }

    //______________________________________________________________________________________________
    private fun cargarImagenes() {
        preguntas = intArrayOf(
            R.drawable.pre0,
            R.drawable.pre1,
            R.drawable.pre2,
            R.drawable.pre3,
            R.drawable.pre4,
        )
        respuestas = intArrayOf(
            R.drawable.res0,
            R.drawable.res1,
            R.drawable.res2,
            R.drawable.res3,
            R.drawable.res4,
        )
        fondo = R.drawable.fondo_preguntas
    }

    //______________________________________________________________________________________________
    /* devuelve array desordenado  requiere de parametro la longitud del array */
    private fun desordenarArray(longitud: Int): ArrayList<Int> {
        val result = ArrayList<Int>()
        for (i in 0 until longitud) {
            result.add(i % longitud)
        }
        result.shuffle()
        return result
    }

    //______________________________________________________________________________________________
    private fun comprobar(i: Int, pregunta: Boolean, imgb: ImageButton?) {
        if (primero == null)
        {
            println("asi queda la orden del los arrays")
            println(Arrays.toString(arrayDesordenadoPreguntas.toArray()))
            println(Arrays.toString(arrayDesordenadoRespuestas.toArray()))
            if (pregunta) 
            {
                primero = imgb
                primero!!.scaleType = ImageView.ScaleType.CENTER_INSIDE
                primero!!.setImageResource(preguntas[arrayDesordenadoPreguntas[i]])
                primero!!.isEnabled = false
                numeroPrimero = arrayDesordenadoPreguntas[i]

            } else
            {
                primero = imgb
                primero!!.scaleType = ImageView.ScaleType.CENTER
                primero!!.setImageResource(respuestas[arrayDesordenadoRespuestas[i]])
                primero!!.isEnabled = false
                numeroPrimero = arrayDesordenadoRespuestas[i]
            }

        } else
        {
            when (pregunta)
            {
                true -> setPregunta(i, imgb)
                else -> setRespuesta(i, imgb)
            }

            if (numeroPrimero == numeroSegundo) 
            {
                primero = null
                bloqueo = false
                aciertos++
                if (aciertos == respuestas.size)
                {
                    println("Game over...")
                }
            } else 
            {
                handler.postDelayed({
                    primero!!.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    primero!!.setImageResource(fondo)
                    primero!!.isEnabled = true
                    imgb!!.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    imgb.setImageResource(fondo)
                    imgb.isEnabled = true
                    bloqueo = false
                    primero = null
                }, 1000)
            }
        }
    }

    //______________________________________________________________________________________________
    private fun setRespuesta(i: Int, imgb: ImageButton?)
    {
        bloqueo = true
        imgb!!.scaleType = ImageView.ScaleType.CENTER
        imgb.setImageResource(respuestas[arrayDesordenadoRespuestas[i]])
        imgb.isEnabled = false
        numeroSegundo = arrayDesordenadoRespuestas[i]
    }

    //______________________________________________________________________________________________
    private fun setPregunta(i: Int, imgb: ImageButton?)
    {
        bloqueo = true
        imgb!!.scaleType = ImageView.ScaleType.CENTER_INSIDE
        imgb.setImageResource(preguntas[arrayDesordenadoPreguntas[i]])
        imgb.isEnabled = false
        numeroSegundo = arrayDesordenadoPreguntas[i]
    }

    //______________________________________________________________________________________________
    private fun init() 
    {
        cargarTablero()
        cargarBotones()
        cargarImagenes()

        arrayDesordenadoRespuestas = desordenarArray(respuestas.size)
        arrayDesordenadoPreguntas = desordenarArray(preguntas.size)

        // setea imagen a cada button en la posicion donde se encuentra
        for (i in 0..4) 
        {
            tableroRespuestas[i]!!.scaleType = ImageView.ScaleType.CENTER_CROP
            tableroPreguntas[i]!!.scaleType = ImageView.ScaleType.CENTER_INSIDE
            tableroRespuestas[i]!!.setImageResource(respuestas[arrayDesordenadoRespuestas[i]])
            tableroPreguntas[i]!!.setImageResource(preguntas[arrayDesordenadoPreguntas[i]])
        }
        // demora  medio segundo luego esconde las imagenes puestas a los buttones
        handler.postDelayed({
            for (i in 0..4) 
            {
                tableroRespuestas[i]!!.scaleType = ImageView.ScaleType.CENTER_CROP
                tableroPreguntas[i]!!.scaleType = ImageView.ScaleType.CENTER_INSIDE
                tableroRespuestas[i]!!.setImageResource(fondo)
                tableroPreguntas[i]!!.setImageResource(fondo)
            }
        }, 500)

        // al hacer  click sobre alguna foto llama a la funcion comprobar si no esta bloqueado
        for (i in 0..4)
        {
            tableroPreguntas[i]!!.isEnabled = true
            tableroRespuestas[i]!!.isEnabled = true
            tableroRespuestas[i]!!.setOnClickListener { if (!bloqueo) comprobar(i, false, tableroRespuestas[i]) }
            tableroPreguntas[i]!!.setOnClickListener { if (!bloqueo) comprobar(i, true, tableroPreguntas[i]) }
        }
    }
    //______________________________________________________________________________________________
}