package com.gmax.kotlin_one.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmax.kotlin_one.databinding.FragmentBaseTabBinding
import kotlinx.android.synthetic.main.fragment_base_tab.*

/**
 * Created by Administrator on 2017\8\1 0001.
 */
abstract class BaseTabFragment : BaseBindingFragment<FragmentBaseTabBinding>(){


//    var tabAdapter:TabAdapter

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentBaseTabBinding {
        return FragmentBaseTabBinding.inflate(inflater,container,false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nsvp.isPagingEnabled = true

        nsvp.setOffscreenPageLimit(2)
        var tabAdapter = TabAdapter(childFragmentManager, nsvp, tabLayout, activity)
        tabLayout.setupWithViewPager(nsvp)


        tabAdapter.clearFragment()
        setupAdapter(tabAdapter)

    }

    protected fun getBundle(arg: String): Bundle {
        val bundle = Bundle()
        bundle.putString("key", arg)
        return bundle
    }
    abstract fun setupAdapter(adapter: TabAdapter)
}