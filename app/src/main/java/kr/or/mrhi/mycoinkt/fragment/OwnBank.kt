package kr.or.mrhi.mycoinkt.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kr.or.mrhi.mycoinkt.DBController
import kr.or.mrhi.mycoinkt.MainActivity.Companion.namePositionMap
import kr.or.mrhi.mycoinkt.POJO.WalletData
import kr.or.mrhi.mycoinkt.POJO.WalletDataItem
import kr.or.mrhi.mycoinkt.adapter.WalletAdapter
import kr.or.mrhi.mycoinkt.databinding.FragmentOwnBankBinding
import kr.or.mrhi.mycoinkt.network.WalletRetrofit
import kr.or.mrhi.mycoinkt.viewModel.CoinViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class OwnBank : Fragment(), OnChartValueSelectedListener {
    private var totalBuyCount = 0.0
    private var evaluationProfitCount = 0.0
    private lateinit var walletList: List<WalletDataItem>
    private lateinit var model: CoinViewModel
    private lateinit var myCoinName: MutableList<String>
    private lateinit var priceList: MutableList<String>
    private lateinit var walletAdapter: WalletAdapter
    private lateinit var entries: ArrayList<PieEntry>
    private lateinit var walletRetrofit: WalletRetrofit
    private lateinit var myCoinPrice: ArrayList<Float>
    private lateinit var myCoinAmong: ArrayList<Int>
    private lateinit var binding :FragmentOwnBankBinding
    private var flag =false;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOwnBankBinding.inflate(LayoutInflater.from(context))
        model = ViewModelProvider(requireActivity()).get(CoinViewModel::class.java)
        walletRetrofit= WalletRetrofit.createWalletRetrofit()
        priceList = ArrayList()
        walletList = ArrayList<WalletDataItem>()
        myCoinName = ArrayList()
        entries = ArrayList()
        binding.mycoinRecycler.layoutManager= LinearLayoutManager(requireActivity())
        walletAdapter = WalletAdapter(walletList, priceList)
        binding.mycoinRecycler.adapter=walletAdapter



        walletRetrofit.getWallet()?.enqueue(object : Callback<WalletData?> {
            override fun onResponse(call: Call<WalletData?>, response: Response<WalletData?>) {
                (walletList as ArrayList<WalletDataItem>).addAll(response.body()!!)
                for (i in walletList.indices) {
                    entries.add(
                        PieEntry(
                            walletList.get(i).totalAmount.toFloat(),
                            walletList.get(i).name, null
                        )
                    )
                }
                if (!flag){
                    setChart()
                    flag=true
                }

                for (wallet in walletList) {
                    myCoinName.add(wallet.name)
                }
                model.getTransactionCoinData("BTC")
                    ?.observe(requireActivity()){ currentData ->
                        for (i in myCoinName){
                            priceList.add(currentData[namePositionMap[i]!!])
                        }
                        walletAdapter.notifyDataSetChanged()
                        var buyCount = 0.0
                        var curruntPrice = 0.0
                        var buyPrice = 0.0
                        var balance = 0
                        totalBuyCount = 0.0
                        evaluationProfitCount = 0.0

                        myCoinAmong = ArrayList<Int>()
                        myCoinPrice = ArrayList<Float>()
                        priceList.removeAll(priceList)

                        //코인별 거래들을 각각 list에 넣음
                        //코인별 거래들을 각각 list에 넣음
                        for (i in walletList.indices) {
                            myCoinAmong.add(walletList.get(i).totalAmount)
                            myCoinPrice.add(walletList.get(i).avgPrice*walletList.get(i).totalAmount)
                            balance= walletList.get(i).money.toInt()
                        }

                        for (i in myCoinName.indices) {
                            if (namePositionMap[myCoinName[i]] != null) {
                                priceList.add(currentData[namePositionMap[myCoinName[i]]!!])
                            }
                        }

                        if (!priceList.isEmpty() && walletList.size != 0) {
                            for (i in priceList.indices) {
                                buyCount = myCoinAmong.get(i).toDouble()
                                buyPrice = myCoinPrice.get(i).toDouble()
                                curruntPrice = priceList[i].toDouble()
                                totalBuyCount += buyPrice //총매수
                                evaluationProfitCount += curruntPrice * buyCount //총평가
                            }
                            binding.textTotalBuyCount.setText(String.format("%.0f", totalBuyCount)) //총매수
                            binding.textTotalEvaluationCount.setText(
                                String.format(
                                    "%.0f",
                                    evaluationProfitCount
                                )
                            ) //총평가
                            binding.textEvaluationProfitCount.setText(
                                String.format(
                                    "%.0f",
                                    evaluationProfitCount - totalBuyCount
                                )
                            )
                            binding.textYieldCount.setText(
                                String.format(
                                    "%.2f%%",
                                    (evaluationProfitCount - totalBuyCount) / totalBuyCount * 100
                                )
                            )
                        }

                        binding.KRWHoldings.setText(balance.toString())
                        binding.holdings.setText(
                            String.format(
                                "%.0f",
                                evaluationProfitCount + binding.KRWHoldings.getText().toString().toDouble()
                            )
                        )
                    }

                model.refrashTransactionDataThread()
            }

            override fun onFailure(call: Call<WalletData?>, t: Throwable) {
                Log.i("지갑", "실패"+t.message)
            }
        })






        return binding.root
    }

    private fun setChart() {
        var dataSet = PieDataSet(entries, "purchase")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)

        // add a lot of colors
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        binding.pieChart.setData(data)
        binding.pieChart.highlightValues(null)
        binding.pieChart.invalidate()
        binding.pieChart.setUsePercentValues(false)
        binding.pieChart.getDescription().setEnabled(false)
        binding.pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        binding.pieChart.setDragDecelerationFrictionCoef(0.95f)
        binding.pieChart.setCenterText("구매내역")
        binding.pieChart.setDrawHoleEnabled(true)
        binding.pieChart.setHoleColor(Color.WHITE)
        binding.pieChart.setTransparentCircleColor(Color.WHITE)
        binding.pieChart.setTransparentCircleAlpha(110)
        binding.pieChart.setHoleRadius(58f)
        binding.pieChart.setTransparentCircleRadius(61f)
        binding.pieChart.setDrawCenterText(true)
        binding.pieChart.setDrawEntryLabels(true)
        binding.pieChart.setRotationAngle(0f)
        binding.pieChart.setRotationEnabled(true)
        binding.pieChart.setHighlightPerTapEnabled(true)
        binding.pieChart.setOnChartValueSelectedListener(this)
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
        binding.pieChart.setEntryLabelColor(Color.BLACK)
        binding.pieChart.setEntryLabelTextSize(12f)

        var l: Legend = binding.pieChart.getLegend()
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f
    }

    override fun onValueSelected(e: Entry?, h: Highlight) {
        if (e == null) return
        Log.i(
            "VAL SELECTED",
            "Value: " + e.getY().toString() + ", index: " + h.getX()
                .toString() + ", DataSet index: " + h.getDataSetIndex()
        )
    }

    override fun onNothingSelected() {
        Log.i("PieChart", "nothing selected")
    }

}