package application;

import javafx.scene.control.TextField;

/**
 * TextField에 값을 입력할 때 예외 발생 방지를 위해서 사용.
 * @author 정의철
 */
public class InputUtil {
    	/**
     * 텍스트 필드에 non-int 타입의 값이 입력되는 것을 방지한다.
     * @param textField 감지할 TextField
     * @param oldVal oldVal
     * @param newVal newVal
     */
    public static void detectString(TextField textField, String oldVal, String newVal) {

        if(newVal.length() == 0) return;    /* 아무것도 입력된 것이 없을 경우, 메소드 종료 */

		char[] charArray = newVal.toCharArray();
		if (charArray.length > 13) {
			// 13자리 넘지 못하게 함.
			textField.setText(oldVal);
		}
        for (char i : charArray) {
            if (!('0' <= i && i <= '9')) {
                // 정수 아닌 값이 입력되었을 경우, 전의 값으로 되돌아 감.
                textField.setText(oldVal);
            }
        }
    }
}