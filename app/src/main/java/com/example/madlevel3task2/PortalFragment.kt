package com.example.madlevel3task2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.madlevel3task2.databinding.FragmentAddPortalBinding


const val PORTAL = "portal"
/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

class PortalFragment : Fragment() {

    private lateinit var binding: FragmentAddPortalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPortalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddPortal.setOnClickListener {
            onAddPortal()
        }
    }

    private fun onAddPortal() {
        val portalTitle = binding.etTitle.text.toString()
        val portalUrl = binding.etUrl.text.toString()

        if (portalTitle.isNotBlank() and portalUrl.isNotBlank()) {
            setFragmentResult(
                PORTAL,
                bundleOf(
                    PORTAL to Portal(portalTitle, portalUrl)
                )
            )

            findNavController().popBackStack()
        } else {
            Toast.makeText(
                activity,
                R.string.error,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}