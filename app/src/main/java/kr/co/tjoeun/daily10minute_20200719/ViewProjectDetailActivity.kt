package kr.co.tjoeun.daily10minute_20200719

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_view_project_detail.*
import kr.co.tjoeun.daily10minute_20200719.datas.Project
import kr.co.tjoeun.daily10minute_20200719.utils.ServerUtil
import org.json.JSONObject

class ViewProjectDetailActivity : BaseActivity() {

//    이 화면에서 보여줄 프로젝트의 id값
    var mProjectId = 0

//    이 화면에서 보여줄 프로젝트 자체 변수
    lateinit var mProject : Project


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_project_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        uploadProofBtn.setOnClickListener {

//            인증등록하기 화면에서도 어떤 프로젝트에 대한 인증인지 표시
//            프로젝트의 id값 + 프로젝트의 제목을 둘다 전달해서 단순히 표시

            val myIntent = Intent(mContext, EditProjectProofActivity::class.java)
            myIntent.putExtra("projectId", mProjectId)
            myIntent.putExtra("projectTitle", mProject.title)
            startActivity(myIntent)
        }

//        다른 사람들의 참여 인증 확인하는 버튼
        viewOtherProofBtn.setOnClickListener {

//            인증 확인화면 진입 => 어떤 프로젝트에 대해서 인지
//            id값만 인증 확인화면에 전달.

            val myIntent = Intent(mContext, ViewProjectProofListActivity::class.java)
            myIntent.putExtra("projectId", mProjectId)
            startActivity(myIntent)

        }

        joinProjectBtn.setOnClickListener {

//            정말 참여할건지? 물어보자.

            val alert = AlertDialog.Builder(mContext)
            alert.setTitle("프로젝트 참여 신청")
            alert.setMessage("정말 프로젝트에 참여하시겠습니까?")
            alert.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->

//                실제로 서버에 참여 신청
                ServerUtil.postRequestProjectJoin(mContext, mProjectId, object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(json: JSONObject) {

//                        신청에 성공했을때만 데이터 갱신

                        val code = json.getInt("code")

                        if (code == 200) {
                            val data = json.getJSONObject("data")
                            val projectObj = data.getJSONObject("project")

//                        갱신된 프로젝트 정보를 새로 대입
                            mProject = Project.getProjectFromJson(projectObj)

//                        별도 기능으로 만들어진 Ui 데이터 세팅 기능 실행
                            setProjectDataToUI()

//                            서버가 최신 정보를 못내려줌 => 강제로 다시 불러오자. (임시방편)
                            getProjectDetailFromServer()

//                            토스트로 참여 신청 성공 메세지 출력
                            runOnUiThread {
                                Toast.makeText(mContext, "프로젝트에 참가 했습니다.", Toast.LENGTH_SHORT).show()
                            }

                        }
                        else {
                            val message = json.getString("message")

                            runOnUiThread {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                            }

                        }



                    }

                })

            })
            alert.setNegativeButton("아니오", null)
            alert.show()

        }

        viewOngoingUsersBtn.setOnClickListener {

//            프로젝트별 참여 명부 화면으로 이동 => 명단 확인

            val myIntent = Intent(mContext, ViewOngoingUsersActivity::class.java)

            myIntent.putExtra("projectId", mProjectId)

            startActivity(myIntent)


        }

    }

    override fun setValues() {

        mProjectId = intent.getIntExtra("projectId", 0)

        getProjectDetailFromServer()



    }

//    프로젝트 상세 정보를 불러오는 기능

    fun getProjectDetailFromServer() {

        ServerUtil.getRequestProjectDetail(mContext, mProjectId, object : ServerUtil.JsonResponseHandler {

            override fun onResponse(json: JSONObject) {

                val data = json.getJSONObject("data")

                val projectObj = data.getJSONObject("project")

//                projectObj로 Project 형태로 변환 => 멤버변수로 저장
                mProject = Project.getProjectFromJson(projectObj)

//                데이터 화면 반영 기능 실행
                setProjectDataToUI()

            }

        })

    }

//    mProject에 세팅된 데이터를 화면에 반영하는 기능 별도 분리

    fun setProjectDataToUI() {

//                프로젝트 정보를 화면에 반영

        runOnUiThread {

            Glide.with(mContext).load(mProject.imageUrl).into(projectImg)

            projectTitleTxt.text = mProject.title
            projectDescriptionTxt.text = mProject.description

            proofMethodTxt.text = mProject.proofMethod
            challengerCountTxt.text = "${mProject.ongoingUserCount}명 도전 진행중"

//            참여 중 / 아니냐 에 따라 보여지는 버튼이 다르게 하자

            if (mProject.myLastStatus == "ONGOING") {
//                참여 중 버튼들 표시, 참가 버튼 숨기기
                ongoingButtonLayout.visibility = View.VISIBLE
                joinProjectBtn.visibility = View.GONE

//                진행률 레이아웃 표시 (VISIBLE)
                progressLayout.visibility = View.VISIBLE

//                진행률 계산해서 표시하기
//                진행률을 변수로 저장
                val progress = mProject.proofCount.toDouble() / mProject.completeDays.toDouble() * 100
//                계산된 진행률을 소수점 2자리만 표기하는 String으로 변경
                val progressStr = "%.2f".format(progress)
                progressTxt.text = "${progressStr}% (${mProject.proofCount}/${mProject.completeDays})"

            }
            else {
                ongoingButtonLayout.visibility = View.GONE
                joinProjectBtn.visibility = View.VISIBLE

//                진행률 레이아웃 숨기기 (GONE)
                progressLayout.visibility = View.GONE
            }

//

        }
    }

}