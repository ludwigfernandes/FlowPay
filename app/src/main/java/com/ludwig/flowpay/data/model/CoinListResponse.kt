package com.ludwig.flowpay.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

typealias CoinListResponse = List<CoinData>

@JsonClass(generateAdapter = true)
data class CoinData(
    @Json(name = "id") val id: String? = null,
    @Json(name = "symbol") val symbol: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "image") val image: String? = null,
    @Json(name = "current_price") val currentPrice: Double? = null,
    @Json(name = "market_cap") val marketCap: Long? = null,
    @Json(name = "market_cap_rank") val marketCapRank: Long? = null,
    @Json(name = "fully_diluted_valuation") val fullyDilutedValuation: Long? = null,
    @Json(name = "total_volume") val totalVolume: Double? = null,
    @Json(name = "high_24h") val high24H: Double? = null,
    @Json(name = "low_24h") val low24H: Double? = null,
    @Json(name = "price_change_24h") val priceChange24H: Double? = null,
    @Json(name = "price_change_percentage_24h") val priceChangePercentage24H: Double? = null,
    @Json(name = "market_cap_change_24h") val marketCapChange24H: Double? = null,
    @Json(name = "market_cap_change_percentage_24h") val marketCapChangePercentage24H: Double? = null,
    @Json(name = "circulating_supply") val circulatingSupply: Double? = null,
    @Json(name = "total_supply") val totalSupply: Double? = null,
    @Json(name = "max_supply") val maxSupply: Double? = null,
    @Json(name = "ath") val ath: Double? = null,
    @Json(name = "ath_change_percentage") val athChangePercentage: Double? = null,
    @Json(name = "ath_date") val athDate: String? = null,
    @Json(name = "atl") val atl: Double? = null,
    @Json(name = "atl_change_percentage") val atlChangePercentage: Double? = null,
    @Json(name = "atl_date") val atlDate: String? = null,
    @Json(name = "roi") val roi: CoinDataROI? = null,
    @Json(name = "last_updated") val lastUpdated: String? = null
)

@JsonClass(generateAdapter = true)
data class CoinDataROI(
    @Json(name = "times") val times: Double? = null,
    @Json(name = "currency") val currency: String? = null,
    @Json(name = "percentage") val percentage: Double? = null
)