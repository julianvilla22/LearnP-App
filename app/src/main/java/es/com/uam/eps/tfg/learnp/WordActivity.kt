package es.com.uam.eps.tfg.learnp

import android.R
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.com.uam.eps.tfg.learnp.ExpandableListDataPump.data
import es.com.uam.eps.tfg.learnp.adapter.RuleAdapter
import es.com.uam.eps.tfg.learnp.adapter.WordAdapter
import es.com.uam.eps.tfg.learnp.database.DbRequest
import es.com.uam.eps.tfg.learnp.database.RuleApi
import es.com.uam.eps.tfg.learnp.database.WordApi
import es.com.uam.eps.tfg.learnp.databinding.ActivityWordBinding
import es.com.uam.eps.tfg.learnp.model.Rule
import es.com.uam.eps.tfg.learnp.model.Word
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap


class WordActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityWordBinding

    private lateinit var rules : List<Rule>
    private lateinit var recyclerView: RecyclerView
    private var idword: Long = 0
    lateinit var ruleAdapter: RuleAdapter


    var expandableListView: ExpandableListView? = null
    var expandableListAdapter: ExpandableListAdapter? = null
    var expandableListTitle: List<String>? = null
    var expandableListDetail: HashMap<String, List<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setSupportActionBar(binding.toolbar)
        var a = intent
        binding.word.text = intent.getStringExtra("name")!!.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        idword = intent.getIntExtra("idword",0).toLong()


        loadRules()

//        expandableListView = binding.expandableListView
//        expandableListDetail = data
//        expandableListTitle = ArrayList(expandableListDetail!!.keys)
//        expandableListAdapter = CustomExpandableListAdapter(
//            this, expandableListTitle as ArrayList<String>,
//            expandableListDetail!!
//        )
//        expandableListView!!.setAdapter(expandableListAdapter)
//        expandableListView!!.setOnGroupExpandListener { groupPosition ->
//            Toast.makeText(
//                applicationContext,
//                (expandableListTitle as ArrayList<String>)[groupPosition] + " List Expanded.",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//        expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
//            Toast.makeText(
//                applicationContext,
//                (expandableListTitle as ArrayList<String>)[groupPosition]
//                        + " -> "
//                        + expandableListDetail!![(expandableListTitle as ArrayList<String>)[groupPosition]]!![childPosition],
//                Toast.LENGTH_SHORT
//            ).show()
//            false
//        }

        recyclerView = findViewById(es.com.uam.eps.tfg.learnp.R.id.rules) ?: RecyclerView(applicationContext)






    }

    private fun loadRules(){
        Log.i(ContentValues.TAG, "loadRules")
        val dbr  = DbRequest()
        val ruleApi = dbr.retrofit.create(RuleApi::class.java) as RuleApi

        ruleApi.getWordRules(idword)
            .enqueue(object : Callback<List<Rule>> {
                override fun onResponse(
                    call: Call<List<Rule>>,
                    response: Response<List<Rule>>
                ) {
                    rules = response.body()!!
                    populateListView(rules)
                }


                override fun onFailure(call: Call<List<Rule>>, t: Throwable) {
                    Log.i(ContentValues.TAG,"La carga de reglas ha fallado")
                }

            })

    }

    private fun populateListView(rules: List<Rule>) {
        Log.i(ContentValues.TAG, "PopulateListView")
        this.ruleAdapter = RuleAdapter(rules)

        recyclerView.adapter = ruleAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter!!.notifyDataSetChanged()


    }

}