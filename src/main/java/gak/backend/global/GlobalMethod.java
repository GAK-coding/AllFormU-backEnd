package gak.backend.global;

import java.util.Random;

public class GlobalMethod {
    public String makeRandom(int num){
        StringBuffer rn = new StringBuffer();
        Random rand = new Random();

        //8자리 랜덤값 생성할거임.
        //a-z (ex. 1+97 => 'b')
        for(int i = 0; i < num ; i++){
            int index = rand.nextInt(3); //랜덤으로 숫자, 대문자, 소문자 고르는거

            switch(index){
                case 0:
                    rn.append((char)(rand.nextInt(26) + 65));
                    break;
                case 1:
                    rn.append((char)(rand.nextInt(26) + 97));
                    break;
                case 2:
                    rn.append(rand.nextInt(10));
                    break;
            }
        }
        return rn.toString();


    }

}
