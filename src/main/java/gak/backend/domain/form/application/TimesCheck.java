//package gak.backend.domain.form.application;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.TimerTask;
//public class TimesCheck extends TimerTask {
//    @Override
//    public void run(){
//        printLog();
//    }
//
//    public void printLog(){
//        //오늘 날짜로 초기화
//        Calendar nowTime = Calendar.getInstance();
//        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String strNowTime = sd.format(nowTime.getTime());
//
//        //String strNowTime = String.format("%1$tF %1$tT",nowTime); //2014-09-14 03:16:22
//        //String strNowTime = String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS",nowTime);//위코드와 결과동일
//        //System.out.println(strNowTime);
//
//        System.out.println("[ " + strNowTime + " ] : 로그 처리");
//    }
//}
