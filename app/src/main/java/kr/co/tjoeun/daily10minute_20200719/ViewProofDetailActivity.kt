package kr.co.tjoeun.daily10minute_20200719

import android.os.Bundle

class ViewProofDetailActivity : BaseActivity() {

//    목록에서 넘겨주는 인증글의 id 값
    var mProofId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_proof_detail)

        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        mProofId = intent.getIntExtra("proof_id", 0)

        getProofDataServer()
    }

    fun getProofDataServer() {
//        GET - /project_proof/id 값 API 호출
    }
}