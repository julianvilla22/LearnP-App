package es.com.uam.eps.tfg.learnp

import android.content.ContentValues
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import java.util.Locale


class WordActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityWordBinding

    private lateinit var rules : List<Rule>
    private lateinit var homophones : List<Word>
    private lateinit var rulesRecyclerView: RecyclerView
    private lateinit var homophonesRecyclerView: RecyclerView
    private var idword: Long = 0
    private lateinit var ruleAdapter: RuleAdapter
    private lateinit var wordAdapter: WordAdapter
    private lateinit var tts: TextToSpeech
    private lateinit var spkButton: ImageButton
    private lateinit var textWord : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar : Toolbar = binding.toolbar
        this.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener {
            finish()
        }

        spkButton = binding.ttsButton
        spkButton.isEnabled = false

        tts = TextToSpeech(applicationContext, this)

        spkButton.setOnClickListener{
            speakOut()
        }

        textWord = intent.getStringExtra("name")!!.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

        binding.word.text = textWord

        binding.activityTitle.text = String.format(resources.getString(R.string.word_activity_title), textWord)

        idword = intent.getIntExtra("idword",0).toLong()


        loadRules()
        loadHomophones()


        rulesRecyclerView = findViewById(R.id.rules) ?: RecyclerView(applicationContext)

        rulesRecyclerView.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                LinearLayout.VERTICAL
            )
        )
        homophonesRecyclerView = findViewById(R.id.homophones)
        homophonesRecyclerView.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                LinearLayout.VERTICAL
            )
        )






    }

    private fun loadHomophones() {
        Log.i(ContentValues.TAG, "loadHomophones")
        val dbr  = DbRequest()
        val wordApi = dbr.retrofit.create(WordApi::class.java) as WordApi

        wordApi.getWordHomophones(idword)
            .enqueue(object : Callback<List<Word>> {
                override fun onResponse(
                    call: Call<List<Word>>,
                    response: Response<List<Word>>
                ) {
                    homophones = response.body()!!
                    populateHomophonesListView(homophones)
                }


                override fun onFailure(call: Call<List<Word>>, t: Throwable) {
                    Log.i(ContentValues.TAG,"La carga de homófonos ha fallado")
                    binding.textConnectionFailed.visibility = View.VISIBLE
                    binding.textNoHomophones.visibility = View.GONE
                    binding.homophones.visibility = View.GONE
                }

            })

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
                    populateRulesListView(rules)
                }


                override fun onFailure(call: Call<List<Rule>>, t: Throwable) {
                    Log.i(ContentValues.TAG,"La carga de reglas ha fallado")
                    binding.textConnectionFailedRules.visibility = View.VISIBLE
                    binding.textNoRules.visibility = View.GONE
                    binding.rules.visibility = View.GONE
                }

            })

    }

    private fun populateRulesListView(rules: List<Rule>) {
        Log.i(ContentValues.TAG, "PopulateListView")
        this.ruleAdapter = RuleAdapter(rules)

        rulesRecyclerView.adapter = ruleAdapter
        ruleAdapter.setOnItemClickListener(object : RuleAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                var clicked = rules[position]

                Log.i(ContentValues.TAG, "Has clickado en la regla: " + clicked.idrule + " Text: " + clicked.text)

                var myIntent = Intent(this@WordActivity, RuleActivity::class.java)
                myIntent.putExtra("idrule", clicked.idrule)
                myIntent.putExtra("text", clicked.text)

                this@WordActivity.startActivity(myIntent)

            }

        })
        if(rules.isEmpty()){
            rulesRecyclerView.visibility = View.GONE
            binding.textNoRules.visibility = View.VISIBLE

        }else{
            rulesRecyclerView.visibility = View.VISIBLE
            binding.textNoRules.visibility = View.GONE
        }
        rulesRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        rulesRecyclerView.adapter!!.notifyDataSetChanged()


    }
    private fun populateHomophonesListView(homophones: List<Word>) {
        Log.i(ContentValues.TAG, "PopulateListView")
        this.wordAdapter = WordAdapter(homophones)

        homophonesRecyclerView.adapter = wordAdapter
        wordAdapter.setOnItemClickListener(object : WordAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

            }

        })
        if(homophones.isEmpty()){
            homophonesRecyclerView.visibility = View.GONE
            binding.textNoHomophones.visibility = View.VISIBLE

        }else{
            homophonesRecyclerView.visibility = View.VISIBLE
            binding.textNoHomophones.visibility = View.GONE
        }
        homophonesRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        homophonesRecyclerView.adapter!!.notifyDataSetChanged()


    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.ENGLISH)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language not supported!")
            } else {
                spkButton.isEnabled = true
            }
        }
    }
    private fun speakOut() {
        tts.speak(textWord, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }


}