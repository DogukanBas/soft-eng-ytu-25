// GenerateReportFragment.kt
import android.content.pm.ActivityInfo
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

    // Fragment içerisinde kullanılacak değişkenleri tanımlıyoruz
    private var xAxisLabels: List<String> = listOf() // Bu "dd-MM-yyyy" formatını saklayacak
    private var yAxisValues: List<Float> = listOf()

    private lateinit var lineChart: LineChart
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private lateinit var updateButton: Button
    private var startDate: Date? = null
    private var endDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            val originalXAxisLabels = args.getStringArrayList(ARG_X_LABELS) ?: listOf()
            // Gelen "yyyy-MM-dd" formatını "dd-MM-yyyy" formatına çevir
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            xAxisLabels = originalXAxisLabels.mapNotNull { label ->
                try {
                    val date = inputFormat.parse(label)
                    date?.let { outputFormat.format(it) } ?: label // Parse edilemezse orijinali kullan
                } catch (e: Exception) {
                    label // Hata durumunda orijinali kullan
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

        // Veri varsa grafiği çiz
        if (xAxisLabels.isNotEmpty() && yAxisValues.isNotEmpty()) {
            setupLineChart()
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
                labelRotationAngle = -45f // X eksenindeki tarihlerin açısını ayarla
                textSize = 10f // Metin boyutunu küçült
                labelCount = calculateOptimalLabelCount(xAxisLabels.size) // Optimal etiket sayısını hesapla
                setAvoidFirstLastClipping(true) // İlk ve son etiketlerin kırpılmasını önle
            }

            // Y ekseni ayarlamaları
            axisLeft.apply {
                setDrawGridLines(true)
                granularity = 1f
            }
            axisRight.isEnabled = false

            // Ekstra alan bırak
            setExtraOffsets(15f, 10f, 15f, 20f)

            // Grafik içi ayarları
            legend.textSize = 12f
            setVisibleXRangeMaximum(12f) // Bir seferde en fazla 12 etiket göster

            invalidate()
        }
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

        // Tarih aralığını kontrol et
        if (endDate.before(startDate)) {
            // Hata durumunda kullanıcıyı bilgilendir
            return
        }

        try {
            // Create a list of indices and labels that are within the date range
            val filteredData = xAxisLabels.mapIndexedNotNull { index, label ->
                try {
                    val date = sdf.parse(label)
                    if (date != null && !date.before(startDate) && !date.after(endDate)) {
                        Pair(index, label)
                    } else null
                } catch (e: Exception) {
                    null // Tarih formatı geçersizse bu etiketi atla
                }
            }

            // Filtrelenmiş veri yoksa kullanıcıyı bilgilendir ve işlemi iptal et
            if (filteredData.isEmpty()) {
                return
            }

            // Extract filtered indices and labels
            val filteredIndices = filteredData.map { it.first }
            val filteredLabels = filteredData.map { it.second }

            // Yeni grafiği oluşturmak için filtrelenmiş değerleri kullan
            val filteredYValues = filteredIndices.map { yAxisValues[it] }

            // Create new entries with consecutive x values (0, 1, 2...) instead of original indices
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

                // Update the x-axis with filtered labels
                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(filteredLabels)
                    labelCount = calculateOptimalLabelCount(filteredLabels.size)
                }

                // Adjust x-axis bounds if needed
                if (filteredLabels.isNotEmpty()) {
                    xAxis.axisMinimum = -0.5f // Biraz ekstra alan bırak
                    xAxis.axisMaximum = (filteredLabels.size - 0.5f) // Biraz ekstra alan bırak
                }

                // Görünümü yenile
                notifyDataSetChanged()
                invalidate()
            }
        } catch (e: Exception) {
            // Hata durumunda orijinal grafiği göster
            setupLineChart()
        }
    }
}