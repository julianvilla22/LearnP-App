package es.com.uam.eps.tfg.learnp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import es.com.uam.eps.tfg.learnp.R
import es.com.uam.eps.tfg.learnp.model.Word
import java.util.*

class WordAdapter (private val wordList : List<Word>) : RecyclerView.Adapter<WordAdapter.Wordviewholder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Wordviewholder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.word_result, parent, false)
        return Wordviewholder(v, mListener)
    }


    override fun onBindViewHolder(holder: Wordviewholder, position: Int) {
        val w : Word = this.wordList[position] ?: Word(-1, "No results")

        holder.textResult.text = w.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

    override fun getItemCount(): Int {
        return wordList.size ?: 0
    }

    class Wordviewholder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var textResult: TextView

        init {
            textResult = itemView.findViewById(R.id.text_result)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

}