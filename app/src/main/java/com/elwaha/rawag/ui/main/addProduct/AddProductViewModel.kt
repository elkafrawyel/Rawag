package com.elwaha.rawag.ui.main.addProduct

import androidx.lifecycle.ViewModel

class AddProductViewModel : ViewModel() {
    val days= ArrayList<String>()
    init {
        days.add("1")
        days.add("2")
        days.add("3")
        days.add("4")
        days.add("5")
    }
}
