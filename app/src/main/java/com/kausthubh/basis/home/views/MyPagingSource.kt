package com.kausthubh.basis.home.views

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kausthubh.basis.home.adapter.viewholder.CardViewHolder.MyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class MyPagingSource(private val context: Context) : PagingSource<Int, MyData>() {
    override fun getRefreshKey(state: PagingState<Int, MyData>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MyData> =
        withContext(Dispatchers.IO) {
            val offset = params.key ?: 0
            val size = params.loadSize
            val list = mutableListOf<MyData>()
            if (offset < 0 || offset > 100) {
                return@withContext LoadResult.Page(emptyList(), null, null)
            }
            delay(1000)
            val width = getDisplayWidth(context)
            val spToPx = spToPx(1f, context)
            val dpToPx = dpToPx(1f, context)
            val height = dpToPx(500f, context)

            val newWidth = width - (2 * 32 * dpToPx)
            val newHeight = height - (40 * dpToPx + 2 * 22 * spToPx * 1.2f)

            for (i in 0 until size) {
                val subtext = randomText()
                val textWidth = subtext.length * spToPx * 18
                val lines = (textWidth.toFloat() / newWidth).roundToInt()
                val lineHeight = 18 * spToPx * 1.2f
                val maxLines = (newHeight / lineHeight).toInt()
                val split = lines > maxLines
                if (split) {
                    val charInLine = newWidth / (spToPx * 18)
                    val textLines = split(subtext, charInLine * maxLines)
                    textLines.forEachIndexed { index, text ->
                        val data = MyData(
                            if (index == 0) "What is the meaning of SARFAESI Act? ${offset + i}" else "",
                            text,
                            offset + i,
                            100,
                            index != 0,
                        )
                        list.add(data)
                    }
                } else {
                    val data = MyData(
                        "What is the meaning of SARFAESI Act? ${offset + i}",
                        subtext,
                        offset + i,
                        100,
                        false,
                    )
                    list.add(data)
                }
            }
            return@withContext LoadResult.Page(list, offset - size, offset + size)
        }

    private fun randomText(): String {
        val random = (1..3).random()
        var string =
            "The Securitisation and Reconstruction of Financial Assets and Enforcement of Security Interest Act, 2002, allows banks and financial institutions to enforce the security interest they hold in the assets of the borrower without the intervention of the court."
        for (i in 1..random) {
            string += "\n The Securitisation and Reconstruction of Financial Assets and Enforcement of Security Interest Act, 2002, allows banks and financial institutions to enforce the security interest they hold in the assets of the borrower without the intervention of the court."
        }
        return string
    }

    private fun split(string: String, number: Int): List<String> {
        val list = mutableListOf<String>()
        var length = string.length
        var lastSplit = 0
        while (length > number) {
            val index = string.substring(lastSplit, lastSplit + number).lastIndexOf(' ')
            if (index <= 0) break
            list.add(string.substring(lastSplit, lastSplit + index).trim())
            length -= index
            lastSplit += index
        }
        list.add(string.substring(lastSplit).trim())
        return list
    }
}
