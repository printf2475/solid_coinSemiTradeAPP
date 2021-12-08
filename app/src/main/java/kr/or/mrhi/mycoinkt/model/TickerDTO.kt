package kr.or.mrhi.mycoinkt.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

open class TickerDTO{
    @SerializedName("opening_price")
    @Expose
    var openingPrice: String? = null

    @SerializedName("closing_price")
    @Expose
    var closingPrice: String? = null

    @SerializedName("min_price")
    @Expose
    var minPrice: String? = null

    @SerializedName("max_price")
    @Expose
    var maxPrice: String? = null

    @SerializedName("units_traded")
    @Expose
    var unitsTraded: String? = null

    @SerializedName("acc_trade_value")
    @Expose
    var accTradeValue: String? = null

    @SerializedName("prev_closing_price")
    @Expose
    var prevClosingPrice: String? = null

    @SerializedName("units_traded_24H")
    @Expose
    var unitsTraded24H: String? = null

    @SerializedName("acc_trade_value_24H")
    @Expose
    var accTradeValue24H: String? = null

    @SerializedName("fluctate_24H")
    @Expose
    var fluctate24H: String? = null

    @SerializedName("fluctate_rate_24H")
    @Expose
    var fluctateRate24H: String? = null

}