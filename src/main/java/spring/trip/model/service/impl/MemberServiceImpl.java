package spring.trip.model.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import spring.trip.domain.Member;
import spring.trip.model.dao.MemberDAO;
import spring.trip.model.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService{
	
	// field
	@Autowired
	private MemberDAO memberDAO;
	private String path = "C:\\Users\\ram\\Desktop\\ram_Develop\\eclipse\\workspace\\GripTrip\\src\\main\\webapp\\upload\\";
	
	@Override
	public boolean isEmailExist(String email) {
		return memberDAO.isEmailExist(email);
	}

	@Override
	public boolean isNicknameExist(String nickname) {
		return memberDAO.isNicknameExist(nickname);
	}

	@Override
	public int registerMember(Member vo) throws Exception {
		return memberDAO.registerMember(vo);
	}

	@Override
	public boolean checkPassword(Member vo) {
		return memberDAO.checkPassword(vo);
	}

	@Override
	public int removeMember(String email) {
		int row= memberDAO.removeMember(email);
		return row;
	}

	@Override
	public Member login(Member vo) {
		Member rvo = memberDAO.login(vo);
		return rvo;
	}

	@Override
	public Member showMemberInfo(String email) {
		Member vo = memberDAO.showMemberInfo(email);
		return vo;
	}

	@Override
	public int updateMember(Member vo) throws Exception{
		int row= memberDAO.updateMember(vo);
		return row;
	}
	
	@Override
	public String findEmail(String username, String nickname) {
		// TODO Auto-generated method stub
		return memberDAO.findEmail(username, nickname);
	}

	@Override
	public String findPassword(String email, String username, String nickname) {
		// TODO Auto-generated method stub
		return memberDAO.findPassword(email, username, nickname);
	}
	
	@Override
	   public void uploadMemberPhoto(Member vo) throws Exception {
	      MultipartFile mFile = vo.getUploadFile();
	      
	      System.out.println("name"+ mFile);
	      if(mFile!=null){
	         if(mFile.getOriginalFilename()!=""){
	            String fileName = mFile.getOriginalFilename();
	         
	            String newFileName = System.currentTimeMillis()+"_"+mFile.getOriginalFilename();
	            vo.setMemberPictureUrl(newFileName);
	            
	            File copyFile = new File(path+newFileName);
	            mFile.transferTo(copyFile);
	            System.out.println("uploadMemberPhoto" + vo.getMemberPictureUrl());
	         }
	      }
	   }

	@Override
	public int deletePictureUrl(String email) throws Exception {
		return memberDAO.deletePictureUrl(email);
	}

	@Override
	public void memberFileDelete(String memberPictureUrl) throws Exception {
		File f = new File(path+memberPictureUrl);
		System.out.println(memberPictureUrl+" file Delete!"+f.delete());	
	}
}
