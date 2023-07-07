package es.com.uam.eps.tfg.learnp.adapter;

import android.view.LayoutInflater
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.com.uam.eps.tfg.learnp.R

import es.com.uam.eps.tfg.learnp.model.Rule;

class RuleAdapter(private val ruleList : List<Rule>) : RecyclerView.Adapter<RuleAdapter.Ruleviewholder>(){

    private lateinit var mListener: RuleAdapter.onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: RuleAdapter.onItemClickListener){
        mListener = listener
    }
    override fun onCreateViewHolder(
        parent:ViewGroup,
        viewType:Int
    ):Ruleviewholder{
        val v:View=
            LayoutInflater.from(parent.context).inflate(R.layout.word_result,parent,false)
        return Ruleviewholder(v, mListener)
    }


    override fun onBindViewHolder(holder:Ruleviewholder,position:Int){
        val r:Rule=this.ruleList[position]?:Rule(-1, -1,"No results")

        holder.textResult.text=r.text
    }

    override fun getItemCount():Int{
        return ruleList.size?:0
    }

    class Ruleviewholder(itemView: View, listener: onItemClickListener) :RecyclerView.ViewHolder(itemView){
        var textResult: TextView

        init{
            textResult=itemView.findViewById(R.id.text_result)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }
    }

}
