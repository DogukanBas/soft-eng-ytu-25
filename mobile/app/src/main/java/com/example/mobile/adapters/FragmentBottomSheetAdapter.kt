package com.example.mobile.adapters

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.mobile.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Herhangi bir Fragment'ı sabit yükseklikli BottomSheet olarak göstermek için adapter
 */
class FragmentBottomSheetAdapter : BottomSheetDialogFragment() {

    private var contentFragment: Fragment? = null
    private var fragmentLayoutId: Int = 0
    private var fixedHeightInDp: Int = 600 // Sabit yükseklik (dp cinsinden)

    companion object {
        fun newInstance(
            fragment: Fragment,
            fragmentLayoutId: Int,
            fixedHeightInDp: Int = 600 // Default 600dp yükseklik
        ): FragmentBottomSheetAdapter {
            return FragmentBottomSheetAdapter().apply {
                this.contentFragment = fragment
                this.fragmentLayoutId = fragmentLayoutId
                this.fixedHeightInDp = fixedHeightInDp
            }
        }
    }

    // dp'yi piksel'e çeviren yardımcı fonksiyon
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(fragmentLayoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Container'ın yüksekliğini kesin olarak ayarla
        view.layoutParams.height = dpToPx(fixedHeightInDp)

        // Fragment'ı container'a ekle
        contentFragment?.let { fragment ->
            childFragmentManager.beginTransaction()
                .replace(R.id.input_menu_container, fragment)
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()

        // Dialog penceresine başlangıç boyutunu kesin ayarla
        dialog?.let { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)

                // Sabit piksel yüksekliği ayarla
                val fixedHeight = dpToPx(fixedHeightInDp)
                it.layoutParams.height = fixedHeight

                // Bottom sheet davranışı ayarla
                behavior.skipCollapsed = false
                behavior.isDraggable = true
                behavior.peekHeight = fixedHeight  // Açıldığında tam olarak bizim yüksekliğimizde olsun
                behavior.state = BottomSheetBehavior.STATE_EXPANDED

                // EXPANDED durumu bile bizim fixedHeight'imizi aşmamalı
                behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                            // Expanded durumda bile yüksekliği sınırla
                            it.layoutParams.height = fixedHeight
                            it.requestLayout()
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        // Kaydırma sırasında yüksekliği sabit tut
                        it.layoutParams.height = fixedHeight
                        it.requestLayout()
                    }
                })
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        // Dialog penceresini hazırla
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(
                com.google.android.material.R.id.design_bottom_sheet
            )

            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)

                // Sabit piksel yüksekliği ayarla
                val fixedHeight = dpToPx(fixedHeightInDp)
                it.layoutParams.height = fixedHeight

                // Bottom sheet davranışı ayarla
                behavior.skipCollapsed = false
                behavior.isDraggable = true
                behavior.peekHeight = fixedHeight
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return dialog
    }
}