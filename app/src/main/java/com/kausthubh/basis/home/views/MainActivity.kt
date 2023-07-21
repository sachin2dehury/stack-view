package com.kausthubh.basis.home.views

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.RecyclerView
import com.kausthubh.basis.databinding.ActivityMainBinding
import com.kausthubh.basis.home.adapter.viewholder.CardAdapter
import com.stacklayoutmanager.StackLayoutManager
import com.stacklayoutmanager.extras.layoutinterpolators.DenseStackInterpolator
import com.yuyakaido.android.cardstackview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import library.stacklayoutmanager.extras.transformers.RotationTransformer

class MainActivity : androidx.appcompat.app.AppCompatActivity(), CardStackListener {

    lateinit var layoutManager: CardStackLayoutManager
    private val stackLayoutManager = StackLayoutManager(
        maxViews = 8,
        horizontalLayout = true,
        scrollMultiplier = 1.5f,

        )

    var userInteracted = false

    private val snapHelper = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                stackLayoutManager.snap()
            }
        }
    }

    lateinit var binding: ActivityMainBinding
    val adapter = CardAdapter()
    val config = PagingConfig(5, 5, true, 5)
    val pager = Pager(config, 25) {
        MyPagingSource(applicationContext)
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

//        stackLayoutManager.viewTransformer = RotationTransformer::transform
//        stackLayoutManager.layoutInterpolator = DenseStackInterpolator()
//        cards.apply {
//            hasFixedSize()
//            addOnScrollListener(snapHelper)
//            layoutManager = stackLayoutManager
//            postDelayed({ stackLayoutManager.peek() }, 1000)
//        }

        layoutManager = CardStackLayoutManager(this@MainActivity, this@MainActivity)
        cards.layoutManager = layoutManager

        cards.adapter = adapter

        layoutManager.setDirections(arrayListOf(Direction.Right))
        layoutManager.setStackFrom(StackFrom.Bottom)
        layoutManager.setVisibleCount(5)
        layoutManager.setRewindAnimationSetting(rewindSetting)
        layoutManager.setSwipeAnimationSetting(swipeAnimationSetting)

        // Button Interactions
        restack.setOnClickListener {
            userInteracted = true
            cards.swipe()
        }

        previousCard.setOnClickListener {
            userInteracted = true
            cards.rewind()
        }

        adapter.addLoadStateListener {
            binding.progressBar.isVisible =
                it.append is LoadState.Loading || it.prepend is LoadState.Loading
        }

//        adapter.addOnPagesUpdatedListener {
//            handleChanges()
//            lifecycleScope.launch(Dispatchers.IO) {
//                delay(100)
//                if (!userInteracted) {
//                    withContext(Dispatchers.Main) {
//                        val index = adapter.snapshot().items.indexOfFirst { it.index == 25 }
//                        if (index > 0) {
//                            layoutManager.scrollToPosition(index)
//                        }
//                    }
//                }
//            }
//        }

        lifecycleScope.launch(Dispatchers.IO) {
            pager.flow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onCardRewound() {
        handleChanges()
    }

    override fun onCardCanceled() {}

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        userInteracted = true
    }

    override fun onCardSwiped(direction: Direction?) {
        userInteracted = true
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
