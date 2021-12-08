package kr.or.mrhi.mycoinkt.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.or.mrhi.myCoin.model.Transaction
import kr.or.mrhi.mycoinkt.POJO.HistoryItem
import kr.or.mrhi.mycoinkt.databinding.FragmentTransacitemBinding

class TransactionHistoryAdapter(transactionList: ArrayList<HistoryItem>) :
    RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder?>() {
    lateinit var context: Context
    var transactionList:  ArrayList<HistoryItem>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding= FragmentTransacitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        Log.i("거래내역", transactionList.size.toString())
        return transactionList.size
    }

    inner class ViewHolder(val binding: FragmentTransacitemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            val amount: Double = transactionList[position].amount.toDouble()
            val price: Double = transactionList[position].price.toDouble()
            val totalPrice = String.format("%.2f", amount * price)
            binding.transactionCoin .text=transactionList[position].coinName
            binding.transactionTime.text=transactionList[position].trTime
            binding.transactionAmount.text=transactionList[position].amount
            binding.transactionPrice .text=transactionList[position].price
            binding.transactionTotalPrice.text=totalPrice
        }
    }

    init {
        this.transactionList = transactionList
    }
}