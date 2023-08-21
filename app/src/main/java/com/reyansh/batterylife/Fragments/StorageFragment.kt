package com.reyansh.batterylife.Fragments

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.ActivityManager
import android.content.Context
import android.os.*
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.timqi.sectorprogressview.ColorfulRingProgressView
import com.reyansh.batterylife.Methods
import com.reyansh.batterylife.R
import java.io.File
import java.text.DecimalFormat
import java.util.*


class StorageFragment : Fragment() {


    private var donutRAMUsage: ColorfulRingProgressView? = null
    private var donutInternalStorage: ColorfulRingProgressView? = null
    private var donutExternalStorage: ColorfulRingProgressView? = null

    private var tvUsedMemory: TextView? = null
    private var tvFreeMemory: TextView? = null
    private var tvTotalMemory: TextView? = null
    private var tvUsedIntMemory: TextView? = null
    private var tvFreeIntMemory: TextView? = null
    private var tvTotalIntMemory: TextView? = null
    private var tvUsedExtMemory: TextView? = null
    private var tvFreeExtMemory: TextView? = null
    private var tvTotalExtMemory: TextView? = null


    private var tvExtMemoryPerc: TextView? = null
    private var tvIntMemoryPerc: TextView? = null
    private var tvMemoryPerc: TextView? = null


    private var llExtMemory: LinearLayout? = null
    private val df = DecimalFormat("#")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_storage, container, false)

        donutRAMUsage = view.findViewById(R.id.donut_ram_usage)
        donutInternalStorage = view.findViewById(R.id.donut_internal_storage)
        donutExternalStorage = view.findViewById(R.id.donut_external_storage)

        tvUsedMemory = view.findViewById(R.id.tv_used_memory)
        tvFreeMemory = view.findViewById(R.id.tv_free_memory)
        tvTotalMemory = view.findViewById(R.id.tv_total_memory)

        tvUsedIntMemory = view.findViewById(R.id.tv_used_intmemory)
        tvFreeIntMemory = view.findViewById(R.id.tv_free_intmemory)
        tvTotalIntMemory = view.findViewById(R.id.tv_total_intmemory)

        tvUsedExtMemory = view.findViewById(R.id.tv_used_extmemory)
        tvFreeExtMemory = view.findViewById(R.id.tv_free_extmemory)
        tvTotalExtMemory = view.findViewById(R.id.tv_total_extmemory)
        llExtMemory = view.findViewById(R.id.ll_ext_memory)

        tvExtMemoryPerc = view.findViewById(R.id.external_storage_perc)
        tvIntMemoryPerc = view.findViewById(R.id.internal_storage_perc)
        tvMemoryPerc = view.findViewById(R.id.ram_percentage)


        // Init
        val handler = Handler()
        val runnable = object : Runnable {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            override fun run() {
                showRAMUsage()
//                handler.postDelayed(this, 1000)
            }
        }

        handler.postDelayed(runnable, 0)
        setUpStorageDetails()
        return view
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && isAdded) {
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @SuppressLint("SetTextI18n")
    private fun setUpStorageDetails() {

        /** Internal Memory usage */
        val totalInternalValue = getTotalInternalMemorySize()
        val freeInternalValue = getAvailableInternalMemorySize()
        val usedInternalValue = totalInternalValue - freeInternalValue
        tvUsedIntMemory?.text = getString(R.string.used_memory) + "\t" + formatSize(usedInternalValue)
        tvFreeIntMemory?.text = getString(R.string.free) + "\t" + formatSize(freeInternalValue)
        tvTotalIntMemory?.text = getString(R.string.total) + "\t" + formatSize(totalInternalValue)

        donutInternalStorage?.percent = Methods.calculatePercentage(usedInternalValue.toDouble(), totalInternalValue.toDouble()).toFloat()
        tvIntMemoryPerc?.text = Methods.calculatePercentage(usedInternalValue.toDouble(), totalInternalValue.toDouble()).toString() + "%"
        if (Methods.getExternalMounts().size > 0) {
            val dirs: Array<File> = ContextCompat.getExternalFilesDirs(activity, null)
            llExtMemory?.visibility = View.VISIBLE
            /** External Memory usage */
            val totalExternalValue = getTotalExternalMemorySize(dirs)
            val freeExternalValue = getAvailableExternalMemorySize(dirs)
            val usedExternalValue = totalExternalValue - freeExternalValue
            tvUsedExtMemory?.text = getString(R.string.used_memory) + "\t" + formatSize(usedExternalValue)
            tvFreeExtMemory?.text = getString(R.string.free) + "\t" + formatSize(freeExternalValue)
            tvTotalExtMemory?.text = getString(R.string.used) + "\t" + formatSize(totalExternalValue)

            donutExternalStorage?.percent = df.format(Methods.calculatePercentage(usedExternalValue.toDouble(), totalExternalValue.toDouble())).toFloat()
            tvExtMemoryPerc?.text = df.format(Methods.calculatePercentage(usedExternalValue.toDouble(), totalExternalValue.toDouble())).toString() + "%"
        } else {
            llExtMemory?.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showRAMUsage() {
        /** RAM Usage count */
        val totalRamValue = totalRamMemorySize()
        val freeRamValue = freeRamMemorySize()
        val usedRamValue = totalRamValue - freeRamValue

        tvUsedMemory?.text = getString(R.string.used_memory) + "\t" + formatSize(usedRamValue)
        tvFreeMemory?.text = getString(R.string.free) + "\t" + formatSize(freeRamValue)
        tvTotalMemory?.text = getString(R.string.total) + "\t" + formatSize(totalRamValue)

        donutRAMUsage?.percent = Methods.calculatePercentage(usedRamValue.toDouble(), totalRamValue.toDouble()).toFloat()
        tvMemoryPerc?.text = Methods.calculatePercentage(usedRamValue.toDouble(), totalRamValue.toDouble()).toString() + "%"
    }

    private fun freeRamMemorySize(): Long {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)

        return mi.availMem
    }

    private fun totalRamMemorySize(): Long {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        return mi.totalMem
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun getAvailableInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return availableBlocks * blockSize
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun getTotalInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return totalBlocks * blockSize
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun getTotalExternalMemorySize(dirs: Array<File>): Long {
        return if (dirs.size > 1) {
            val stat = StatFs(dirs[1].path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            totalBlocks * blockSize
        } else {
            0
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun getAvailableExternalMemorySize(dirs: Array<File>): Long {
        return if (dirs.size > 1) {
            val stat = StatFs(dirs[1].path)
            val blockSize = stat.blockSizeLong
            val availableBlocks = stat.availableBlocksLong
            availableBlocks * blockSize
        } else {
            0
        }
    }

    private fun formatSize(size: Long): String {
        if (size <= 0)
            return "0"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }

    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }
}
