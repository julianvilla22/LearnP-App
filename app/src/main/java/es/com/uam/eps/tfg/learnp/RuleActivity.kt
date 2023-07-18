package es.com.uam.eps.tfg.learnp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.com.uam.eps.tfg.learnp.adapter.WordAdapter
import es.com.uam.eps.tfg.learnp.database.DbRequest
import es.com.uam.eps.tfg.learnp.database.WordApi
import es.com.uam.eps.tfg.learnp.databinding.ActivityRuleBinding
import es.com.uam.eps.tfg.learnp.model.Rule
import es.com.uam.eps.tfg.learnp.model.Word
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RuleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRuleBinding
    private lateinit var examples : List<Word>
    private lateinit var exceptions : List<Word>
    private lateinit var examplesRecyclerView: RecyclerView
    private lateinit var exceptionsRecyclerView: RecyclerView
    private var idrule: Long = 0
    private lateinit var description: String
    lateinit var wordAdapter: WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar : Toolbar = binding.toolbar
        this.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener {
            finish()
        }

        idrule = intent.getLongExtra("idrule",0).toLong()
        description = intent.getStringExtra("text")!!

        binding.ruleDescription.text = description

        loadExamples()
        loadExceptions()
        examplesRecyclerView = findViewById(R.id.examples) ?: RecyclerView(applicationContext)

        examplesRecyclerView.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                LinearLayout.VERTICAL
            )
        )
        exceptionsRecyclerView = findViewById(R.id.exceptions) ?: RecyclerView(applicationContext)

        exceptionsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                LinearLayout.VERTICAL
            )
        )

    }

    private fun loadExamples() {
        Log.i(ContentValues.TAG, "loadExamples")
        val dbr  = DbRequest()
        val wordApi = dbr.retrofit.create(WordApi::class.java) as WordApi

        wordApi.getRuleExamples(idrule)
            .enqueue(object : Callback<List<Word>> {
                override fun onResponse(
                    call: Call<List<Word>>,
                    response: Response<List<Word>>
                ) {
                    examples = response.body()!!
                    populateExamplesListView(examples)
                }


                override fun onFailure(call: Call<List<Word>>, t: Throwable) {
                    Log.i(ContentValues.TAG,"La carga de ejemplos ha fallado")
                }

            })

    }
    private fun loadExceptions() {
        Log.i(ContentValues.TAG, "loadExceptions")
        val dbr  = DbRequest()
        val wordApi = dbr.retrofit.create(WordApi::class.java) as WordApi

        wordApi.getRuleExceptions(idrule)
            .enqueue(object : Callback<List<Word>> {
                override fun onResponse(
                    call: Call<List<Word>>,
                    response: Response<List<Word>>
                ) {
                    exceptions = response.body()!!
                    populateExceptionsListView(exceptions)
                }


                override fun onFailure(call: Call<List<Word>>, t: Throwable) {
                    Log.i(ContentValues.TAG,"La carga de excepciones ha fallado")
                }

            })

    }

    private fun populateExamplesListView(examples: List<Word>) {
        Log.i(ContentValues.TAG, "PopulateListView")
        this.wordAdapter = WordAdapter(examples)

        examplesRecyclerView.adapter = wordAdapter
        wordAdapter.setOnItemClickListener(object : WordAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

            }

        })
        examplesRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        examplesRecyclerView.adapter!!.notifyDataSetChanged()


    }
    private fun populateExceptionsListView(examples: List<Word>) {
        Log.i(ContentValues.TAG, "PopulateExceptionsListView")
        this.wordAdapter = WordAdapter(examples)

        exceptionsRecyclerView.adapter = wordAdapter
        wordAdapter.setOnItemClickListener(object : WordAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

            }

        })
        if(exceptions.isEmpty()){
            exceptionsRecyclerView.visibility = View.GONE
            binding.textNoExceptions.visibility = View.VISIBLE
        }else{
            exceptionsRecyclerView.visibility = View.VISIBLE
            binding.textNoExceptions.visibility = View.GONE
        }

        exceptionsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        exceptionsRecyclerView.adapter!!.notifyDataSetChanged()


    }


}