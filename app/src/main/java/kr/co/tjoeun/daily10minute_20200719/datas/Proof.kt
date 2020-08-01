package kr.co.tjoeun.daily10minute_20200719.datas

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Proof {

    var id = 0
    var content = ""
    val proofTime = Calendar.getInstance() // 인증 일시를 분석해서 셋팅 예정

    val imageUrlList = ArrayList<String>() // 이미지의 주소만 따서 목록으로 저장
//    좋아요 / 댓글 갯수 저장 변수
    var likeCount = 0
    var replyCount = 0

//    내가 좋아요를 찍어둔 게시글인가?
    var myLike = false


    lateinit var user : User // 인증을 올린 사람에 대한 정보.


    companion object{

//        적당한 json을 넣으면 => Proof 로 변환해주는 기능

        fun getProofFromJson(json : JSONObject) : Proof{
            val p = Proof()

//            재료로 들어오는 json을 가지고 => p의 멤버변수들에 내용을 채워주자
        p.id = json.getInt("id")
        p.content = json.getString("content")

//            서버에서는 인증 일시를 String으로 알려줌
//            앱에서는 인증일시를 Calendar로 저장해야함
//            String => Calendar 변환. SimpleDateFormat 필요.

        val proofTimeString = json.getString("proof_time")

        //            서버에서 내려주는 양식을 읽어올 SimpleDateFormat 생성
//            2020-06-09 02:53:34 양식 분석
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

//            sdf를 이용해서 => 캘린더변수에 시간 대입
        p.proofTime.time = sdf.parse(proofTimeString)

//            images JSONArry 내부의 => JSONObject 내부의 => img_url 들을 String으로 얻어내서
//            p.이미지주소목록에 저장

            val images = json.getJSONArray("images")

            for (i in 0 until images.length()){
//                i가 0에서부터 ~ 목록 길이 직전까지 증가

                val imageObj = images.getJSONObject(i)

//                imageObj 내부의 img_url String 을 얻어내서 => 목록에 저장
                val url = imageObj.getString("img_url")
                p.imageUrlList.add(url)
            }


//            인증 글 작성자 정보 파싱
//            user JSONObject 추출 => User 클래스 변환 => p.user에 저장

            val userObj = json.getJSONObject("user")
            val user = User.getUserFromJson(userObj)
            p.user = user

//            좋아요/댓글 갯수도 같이 파싱
            p.likeCount = json.getInt("like_count")
            p.replyCount = json.getInt("reply_count")

//            내가 좋아요를 찍은 글인지도 파싱
            p.myLike=json.getBoolean("my_like")

        return p
    }

    }

}
