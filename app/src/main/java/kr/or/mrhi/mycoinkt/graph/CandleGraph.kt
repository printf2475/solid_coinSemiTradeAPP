package kr.or.mrhi.mycoinkt.graph

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import kr.or.mrhi.mycoinkt.R

class CandleGraph : DemoBase() {
    private lateinit var chart: CandleStickChart
    private val tvX: TextView? = null
    private val tvY: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_coin_detail)
        title = "CandleStickChartActivity"
        chart = findViewById(R.id.candleChart)
        chart.setBackgroundColor(Color.WHITE)
        chart.getDescription().setEnabled(false)

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)
        val xAxis: XAxis = chart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setDrawGridLines(false)
        val leftAxis: YAxis = chart.getAxisLeft()
        //        leftAxis.setEnabled(false);
        leftAxis.setLabelCount(7, false)
        leftAxis.setDrawGridLines(false)
        leftAxis.setDrawAxisLine(false)
        val rightAxis: YAxis = chart.getAxisRight()
        rightAxis.setEnabled(false)
        //        rightAxis.setStartAtZero(false);
        chart.getLegend().setEnabled(false)
    }
}