package com.example.nopicts.firebasecrud

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text


class HeroAdapter(val mCtx: Context, val layoutId: Int, val heroList: List<Hero>)
      : ArrayAdapter<Hero>(mCtx,layoutId,heroList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view : View = layoutInflater.inflate(layoutId, null)

        val heroName = view.findViewById<TextView>(R.id.heroName)

        val buttonUpdate = view.findViewById<ImageView>(R.id.editButton)
        val buttonDelete = view.findViewById<ImageView>(R.id.deleteButton)

        val hero = heroList[position]

        heroName.text = hero.name

        buttonUpdate.setOnClickListener {
            updateHero(hero)
        }

        buttonDelete.setOnClickListener {
            deleteHero(hero)
        }

        return view
    }

    fun updateHero(hero: Hero) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("UpDateHero")

        val inflater = LayoutInflater.from(mCtx)

        val view  = inflater.inflate(R.layout.hero_update, null)

        val editText = view.findViewById<EditText>(R.id.editText)

        val rating = view.findViewById<RatingBar>(R.id.rating)

        editText.setText(hero.name)
        rating.rating = hero.rating.toFloat()

        builder.setView(view)

        builder.setPositiveButton("Update") { dialog, which ->
            val dbHero = FirebaseDatabase.getInstance().getReference("heros")

            val name = editText.text.trim().toString()

            if(name.isEmpty()) {
                editText.error =  "Plase enter value"
                editText.requestFocus()
                return@setPositiveButton
            }

            val hero = Hero(hero.id,name,rating.rating.toInt())

            dbHero.child(hero.id).setValue(hero)

            Toast.makeText(mCtx,"Update Success",Toast.LENGTH_LONG).show()

        }

        builder.setNegativeButton("No") { dialog, which ->
        }

        val alert = builder.create()
        alert.show()
    }

    @SuppressLint("SetTextI18n")
    fun deleteHero(hero: Hero) {
        val builder = AlertDialog.Builder(mCtx)

        builder.setTitle("DeleteHero")

        val inflater = LayoutInflater.from(mCtx)

        val view  = inflater.inflate(R.layout.hero_delete, null)


        val deleteView = view.findViewById<TextView>(R.id.deleteId)

        deleteView.text = "Confirm Delete " + hero.name

        builder.setView(view)

        builder.setPositiveButton("Yes") { dialog, which ->
            val dbHero = FirebaseDatabase.getInstance().getReference("heros")

            dbHero.child(hero.id).removeValue()
        }

        builder.setNegativeButton("No") { dialog, which ->
        }

        val alert = builder.create()
        alert.show()
    }
}