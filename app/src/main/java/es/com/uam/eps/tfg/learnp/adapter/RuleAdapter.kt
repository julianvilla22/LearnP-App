package es.com.uam.eps.tfg.learnp.adapter;

import android.view.LayoutInflater
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.com.uam.eps.tfg.learnp.R

import es.com.uam.eps.tfg.learnp.model.Rule;

class RuleAdapter(private val ruleList : List<Rule>) : RecyclerView.Adapter<RuleAdapter.Ruleviewholder>(){

    override fun onCreateViewHolder(
        parent:ViewGroup,
        viewType:Int
    ):Ruleviewholder{
        val v:View=
            LayoutInflater.from(parent.context).inflate(R.layout.word_result,parent,false)
        return Ruleviewholder(v)
    }


    override fun onBindViewHolder(holder:Ruleviewholder,position:Int){
        val r:Rule=this.ruleList[position]?:Rule(-1, -1,"No results")

        holder.textResult.text=r.text
    }

    override fun getItemCount():Int{
        return ruleList.size?:0
    }

    class Ruleviewholder(itemView:View) :RecyclerView.ViewHolder(itemView){
        var textResult: TextView

        init{
            textResult=itemView.findViewById(R.id.text_result)

        }
    }

}
