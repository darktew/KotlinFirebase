package com.example.nopicts.firebasecrud

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var herolist : MutableList<Hero>
    lateinit var ref: DatabaseReference

    lateinit var listView : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        herolist = mutableListOf()

        listView = listViewData

        ref = FirebaseDatabase.getInstance().getReference("heros")

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()) {
                    herolist.clear()
                    for(h in p0.children) {
                        val hero = h.getValue(Hero::class.java)
                        herolist.add(hero!!)
                    }

                    val adapter = HeroAdapter(this@MainActivity, R.layout.hero, herolist)

                    listView.adapter = adapter
                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }

        })

    }

    fun saveHero(view: View) {
        val name = editText.text.toString().trim()

        if(name.isEmpty()) {
            editText.error = "Plase enter a name"
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("heros")

        val heroId = ref.push().key

        val hero = Hero(heroId,name, rating.rating.toInt())

        ref.child(heroId).setValue(hero).addOnCompleteListener {
            editText.text.clear()
            rating.rating = 0.0f
            Toast.makeText(applicationContext, "Hero saving success", Toast.LENGTH_LONG).show()
        }
    }
}
