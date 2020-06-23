package server.Enum;

/**
 * 클라이언트의 요청 사항 목록
 * @author 정의철
 *
 */
public enum RequsetType {
	/**
	 * 거래 진행
	 */
    TRANSACTION,
    /**
     * 자신의 계좌 목록 조회
     */
    GETMYACCOUNTLIST,
    /**
     * 자신의 거래 내역 조회
     */
    GETMYTRANSACTIONLIST,
    /**
     * 타인의 계좌 조회
     */
    SEARCHACCOUNT,
    /**
     * 거래진행 시, 유저의 비밀번호 체크
     */
    CHECKPASSWORD,
    /**
     * 연결 종료 요청
     */
    DISCONNECT;
}