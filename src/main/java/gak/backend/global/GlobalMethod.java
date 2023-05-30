package gak.backend.global;

import java.util.Random;

public class GlobalMethod {
    Character[] special = {'!','@','#','$','%','^','&','*'};
    public String makeRandom(int num){
        StringBuffer rn = new StringBuffer();
        Random rand = new Random();

        //8자리 랜덤값 생성할거임.
        //a-z (ex. 1+97 => 'b')
        for(int i = 0; i < num ; i++){
            int index = 0;
            if(num==6) {
                index = rand.nextInt(3); //랜덤으로 숫자, 대문자, 소문자 고르는거(0~2)
            }
            if(num==8) {
                index = rand.nextInt(4);
            }

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
                case 3:
                    rn.append(special[rand.nextInt(8)]);//배열에 특수 문자를 넣어 놓고 랜덤값으로 접근.
                    break;

            }
        }
        return rn.toString();


    }

}
