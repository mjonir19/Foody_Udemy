package com.example.foody_udemy_training_2.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.foody_udemy_training_2.databinding.FragmentInstructionsBinding
import com.example.foody_udemy_training_2.util.Constants

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: com.example.foody_udemy_training_2.models.Result? = args?.getParcelable(
            Constants.RECIPE_RESULT_KEY
        )

        // setup webview
        binding.instructionsWebView.webViewClient = object : WebViewClient() {}
        val websiteUrl : String = myBundle!!.sourceUrl
        binding.instructionsWebView.loadUrl(websiteUrl)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}