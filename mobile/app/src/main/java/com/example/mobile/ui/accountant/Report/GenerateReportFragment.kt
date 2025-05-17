// GenerateReportFragment.kt
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.app.DatePickerDialog
import android.util.Log
import android.widget.TextView
import com.example.mobile.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.example.mobile.ui.BaseFragment
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.min

class GenerateReportFragment : BaseFragment() {

    companion object {
        private const val ARG_X_LABELS = "x_labels"
        private const val ARG_Y_VALUES = "y_values"

        fun newInstance(xAxisLabels: ArrayList<String>, yAxisValues: FloatArray): GenerateReportFragment {
            val fragment = GenerateReportFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_X_LABELS, xAxisLabels)
            args.putFloatArray(ARG_Y_VALUES, yAxisValues)
            fragment.arguments = args
            return fragment
        }
    }

    private var xAxisLabels: List<String> = listOf()
    private var yAxisValues: List<Float> = listOf()

    private lateinit var lineChart: LineChart
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private lateinit var updateButton: Button

    private lateinit var dailyAverage: TextView
    private lateinit var monthlyAverage: TextView
    private lateinit var yearlyAverage: TextView

    private var startDate: Date? = null
    private var endDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            val originalXAxisLabels = args.getStringArrayList(ARG_X_LABELS) ?: listOf()
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            xAxisLabels = originalXAxisLabels.mapNotNull { label ->
                try {
                    val date = inputFormat.parse(label)
                    date?.let { outputFormat.format(it) } ?: label
                } catch (e: Exception) {
                    label
                }
            }

            val yArray = args.getFloatArray(ARG_Y_VALUES)
            yAxisValues = yArray?.toList() ?: listOf()
        } ?: run {
            xAxisLabels = listOf()
            yAxisValues = listOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_line_chart_with_datepicker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lineChart = view.findViewById(R.id.lineChart)
        startDateButton = view.findViewById(R.id.startDateButton)
        endDateButton = view.findViewById(R.id.endDateButton)
        updateButton = view.findViewById(R.id.updateButton)

        dailyAverage = view.findViewById(R.id.tv_daily_average)
        monthlyAverage = view.findViewById(R.id.tv_monthly_average)
        yearlyAverage = view.findViewById(R.id.tv_yearly_average)

        if (xAxisLabels.isNotEmpty() && yAxisValues.isNotEmpty()) {
            setupLineChart()
            calculateAssumptions()
        }

        startDateButton.setOnClickListener {
            showDatePicker { date ->
                startDate = date
                startDateButton.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
            }
        }

        endDateButton.setOnClickListener {
            showDatePicker { date ->
                endDate = date
                endDateButton.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
            }
        }

        updateButton.setOnClickListener {
            if (startDate != null && endDate != null) {
                updateChartForDateRange(startDate!!, endDate!!)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    private fun setupLineChart() {
        val entries = yAxisValues.mapIndexed { index, value -> Entry(index.toFloat(), value) }
        val lineDataSet = LineDataSet(entries, "Data").apply {
            setDrawFilled(true)
            fillDrawable = resources.getDrawable(R.drawable.gradient_fill, null)
            color = resources.getColor(R.color.teal_700, null)
        }

        val lineData = LineData(lineDataSet)

        lineChart.apply {
            data = lineData
            description.isEnabled = false
            animateY(1000)

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(xAxisLabels)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
                labelRotationAngle = -45f
                textSize = 10f
                labelCount = calculateOptimalLabelCount(xAxisLabels.size)
                setAvoidFirstLastClipping(true)
            }

            axisLeft.apply {
                setDrawGridLines(true)
                granularity = 1f
            }
            axisRight.isEnabled = false

            setExtraOffsets(15f, 10f, 15f, 20f)

            legend.textSize = 12f
            setVisibleXRangeMaximum(12f)

            invalidate()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun calculateAssumptions() {
        val sum_per_month = calculateSumPerMonth()
        val sum_per_year = calulateSumPerYear()

        Log.i("TAG", "Averages per month: $sum_per_month")
        Log.i("TAG", "Averages per year: $sum_per_year")

        val last_day = xAxisLabels[xAxisLabels.size - 1].split("-")[0].toInt()
        val last_month = xAxisLabels[xAxisLabels.size - 1].split("-")[1].toInt()
        val last_year = xAxisLabels[xAxisLabels.size - 1].split("-")[2].toInt()

        val day_diff = xAxisLabels[xAxisLabels.size - 1].split("-")[0].toInt() - xAxisLabels[0].split("-")[0].toInt() + 1
        val month_diff = xAxisLabels[xAxisLabels.size - 1].split("-")[1].toInt() - xAxisLabels[0].split("-")[1].toInt() + 1
        val year_diff = xAxisLabels[xAxisLabels.size - 1].split("-")[2].toInt() - xAxisLabels[0].split("-")[2].toInt() + 1

        val n_day = min(day_diff, 5)
        val n_month = min(month_diff, 5)
        val n_year = min(year_diff, 5)

        Log.i(TAG,"last_day: $last_day, last_month: $last_month, last_year: $last_year")
        Log.i(TAG,"n_day: $n_day, n_month: $n_month, n_year: $n_year")

        var daily_average = 0f
        var monthly_average = 0f
        var yearly_average = 0f

        var dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val endDate = LocalDate.of(last_year, last_month, last_day)
        for (offset in 0 until n_day) {
            val currentDate = endDate.minusDays(offset.toLong())
            val dateString = currentDate.format(dateFormatter)
            val index = xAxisLabels.indexOf(dateString)
            if (index != -1) {
                if (index < yAxisValues.size) {
                    daily_average += yAxisValues[index]
                }
            }
        }
        daily_average /= n_day.toFloat()

        dateFormatter = DateTimeFormatter.ofPattern("MM-yyyy")
        for (offset in 0 until n_month) {
            val currentDate = endDate.minusMonths(offset.toLong())
            val dateString = currentDate.format(dateFormatter)
            if (dateString in sum_per_month) {
                monthly_average += sum_per_month[dateString] ?: 0f
            }
        }
        monthly_average /= n_month.toFloat()

        dateFormatter = DateTimeFormatter.ofPattern("yyyy")
        for (offset in 0 until n_year) {
            val currentDate = endDate.minusYears(offset.toLong())
            val dateString = currentDate.format(dateFormatter)
            if (dateString in sum_per_year) {
                yearly_average += sum_per_year[dateString] ?: 0f
            }
        }
        yearly_average /= n_year.toFloat()

        Log.i(TAG,"Daily average: $daily_average")
        Log.i(TAG,"Monthly average: $monthly_average")
        Log.i(TAG,"Yearly average: $yearly_average")

        dailyAverage.text = String.format("Daily Average: %.2f", daily_average)
        monthlyAverage.text = String.format("Monthly Average: %.2f", monthly_average)
        yearlyAverage.text = String.format("Yearly Average: %.2f", yearly_average)
    }

    private fun calculateSumPerMonth(): Map<String, Float> {
        val monthValues = mutableMapOf<String, MutableList<Float>>()
        for (i in xAxisLabels.indices) {
            val dateParts = xAxisLabels[i].split("-")
            val monthKey = "${dateParts[1]}-${dateParts[2]}"
            monthValues.getOrPut(monthKey) { mutableListOf() }.add(yAxisValues[i])
        }
        return monthValues.mapValues { it.value.sum() }
    }

    private fun calulateSumPerYear(): Map<String, Float> {
        val yearValues = mutableMapOf<String, MutableList<Float>>()
        for (i in xAxisLabels.indices) {
            val dateParts = xAxisLabels[i].split("-")
            val yearKey = dateParts[2]
            yearValues.getOrPut(yearKey) { mutableListOf() }.add(yAxisValues[i])
        }
        return yearValues.mapValues { it.value.sum() }
    }

    private fun calculateOptimalLabelCount(size: Int): Int {
        // Etiket sayısını ekran genişliğine göre optimize et
        return when {
            size <= 7 -> size
            size <= 14 -> size / 2
            size <= 30 -> size / 3
            else -> size / 4
        }
    }

    private fun showDatePicker(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }.time
            onDateSelected(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun updateChartForDateRange(startDate: Date, endDate: Date) {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        if (endDate.before(startDate)) {
            return
        }

        try {
            val filteredData = xAxisLabels.mapIndexedNotNull { index, label ->
                try {
                    val date = sdf.parse(label)
                    if (date != null && !date.before(startDate) && !date.after(endDate)) {
                        Pair(index, label)
                    } else null
                } catch (e: Exception) {
                    null
                }
            }
            if (filteredData.isEmpty()) {
                return
            }

            val filteredIndices = filteredData.map { it.first }
            val filteredLabels = filteredData.map { it.second }
            val filteredYValues = filteredIndices.map { yAxisValues[it] }
            val filteredEntries = filteredYValues.mapIndexed { index, value ->
                Entry(index.toFloat(), value)
            }
            val lineDataSet = LineDataSet(filteredEntries, "Filtered Data").apply {
                setDrawFilled(true)
                fillDrawable = resources.getDrawable(R.drawable.gradient_fill, null)
                color = resources.getColor(R.color.teal_700, null)
            }

            lineChart.apply {
                data = LineData(lineDataSet)
                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(filteredLabels)
                    labelCount = calculateOptimalLabelCount(filteredLabels.size)
                }

                if (filteredLabels.isNotEmpty()) {
                    xAxis.axisMinimum = -0.5f
                    xAxis.axisMaximum = (filteredLabels.size - 0.5f)
                }

                notifyDataSetChanged()
                invalidate()
            }
        } catch (e: Exception) {
            setupLineChart()
        }
    }
}