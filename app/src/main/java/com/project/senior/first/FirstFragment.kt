package com.project.senior.first

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.senior.R
import com.project.senior.databinding.FragmentFirstBinding
import com.project.senior.databinding.FragmentSeniorDetailsBinding


class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentFirstBinding.inflate(inflater,container,false)
        return binding.root
    }

}