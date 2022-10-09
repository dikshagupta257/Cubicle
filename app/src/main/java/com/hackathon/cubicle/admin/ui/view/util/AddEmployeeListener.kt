package com.hackathon.cubicle.admin.ui.view.util

import com.hackathon.cubicle.data.EachEmployeeData

interface AddEmployeeListener {
    fun onAddButtonClicked(employee: EachEmployeeData)
}