package com.kausthubh.basis.home.views

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.RecyclerView
import com.kausthubh.basis.databinding.ActivityMainBinding
import com.kausthubh.basis.home.adapter.viewholder.CardAdapter
import com.kausthubh.basis.misc.custom.OnSwipeTouchListener
import com.yuyakaido.android.cardstackview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : androidx.appcompat.app.AppCompatActivity(), CardStackListener {

    lateinit var layoutManager: CardStackLayoutManager

    lateinit var binding: ActivityMainBinding
    val adapter = CardAdapter()
    val config = PagingConfig(5, 5, false, 5)
    val pager = Pager(config, 25) {
        MyPagingSource()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    /**
     * Everthing related to setting up the application.
     *
     */
    private fun initViews() = with(binding) {
        val rewindSetting = RewindAnimationSetting.Builder()
            .setDirection(Direction.Left)
            .setDuration(500)
            .setInterpolator(OvershootInterpolator())
            .build()

        val swipeAnimationSetting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Right)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .build()

        layoutManager = CardStackLayoutManager(this@MainActivity, this@MainActivity)
        cards.layoutManager = layoutManager
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        cards.adapter = adapter

        layoutManager.setDirections(arrayListOf(Direction.Right))
        layoutManager.setStackFrom(StackFrom.Bottom)
        layoutManager.setVisibleCount(5)
        layoutManager.setRewindAnimationSetting(rewindSetting)
        layoutManager.setSwipeAnimationSetting(swipeAnimationSetting)

        // Button Interactions
        restack.setOnClickListener {
            cards.swipe()
        }

        previousCard.setOnClickListener {
            cards.rewind()
        }

        container.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
            override fun onSwipeDown() {
                cards.rewind()
            }
        })

        lifecycleScope.launch(Dispatchers.IO) {
            pager.flow.collectLatest {
                adapter.submitData(it)
            }
        }


        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                delay(100)
                println(adapter.snapshot().items.joinToString { it.index.toString() })
                withContext(Dispatchers.Main) {
                    val index = adapter.snapshot().items.indexOfFirst { it.index == 25 }
                    println("Sachin Index bla bla $index ${adapter.itemCount}")
                    if (index > 0) {
                        layoutManager.scrollToPosition(index)
                    }
                }
            }
        }
    }

    override fun onCardRewound() {
        handleChanges()
    }

    override fun onCardCanceled() {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        handleChanges()
    }

    /**
     * Handle visibility and miscellanous things when cards are swiped.
     */
    fun handleChanges() = with(binding) {
        val item = adapter.getMyData(layoutManager.topPosition)
        val index = item?.index ?: 0
        val last = item?.lastIndex ?: 0
        tvProgress.text = "${index + 1}/$last"
        placeholder.visibility =
            if (layoutManager.topPosition == (layoutManager.itemCount)) View.VISIBLE else View.GONE
        cards.visibility =
            if (layoutManager.topPosition == (layoutManager.itemCount)) View.GONE else View.VISIBLE
        tvProgress.visibility = cards.visibility
    }
}
