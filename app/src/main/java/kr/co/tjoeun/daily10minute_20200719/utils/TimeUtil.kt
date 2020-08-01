package kr.co.tjoeun.daily10minute_20200719.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class TimeUtil {

    companion object{

//        시간을 재료로 넣으면 시차를 보정해서 => 상황에 맞는 문구로 알려주는 기능

        fun getTimeAgoStringFromCalendar(cal :Calendar) : String{
//            cal 을 => 핸드폰에 설정된 시간에 맞는 시차 계산
//            cal 내부에는, 어느 시간대 (서울, 영국, 뉴욕..)인지 정보가 저장되어있다.
//            시간 값만 서버에서 준 그대로 받아적어둔 상태.

//            핸드폰의 시간대와 => 표준시간대의 시차를 구해서
//            cal 의 시간을 보정해주자.

            val myPhoneTimeZone = cal.timeZone // 한국폰 : 한국시간대...

//            표준시간대 (UTC / GMT +00:00) 과의 몇시간 차이가 나는지 계산
//            rawOffset : 시차를 밀리초 단위까지 기록. => 시간단위로 변환
            val timeOffset = myPhoneTimeZone.rawOffset / 1000 / 60 / 60

//            재료로 들어온 cal 에게 시간값을 ( 시차만큼 ) 증가시키자
            cal.add(Calendar.HOUR, timeOffset)

//            양식을 가공해서 String으로 리턴
//            상황에 따라 다른 문구를 리턴하자
//            10초 이내 : 방금 전
//            1분 이내 : ?초전
//            1시간 이내 : ?분전
//            12시간 이내 : ?시간 전
//            그 이상 : 양식으로 가공

//            현재 시간이 몇시인지 알아야함 => Calendar를 새로 만들면 => 현재시간 기록되어있다.
            val now = Calendar.getInstance()

//            현재 시간 - 재료로 들어오는 시간 => 간격이 얼마나 되는가?
//            계산결과 : 1800000 등의 숫자로 나옴.
//            1800000밀리초 : 1800초 : 30분

            val msDiff = now.timeInMillis - cal.timeInMillis
            Log.d("시간차이값 : ", msDiff.toString())

            if (msDiff < 10 * 1000 ) { //10초보다 작다면?
                return "방금 전"
            }else if(msDiff < 1 * 60 * 1000){  // 1분 보다 작다면?
                val second = msDiff / 1000
                return "${second}초 전"
            }else if(msDiff < 1 * 60 * 60 * 1000){  // 1시간 보다 작다면?
                val minute = msDiff / 1000 / 60
                return "${minute}분 전"
            }else if(msDiff < 12 * 60 * 60 * 1000){  // 12시간 보다 작다면?
                val hour = msDiff / 1000 / 60 / 60
                return "${hour}시간 전"
            }else{
                val sdf = SimpleDateFormat("yyyy년 M월 d일 a h시 m분")
                return sdf.format(cal.time)
            }


        }

    }

}