package kr.or.mrhi.mycoinkt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.or.mrhi.mycoinkt.MainActivity
import kr.or.mrhi.mycoinkt.MainActivity.Companion.namePositionMap
import kr.or.mrhi.mycoinkt.POJO.WalletDataItem
import kr.or.mrhi.mycoinkt.databinding.MyCoinItemBinding


class WalletAdapter( walletList: List<WalletDataItem>,
                     priceList: List<String>) :
    RecyclerView.Adapter<WalletAdapter.ViewHolder>() {
    private var priceList: List<String> = priceList
    private val walletList: List<WalletDataItem> = walletList
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding= MyCoinItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return walletList.size
    }

    inner class ViewHolder(val binding: MyCoinItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            var currentPrice = 0.0
            if (position < walletList.size  && !priceList.isEmpty()) {
                currentPrice = priceList[position].toDouble()

                val amount: Double = walletList[position].totalAmount.toString().toDouble()
                val avgPrice: Double = walletList[position].avgPrice.toDouble()
                val valueCount = currentPrice * amount
                binding.myCoinName.text= MainActivity.stringName[namePositionMap[walletList[position].name]!!]
                binding.myCoinTicker.text=walletList[position].name
                binding.myCoinMarginCount.text=String.format("%.0f",valueCount-avgPrice*amount)
                binding.myCoinAmountCount.text=String.format("%.0f", amount)
                binding.myCoinAveragePriceCount.text=String.format("%.0f", avgPrice)
                binding.myCoinValueCount.text=String.format("%.0f", currentPrice*amount)
                binding.myCoinBuyPriceCount.text=String.format("%.0f", avgPrice*amount)
                binding.myCoinPercentCount.text= String.format("%.2f%%", (valueCount-avgPrice*amount) / valueCount* 100)
            }
        }
    }

}