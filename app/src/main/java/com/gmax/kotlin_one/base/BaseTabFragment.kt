package com.gmax.kotlin_one.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmax.kotlin_one.databinding.FragmentBaseTabBinding
import com.gmax.kotlin_one.utils.RxBus
import kotlinx.android.synthetic.main.fragment_base_tab.*
import rx.Observable

/**
 * Created by Administrator on 2017\8\1 0001.
 */
abstract class BaseTabFragment : BaseBindingFragment<FragmentBaseTabBinding>(){


//    var tabAdapter:TabAdapter
    lateinit var cancleOb:Observable<Boolean>

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentBaseTabBinding {
        return FragmentBaseTabBinding.inflate(inflater,container,false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nsvp.isPagingEnabled = true

        nsvp.offscreenPageLimit = 2
        var tabAdapter = TabAdapter(childFragmentManager, nsvp, tabLayout, activity)
        tabLayout.setupWithViewPager(nsvp)


        tabAdapter.clearFragment()
        setupAdapter(tabAdapter)

        cancleOb = RxBus.getInstance().register("cancle",Boolean::class.java)
        cancleOb.subscribe{

            isCancle ->
            if (isCancle){
                cancle.visibility = View.VISIBLE
                base_add.visibility = View.GONE
            } else {
                cancle.visibility = View.GONE
                base_add.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getInstance().unregister("cancle",cancleOb)
    }
    protected fun getBundle(arg: String): Bundle {
        val bundle = Bundle()
        bundle.putString("key", arg)
        return bundle
    }
    abstract fun setupAdapter(adapter: TabAdapter)
}