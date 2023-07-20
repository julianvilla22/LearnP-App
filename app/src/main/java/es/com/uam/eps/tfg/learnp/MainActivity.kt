package es.com.uam.eps.tfg.learnp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.com.uam.eps.tfg.learnp.adapter.WordAdapter
import es.com.uam.eps.tfg.learnp.database.DbRequest
import es.com.uam.eps.tfg.learnp.database.WordApi
import es.com.uam.eps.tfg.learnp.databinding.ActivityMainBinding
import es.com.uam.eps.tfg.learnp.model.Word
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var words : List<Word>
    private val results = mutableListOf<Word>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    lateinit var wordAdapter: WordAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        recyclerView = findViewById(R.id.results) ?: RecyclerView(applicationContext)

        loadWords()

        searchView = findViewById(R.id.search)

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

        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
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
                    populateListView()
                }


                override fun onFailure(call: Call<List<Word>>, t: Throwable) {
                    Log.i(TAG,"La carga de palabras ha fallado")
                    recyclerView.visibility = View.GONE
                    binding.textNoResult.visibility = View.GONE
                    binding.textConnectionFailed.visibility = View.VISIBLE
                    words = ArrayList()
                    populateListView()


                }

                })

    }

    private fun populateListView() {
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
            binding.textConnectionFailed.visibility = View.GONE
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