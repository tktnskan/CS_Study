/* 21-10-21 */

[java Date to String, String to Date 정리]

1. String to Date

String from = "2021-10-21 10:05:10"

SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//밀리초는 SSS 처럼 대문자로 해줘야 시간이 보존된다

Date to = format.parse(from);

2. Date to String

Date from  = new Date();

SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

String to = format.format(from);

3. 두 날짜의 간격 계산

.getTime() 을 통해서 Date포맷 -> MilliSeconds(long) 으로 변환
Long 을 가감하여 차이를 계산한다.

Date date = new Date();
Long crntDayTime = date.getTime();
Long nextDayTime = crntDayTime + 1000 * 60 * 60 * 24
// 하루 뒤의 날짜 Long (1000: 밀리세컨드 to 세컨드, 60 : 초, 60: 분, 24: 시간)

Date nextDayTimeDateFormat = format.format(nextDayTime);