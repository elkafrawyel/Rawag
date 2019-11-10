package com.elwaha.rawag.ui.main.profile.comments.report

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.ProblemModel

class ReportDialog(context: Context, val problems: ArrayList<ProblemModel>, iReportProblems: IReportProblems) :
    Dialog(context, R.style.CustomDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this@ReportDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.setCancelable(true)
        setContentView(R.layout.comment_report_dialog)
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

    }
}

public interface IReportProblems {
    fun getProblem(problemModel: ProblemModel)
}
