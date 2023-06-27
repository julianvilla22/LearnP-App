package es.com.uam.eps.tfg.learnp

import android.app.Application
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import es.com.uam.eps.tfg.learnp.database.LearnPDatabase
import es.com.uam.eps.tfg.learnp.model.Word
import org.json.JSONObject
import java.util.concurrent.Executors

class LearnPApplication : Application(){
    /* Ejecutor de hilos secundarios */
    private val executor = Executors.newSingleThreadExecutor()

    val url = "localhost:8080/word"

    init {
        /*// Iniciliza aquí la lista de tarjetas del mazo Inglés
        cards.add(Card("To wake up", "Despertarse"))
        cards.add(Card("To give in", "Dar el brazo a torcer"))
        cards.add(Card("To pick up", "Recoger"))
        // Iniciliza aquí la lista de tarjetas del mazo Francés
        cards2.add(Card("Manger", "Comer"))
        cards2.add(Card("Parler", "Hablar"))
        cards2.add(Card("Trouver", "Encontrar"))
        // Iniciliza aquí la lista de tarjetas del mazo Japonés
        cards3.add(Card("Arigato", "Gracias"))
        cards3.add(Card("Nani", "Qué"))

        // Inicializa la lista de mazos
        decks.add(Deck("Inglés"))
        decks.add(Deck("Francés"))
        decks.add(Deck("Japonés"))

        // Asocia las tarjetas a cada mazo
        decks[0].cards = cards
        decks[1].cards = cards2
        decks[2].cards = cards3*/
    }

    /* Aqui se crea e inicializa la BD de la App */
    override fun onCreate() {
        super.onCreate()




        // Añade unas tarjetas mediante los métodos del DAO en un hilo secundario
/*        executor.execute {
            learnPDatabase.wordDao.addDeck(Deck("Inglés", 1))
            learnPDatabase.wordDao.addDeck(Deck("Francés", 2))
            learnPDatabase.wordDao.addDeck(Deck("Japonés", 3))

            learnPDatabase.wordDao.addCard(Card(question = "To wake up", answer = "Despertarse", deck = 1))
            learnPDatabase.wordDao.addCard(Card(question = "To give in", answer = "Dar el brazo a torcer", deck = 1))
            learnPDatabase.wordDao.addCard(Card(question = "To pick up", answer = "Recoger", deck = 1))
            learnPDatabase.wordDao.addCard(Card(question = "Look forward", answer = "Desear", deck = 1))

            learnPDatabase.wordDao.addCard(Card(question = "Manger", answer = "Comer", deck = 2))
            learnPDatabase.wordDao.addCard(Card(question = "Parler", answer = "Hablar", deck = 2))
            learnPDatabase.wordDao.addCard(Card(question = "Trouver", answer = "Encontrar", deck = 2))

            learnPDatabase.wordDao.addCard(Card(question = "Arigato", answer = "Gracias", deck = 3))
            learnPDatabase.wordDao.addCard(Card(question = "Nani", answer = "Qué", deck = 3))
        }*/

        //Timber.plant(Timber.DebugTree())
    }

    companion object {
        var words: MutableList<Word> = mutableListOf<Word>()
    }
    public fun getAllWords() : List<Word>? {
        val url = "http://my-json-feed"
        var res : JSONObject;

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                res = response
            },
            { error ->
                // TODO: Handle error
            }
        )

        return ArrayList<Word>()

    }


}