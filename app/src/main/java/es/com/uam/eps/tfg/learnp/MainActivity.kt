package es.com.uam.eps.tfg.learnp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.Resources
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request

import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import es.com.uam.eps.tfg.learnp.adapter.WordAdapter
import es.com.uam.eps.tfg.learnp.database.DatabaseHelper
import es.com.uam.eps.tfg.learnp.database.DbRequest
import es.com.uam.eps.tfg.learnp.database.WordApi
import es.com.uam.eps.tfg.learnp.databinding.ActivityMainBinding
import es.com.uam.eps.tfg.learnp.model.Word
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var db: DatabaseHelper;
    private lateinit var words : List<Word>
    private val results = mutableListOf<Word>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    lateinit var wordAdapter: WordAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //setSupportActionBar(binding.toolbar)

        /*val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)*/

        /*binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        db = DatabaseHelper(this)
        recyclerView = findViewById(R.id.results) ?: RecyclerView(applicationContext)

        loadWords()

        searchView = findViewById(R.id.search)
        searchView.animation

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i(TAG,"Llego al querysubmit")
                if(!query.isNullOrEmpty()){
                    updateResults(query)
                    return true
                } else
                    updateResults("")

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.i(TAG,"Llego al querytextchange")
                updateResults(newText)
                return true
            }

        })
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                LinearLayout.VERTICAL
            )
        )




    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    private fun loadWords(){
        Log.i(TAG, "loadWords")
        val dbr  = DbRequest()
        val wordApi = dbr.retrofit.create(WordApi::class.java) as WordApi

        wordApi.getAllWords()
                .enqueue(object : Callback<List<Word>> {
                override fun onResponse(
                    call: Call<List<Word>>,
                    response: Response<List<Word>>
                ) {
                    words = response.body()!!.sortedBy { w->
                        w.name.uppercase()
                    }
                    results.addAll(words)
                    populateListView(results)
                }


                override fun onFailure(call: Call<List<Word>>, t: Throwable) {
                    Log.i(TAG,"La carga de palabras ha fallado")
                }

                })

    }

    private fun populateListView(body: List<Word>) {
        Log.i(TAG, "PopulateListView")
        this.wordAdapter = WordAdapter(results)

        recyclerView.adapter = wordAdapter
        wordAdapter.setOnItemClickListener(object : WordAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                var clicked = results[position]

                Log.i(TAG, "Has clickado en la palabra: " + clicked.name + " Id: " + clicked.idword)

                var myIntent = Intent(this@MainActivity, WordActivity::class.java)
                myIntent.putExtra("idword", clicked.idword)
                myIntent.putExtra("name", clicked.name)

                this@MainActivity.startActivity(myIntent)
            }

        })
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter!!.notifyDataSetChanged()



    }

    private fun updateResults(query: String){
        results.clear()

        if (query.isNullOrEmpty()) {
            results.addAll(words)
        } else{
            results.addAll(words.filter { w ->
                w.name.lowercase().contains(query.lowercase())
            })
        }
        if(results.isEmpty()){
            recyclerView.visibility = View.GONE
            binding.textNoResult.text = "Your search \"$query\" did not yield any results."
            binding.textNoResult.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            binding.textNoResult.visibility = View.INVISIBLE
        }

        recyclerView.adapter!!.notifyDataSetChanged()

    }
}