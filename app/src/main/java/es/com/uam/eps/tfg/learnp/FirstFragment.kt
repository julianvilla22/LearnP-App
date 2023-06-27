package es.com.uam.eps.tfg.learnp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import es.com.uam.eps.tfg.learnp.adapter.WordAdapter
import es.com.uam.eps.tfg.learnp.database.DatabaseHelper
import es.com.uam.eps.tfg.learnp.databinding.FragmentFirstBinding
import es.com.uam.eps.tfg.learnp.model.Word
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var db: DatabaseHelper;
    private lateinit var list : List<Word>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    lateinit var wordAdapter: WordAdapter

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        db = DatabaseHelper(this.requireContext())
        recyclerView = view?.findViewById(R.id.results) ?: RecyclerView(requireContext())

        lifecycleScope.launch {
            list = db.executeWordQuery("SELECT * FROM specs.word")
            // Aquí puedes hacer lo que necesites con los datos obtenidos
            wordAdapter = WordAdapter(list)
            recyclerView.adapter = wordAdapter
            (recyclerView.adapter as WordAdapter).notifyDataSetChanged()
        }






        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}