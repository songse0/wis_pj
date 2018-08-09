package spring.trip.model.dao;

import spring.trip.domain.Member;

public interface MemberDAO {
	public boolean isEmailExist(String email); // 이메일 중복 확인

	public boolean isNicknameExist(String nickname); // 닉네임 중복 확인

	public int registerMember(Member vo); // 회원가입

	public boolean checkPassword(Member vo); // 비밀번호 맞는지 확인

	public int removeMember(String email); // 회원탈퇴

	public Member login(Member vo); // 로그인

	public Member showMemberInfo(String email); // 멤버 정보 조회

	public int updateMember(Member vo); // 멤버 정보 수정

	public String findEmail(String username, String nickname); // email 찾기

	public String findPassword(String email, String username, String nickname); // password	
	
	public int deletePictureUrl(String email) throws Exception;
}
