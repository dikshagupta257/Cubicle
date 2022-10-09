package com.hackathon.cubicle.employee.data.model

data class TaskModel(
    var title:String = "",
    var description: String = "",
    var category: String = "",
    var date: Long = 0L,
    var time:Long = 0L,
    var timeTaken:Long = 0L
)
