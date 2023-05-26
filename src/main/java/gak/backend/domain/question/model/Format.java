package gak.backend.domain.question.model;

/*
### 문장형 : Description_x
---
단답형(short ), 장문형, 날짜(date), 시간(time), 파일 업로드(image)
### 선택형 : Selection_x
---
객관식(selection)_객관식은 그대로 Selection 할거임, 체크박스(checkBox), 드롭 다운(dropDown), 선형 배열
### 그리드 : Grid_x
---
객관식 그리드(radio), 체크 박스 그리드(checkBox)
 */
public enum Format {
    //질문 형식 들어감
    Description_SHORT, Description_LONG, Description_DATE, Description_TIME, Description_IMG,
    Selection_OPTION, Selection_CHECKBOX, Selection_DROPDOWN, Selection_LINEAR,
    Grid_RADIO, Grid_CHECKBOX
}
