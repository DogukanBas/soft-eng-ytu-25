// GenerateReportFragment.kt
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.app.DatePickerDialog
import androidx.fragment.app.Fragment
import com.example.mobile.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.example.mobile.ui.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class GenerateReportFragment(
    private val xAxisLabels: List<String>,
    private val yAxisValues: List<Float>
) : BaseFragment() {

    private lateinit var lineChart: LineChart
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private lateinit var updateButton: Button
    private var startDate: Date? = null
    private var endDate: Date? = null

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

        setupLineChart()

        startDateButton.setOnClickListener {
            showDatePicker { date ->
                startDate = date
                startDateButton.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
            }
        }

        endDateButton.setOnClickListener {
            showDatePicker { date ->
                endDate = date
                endDateButton.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
            }
        }

        updateButton.setOnClickListener {
            if (startDate != null && endDate != null) {
                updateChartForDateRange(startDate!!, endDate!!)
            }
        }
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
            }

            axisRight.isEnabled = false
            invalidate()
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
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val filteredEntries = xAxisLabels.mapIndexedNotNull { index, label ->
            val date = sdf.parse(label)
            if (date != null && date in startDate..endDate) {
                Entry(index.toFloat(), yAxisValues[index])
            } else null
        }

        val lineDataSet = LineDataSet(filteredEntries, "Filtered Data").apply {
            setDrawFilled(true)
            fillDrawable = resources.getDrawable(R.drawable.gradient_fill, null)
            color = resources.getColor(R.color.teal_700, null)
        }

        lineChart.data = LineData(lineDataSet)
        lineChart.invalidate()
    }
}
