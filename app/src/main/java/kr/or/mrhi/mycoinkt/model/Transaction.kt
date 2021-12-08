package kr.or.mrhi.myCoin.model

data class Transaction(
    val coinName: String?,
    val transaction: String?,
    val transactionTime: String?,
    val quantity: String?,
    val price: String?,
    val balance: Int?,
    val avgPrice: String?
)