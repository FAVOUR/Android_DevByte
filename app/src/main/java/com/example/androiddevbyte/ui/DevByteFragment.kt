package com.example.androiddevbyte.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androiddevbyte.R

class DevByteFragment : Fragment() {

    companion object {
        fun newInstance() = DevByteFragment()
    }

    private lateinit var viewModel: DevByteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dev_byte_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DevByteViewModel::class.java)
        // TODO: Use the ViewModel
    }

}