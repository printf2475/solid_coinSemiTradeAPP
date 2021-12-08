package kr.or.mrhi.mycoinkt.adapter

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.or.mrhi.mycoinkt.MainActivity.Companion.namePositionMap
import kr.or.mrhi.mycoinkt.MainActivity.Companion.stringName
import kr.or.mrhi.mycoinkt.R
import kr.or.mrhi.mycoinkt.WebViewActivity
import kr.or.mrhi.mycoinkt.databinding.CoinlistItemBinding
import kr.or.mrhi.mycoinkt.model.TickerDTO

class MainCoinAdapter(
    private val priceList: List<String>,
    var searchList: List<String>,

    ) : RecyclerView.Adapter<MainCoinAdapter.ViewHolders?>() {
    private lateinit var tickerData: List<TickerDTO>
    private var changeRate = 0.0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding= CoinlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    fun setTickerData(tickerData: List<TickerDTO>?) {
        this.tickerData = tickerData!!;
    }


    inner class ViewHolders(val binding: CoinlistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        lateinit var tradeValue24HStr: String
        lateinit var prevClosingPrice: String
        lateinit var currentPrice: String
        var currentPriceDouble = 0.0
        var prevClosingPriceDouble = 0.0
        var volume: Long = 0
        var volumeList = 0.0
        var tradeValue24H: Long = 0
        var tradeValue24HDouble = 0.0


        init {
            itemView.setOnClickListener { view ->
                val position: Int = getAdapterPosition()
                if (position >= 0 && position < searchList.size) {
                    val intent = Intent(view.context, WebViewActivity::class.java)
                    intent.putExtra("Link", "http://192.168.0.187/solid/coin/coin.php?coinName="+searchList.get(position))
                    view.context.startActivity(intent)
                }
            }
        }

        fun onBind(position: Int) {
            prevClosingPrice = null.toString()
            volumeList = 0.0
            if (!searchList.isEmpty() && !priceList.isEmpty()) {
                initCoinData(position)
                changeTextColor(changeRate)
            }
        }
        private fun initCoinData(position: Int) {

            binding.tvCoinName.text=stringName.get(namePositionMap[searchList.get(position)]!!)
            binding.tvCoinTicker.text=searchList.get(position) + "/KRW"
            currentPrice = priceList[position]
            binding.tvCurrentPrice.text=currentPrice
            prevClosingPrice = tickerData.get(position).prevClosingPrice!!
            tradeValue24HDouble = tickerData.get(position).accTradeValue24H!!.toDouble()
            currentPriceDouble = currentPrice.toDouble()
            prevClosingPriceDouble = prevClosingPrice.toDouble()
            changeRate =
                (currentPriceDouble - prevClosingPriceDouble) / prevClosingPriceDouble * 100
            binding.tvChangeRate.text = String.format("%.2f", changeRate)
            tradeValue24HStr = String.format("%.0f", tradeValue24HDouble)
            tradeValue24H = tradeValue24HStr.toLong()
            binding.tvTradeValue24H.text=(tradeValue24H / 1000000).toString() + "백만"
        }

        private fun changeTextColor(changeRate: Double) {
            if (changeRate == 0.00) {
                binding.tvChangeRate.setTextColor(Color.BLACK)
                binding.tvCurrentPrice.setTextColor(Color.BLACK)
                binding.tvTradeValue24H.setTextColor(Color.BLACK)
            } else if (changeRate < 0.00) {
                binding.tvChangeRate.setTextColor(Color.BLUE)
                binding.tvCurrentPrice.setTextColor(Color.BLUE)
                binding.tvTradeValue24H.setTextColor(Color.BLUE)
            } else if (changeRate > 0.00) {
                binding.tvChangeRate.setTextColor(Color.RED)
                binding.tvCurrentPrice.setTextColor(Color.RED)
                binding.tvTradeValue24H.setTextColor(Color.RED)
            }
        }


    }
}