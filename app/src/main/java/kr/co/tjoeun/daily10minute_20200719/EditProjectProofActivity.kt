package kr.co.tjoeun.daily10minute_20200719

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_edit_project_proof.*
import kr.co.tjoeun.daily10minute_20200719.datas.Project

class EditProjectProofActivity : BaseActivity() {

//    어떤 프로젝트에 대해 인증할지 구별하는 id 값
    var mProjectId = 0

    lateinit var mProject : Project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_project_proof)

        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        mProjectId = intent.getIntExtra("projectId", 0)

//        프로젝트의 제목은 intent가 넘겨주는 내용을 그대로 표시
        projectTitleTxt.text = intent.getStringExtra("projectTitle")

    }
}