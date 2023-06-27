package es.com.uam.eps.tfg.learnp.database

import android.content.Context
import es.com.uam.eps.tfg.learnp.model.Word
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.DriverManager

class DatabaseHelper(private val context: Context) {

    private val DB_HOST = "10.0.2.2"
    private val DB_PORT = "3306"
    private val DB_NAME = "specs"
    private val DB_USER = "andro"
    private val DB_PASSWORD = "andro"

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun executeWordQuery(query: String): List<Word> = withContext(dispatcher) {
        val resultList = mutableListOf<Word>()

        try {
            Class.forName("com.mysql.jdbc.Driver")
            val url = "jdbc:mysql://$DB_HOST:$DB_PORT/$DB_NAME"
            val connection = DriverManager.getConnection(url, DB_USER, DB_PASSWORD)
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(query)

            while (resultSet.next()) {
                val id = resultSet.getInt("idword")
                val name = resultSet.getString("name")
                val obj = Word(id, name)
                resultList.add(obj)
            }

            resultSet.close()
            statement.close()
            connection.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext resultList
    }
}
