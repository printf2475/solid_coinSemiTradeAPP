package kr.or.mrhi.mycoinkt

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.google.android.material.tabs.TabLayout
import kr.or.mrhi.myCoin.model.Transaction
import kr.or.mrhi.mycoinkt.MainActivity.Companion.stringName
import kr.or.mrhi.mycoinkt.MainActivity.Companion.stringSymbol
import kr.or.mrhi.mycoinkt.POJO.CandleCoinData
import kr.or.mrhi.mycoinkt.viewModel.CoinViewModel
import java.lang.IllegalStateException

class CoinMain : AppCompatActivity(), View.OnClickListener {
    private lateinit var ACD_CoinName: TextView
    private lateinit var ACD_PriceChange: TextView
    private lateinit var ACD_tvCompare: TextView
    private lateinit var ACD_Percent: TextView
    private lateinit var ACD_CoinPrice: TextView
    private lateinit var totalAmountCount: TextView
    private lateinit var orderAvailableCount: TextView
    private lateinit var candleChart: CandleStickChart
    private lateinit var btnFavorite: ImageView
    private lateinit var ivUpDown: ImageView
    private lateinit var btnBuy: Button
    private lateinit var orderAmount_edttext: EditText
    private lateinit var tabLayout: TabLayout
    private lateinit var transactionList: List<Transaction?>
    private lateinit var myCoinPrice: MutableList<Int>
    private var KRWHoldings = 0
    private lateinit var mainCoinName: String
    private lateinit var mainCoinPrice: String
    private lateinit var mainCoinValue: String
    private var mainPercent = 0.0
    private var mainChangePrice = 0.0
    private var totalPriceTemp = 0.00
    private var position = 0
    private lateinit var model: CoinViewModel
    private lateinit var transaction: Transaction
    private lateinit var dbController: DBController
    private var prevClosingPrice = "0.00"
    private var count = 0
    private var tabLayoutPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_detail)
        dbController = DBController(this)
        model = ViewModelProvider(this).get(CoinViewModel::class.java)
        val intent: Intent = getIntent()
        mainCoinName = intent.getStringExtra("CoinID").toString()
        mainCoinValue = intent.getStringExtra("CoinData").toString()
        position = intent.getIntExtra("Position", DEFAULTVALUE)
        mainCoinPrice = String.format("%.0f", mainCoinValue.toDouble())
        findview()
        transactionList = dbController.getMyWallet()
        btnBuy.setOnClickListener(this)
        btnFavorite.setOnClickListener(this)
        transaction = dbController.getCoinTransaction(mainCoinName)!!
        tabLayoutPosition = tabLayout.getSelectedTabPosition()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tabLayoutPosition = tab.getPosition()
                if (tabLayoutPosition == 0) {
                    btnBuy.text = "매수"
                } else if (tabLayoutPosition == 1) {
                    btnBuy.text = "매도"
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        ACD_CoinName.setText(stringName[position] + "(" + mainCoinName + "/KRW)")
        ACD_CoinPrice.setText(mainCoinPrice)
        orderAvailableCount.setText(java.lang.String.valueOf(transaction.balance))
        orderAmount_edttext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (orderAmount_edttext.getText().length != 0) {
                    totalPriceTemp = orderAmount_edttext.text.toString().toDouble()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
//        model.getTransactionCoinData(mainCoinName)
//            .observe(this, Observer<List<String>> { stringList ->
//                var balance = 0
//                val myCoinAmong: Double =
//                    dbController.getCoinTransaction(mainCoinName)!!.quantity?.toDouble()!!
//                myCoinPrice = ArrayList()
//                transactionList = dbController.getMyWallet()
//                for (transaction in transactionList) {
//                    myCoinPrice.add(transaction?.balance!!)
//                }
//                for (i in myCoinPrice) {
//                    balance += i
//                }
//                KRWHoldings = balance
//                if (tabLayoutPosition == 0) {
//                    orderAvailableCount.setText(KRWHoldings.toString())
//                } else if (tabLayoutPosition == 1) {
//                    orderAvailableCount.setText(
//                        (myCoinAmong * ACD_CoinPrice.getText().toString().toInt()).toString()
//                    )
//                }
//                ACD_CoinPrice.setText(stringList[position])
//                mainChangePrice = stringList[position].toDouble() - prevClosingPrice.toDouble()
//                mainPercent =
//                    String.format("%.2f", mainChangePrice / prevClosingPrice.toDouble() * 100)
//                        .toDouble()
//                if (mainPercent == 0.00) {
//                    ACD_CoinPrice.setTextColor(Color.BLACK)
//                    ACD_Percent.setTextColor(Color.BLACK)
//                    ACD_PriceChange.setTextColor(Color.BLACK)
//                    ivUpDown!!.visibility = View.GONE
//                } else if (mainPercent < 0.00) {
//                    ACD_CoinPrice.setTextColor(Color.BLUE)
//                    ACD_Percent.setTextColor(Color.BLUE)
//                    ACD_PriceChange.setTextColor(Color.BLUE)
//                    ivUpDown!!.setImageResource(R.drawable.decrease)
//                } else if (mainPercent > 0.00) {
//                    ACD_CoinPrice.setTextColor(Color.RED)
//                    ACD_Percent.setTextColor(Color.RED)
//                    ACD_PriceChange.setTextColor(Color.RED)
//                    ivUpDown!!.setImageResource(R.drawable.increase)
//                }
//                ACD_Percent.setText("$mainPercent%")
//                ACD_PriceChange.setText(mainChangePrice.toString())
//                totalAmountCount.setText((totalPriceTemp.toInt() * stringList[position].toLong()).toString())
//                //                Log.i("값이 오나", stringList.get(position).toString());
////                Log.i("전일대비", String.valueOf((Double.parseDouble(stringList.get(position)) - Double.parseDouble(prevClosingPrice)) / Double.parseDouble(prevClosingPrice) * 100));//전일대비 변동률 작동 확인 완료!
//            })
//        model.getTickerDTO(mainCoinName).observe(this, Observer<Any> { tickerPOJOData ->
//            prevClosingPrice = tickerPOJOData.getPrevClosingPrice()
//            Log.i("변동률과 거래대금", tickerPOJOData.getPrevClosingPrice().toString())
//        })
//        model.getCandleCoinData(mainCoinName, "5m")
//            .observe(this, Observer<List<Any>> { candleCoinData ->
//                Log.d("캔들", candleCoinData.size.toString() + "")
//                candleDataSet(candleCoinData)
//            })
    }

    private fun findview() {
        ACD_CoinName = findViewById<TextView>(R.id.ACD_CoinName)
        ACD_PriceChange = findViewById<TextView>(R.id.ACD_PriceChange)
        ACD_tvCompare = findViewById<TextView>(R.id.ACD_tvCompare)
        ACD_Percent = findViewById<TextView>(R.id.ACD_Percent)
        ACD_CoinPrice = findViewById<TextView>(R.id.ACD_CoinPrice)
        totalAmountCount = findViewById<TextView>(R.id.totalAmountCount)
        orderAvailableCount = findViewById<TextView>(R.id.orderAvailableCount)
//        candleChart = findViewById(R.id.candleChart)
        btnFavorite = findViewById<ImageView>(R.id.btnFavorite)
        btnBuy = findViewById<Button>(R.id.btnBuy)
        orderAmount_edttext = findViewById<EditText>(R.id.orderAmount_edttext)
        tabLayout = findViewById<TabLayout>(R.id.tabLayout2)
        ivUpDown = findViewById<ImageView>(R.id.ivUpDown)
        val list = dbController.getFavoritesList()
        for (name in list) {
            if (name == mainCoinName) {
                btnFavorite.setImageResource(R.drawable.likebutton)
                count++
            }
        }
    }

    fun candleDataSet(candleCoinData: List<CandleCoinData>) {
        val values: ArrayList<CandleEntry> = ArrayList<CandleEntry>()
        for (index in candleCoinData.indices) {
            val high: Float = candleCoinData[index].maxPrice.toFloat()
            val low: Float = candleCoinData[index].minPrice.toFloat()
            val open: Float = candleCoinData[index].openingPrice.toFloat()
            val close: Float = candleCoinData[index].closingPrice.toFloat()
            values.add(CandleEntry((index + 1).toFloat(), high, low, open, close))
        }
        candleChart.setBackgroundColor(Color.WHITE)
        candleChart.getDescription().setEnabled(true)

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        candleChart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        candleChart.setPinchZoom(false)
        candleChart.setDrawGridBackground(true)
        val xAxis: XAxis = candleChart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setDrawGridLines(true)
        val leftAxis: YAxis = candleChart.getAxisLeft()
        leftAxis.setLabelCount(7, false)
        leftAxis.setDrawGridLines(true)
        leftAxis.setDrawAxisLine(true)
        val rightAxis: YAxis = candleChart.getAxisRight()
        rightAxis.setEnabled(false)
        candleChart.getLegend().setEnabled(false)
        val set1 = CandleDataSet(values, "Data Set")
        set1.setDrawIcons(false)
        set1.setAxisDependency(YAxis.AxisDependency.LEFT)
        set1.setShadowColor(Color.DKGRAY)
        set1.setShadowWidth(0.7f)
        set1.setDecreasingColor(Color.RED)
        set1.setDecreasingPaintStyle(Paint.Style.FILL)
        set1.setIncreasingColor(Color.rgb(122, 242, 84))
        set1.setIncreasingPaintStyle(Paint.Style.FILL)
        set1.setNeutralColor(Color.BLUE)
        //set1.setHighlightLineWidth(1f);
        val data = CandleData(set1)
        candleChart.setData(data)
        candleChart.invalidate()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnFavorite -> if (count % 2 == 0) {
                dbController.insertFavorite(stringSymbol.get(position))
                btnFavorite.setImageResource(R.drawable.likebutton)
                count++
            } else {
                dbController.deleteFavoritesList(stringSymbol.get(position))
                btnFavorite.setImageResource(R.drawable.unlikebutton)
                count++
            }
            R.id.btnBuy -> {
                Log.i("DB값", "눌림")
                val buyCoinNumber: String = orderAmount_edttext.getText().toString()
                val buyCoinPrice: String = ACD_CoinPrice.getText().toString()
                val balance =
                    String.format("%.0f", buyCoinNumber.toDouble() * buyCoinPrice.toDouble())
                        .toInt()
                if (tabLayoutPosition == 0) {
                    orderAvailableCount.setText(balance.toString())
                    if (KRWHoldings < balance) {
                        btnBuy.isEnabled = false
                        val dialog = AlertDialog.Builder(this)
                        dialog.setTitle("매수주문 오류")
                        dialog.setTitle("주문가능한 금액을 초과하였습니다.")
                        dialog.setPositiveButton("확인", null)
                        dialog.show()
                    } else {
                        dbController.insertTransaction(
                            Transaction(
                                stringSymbol.get(position),
                                "buy",
                                "",
                                buyCoinNumber,
                                buyCoinPrice,
                                -balance,
                                null
                            )
                        )
                        transaction = dbController.getCoinTransaction(mainCoinName)!!
                        Log.i("DB값 매수", transaction.toString())
                    }
                } else if (tabLayoutPosition == 1) {
                    val buyAmong: Int = orderAmount_edttext.getText().toString().toInt()
                    val myCoinAmong: Double =
                        dbController.getCoinTransaction(mainCoinName)?.quantity.toString().toDouble()
                    orderAvailableCount.setText((myCoinAmong * buyCoinPrice.toInt()).toString())
                    if (myCoinAmong < buyAmong) {
                        val dialog2 = AlertDialog.Builder(this)
                        dialog2.setTitle("매도주문 오류")
                        dialog2.setTitle("주문가능 금액이 부족합니다.")
                        dialog2.setPositiveButton("확인", null)
                        dialog2.show()
                    } else {
                        dbController.insertTransaction(
                            Transaction(
                                stringSymbol.get(position),
                                "sell",
                                "",
                                buyCoinNumber,
                                buyCoinPrice,
                                +balance,
                                null
                            )
                        )
                        transaction = dbController.getCoinTransaction(mainCoinName)!!
                        Log.i("DB값 매도", transaction.toString())
                    }
                }
            }
            else -> throw IllegalStateException("Unexpected value: " + view.id)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("flag", "flag")
        startActivity(intent)
    }

    companion object {
        private const val DEFAULTVALUE = 1000
    }
}