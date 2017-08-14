package com.gmax.kotlin_one.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by Administrator on 2017\8\1 0001.
 */
class PigeonFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        arguments
        var view = TextView(activity)
        view.text = arguments.getString("ARGS")

        return view
    }

    companion object{
        fun newInstance(content: String): PigeonFragment {
            val args = Bundle()
            args.putString("ARGS", content)
            val fragment = PigeonFragment()
            fragment.arguments = args
            return fragment
        }
    }
}