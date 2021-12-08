package kr.or.mrhi.mycoinkt.POJO

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class TransactionData {
    @SerializedName("transaction_date")
    @Expose
    var transactionDate: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("units_traded")
    @Expose
    var unitsTraded: String? = null

    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("total")
    @Expose
    var total: String? = null
    override fun toString(): String {
        return "TransactionData{" +
                "transactionDate='" + transactionDate + '\'' +
                ", type='" + type + '\'' +
                ", unitsTraded='" + unitsTraded + '\'' +
                ", price='" + price + '\'' +
                ", total='" + total + '\'' +
                '}'
    }
}