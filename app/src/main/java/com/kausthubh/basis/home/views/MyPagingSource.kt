package com.kausthubh.basis.home.views

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kausthubh.basis.home.adapter.viewholder.CardViewHolder.MyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MyPagingSource : PagingSource<Int, MyData>() {
    override fun getRefreshKey(state: PagingState<Int, MyData>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MyData> =
        withContext(Dispatchers.IO) {
            val offset = params.key ?: 0
            val size = params.loadSize
            val list = mutableListOf<MyData>()
            println("Sachin offset $offset $size")
            if (offset < 0 || offset > 100) {
                return@withContext LoadResult.Page(emptyList(), null, null)
            }
            println("Sachin Before ${System.currentTimeMillis()}")
            delay(1000)
            println("Sachin After ${System.currentTimeMillis()}")
            for (i in 0 until size) {
                if (((offset + i) % 7 == 0) && false) {
                    val data = MyData(
                        "What is the meaning of SARFAESI Act? ${offset + i}",
                        "The Securitisation and Reconstruction of Financial Assets and Enforcement of Security Interest Act, 2002, allows banks and financial institutions to enforce the security interest they hold in the assets of the borrower without the intervention of the court.",
                        offset + i,
                        100,
                        false,
                    )
                    val data2 = MyData(
                        "",
                        "The Securitisation and Reconstruction of Financial Assets and Enforcement of Security Interest Act, 2002, allows banks and financial institutions to enforce the security interest they hold in the assets of the borrower without the intervention of the court.",
                        offset + i,
                        100,
                        true,
                    )
                    list.add(data)
                    list.add(data2)
                } else {
                    val data = MyData(
                        "What is the meaning of SARFAESI Act? ${offset + i}",
                        "The Securitisation and Reconstruction of Financial Assets and Enforcement of Security Interest Act, 2002, allows banks and financial institutions to enforce the security interest they hold in the assets of the borrower without the intervention of the court.",
                        offset + i,
                        100,
                        false,
                    )
                    list.add(data)
                }
            }
            println("sachin 2 $offset")
            return@withContext LoadResult.Page(list, offset - size, offset + size)
        }
}
