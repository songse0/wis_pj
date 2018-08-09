package spring.trip.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import spring.trip.domain.Member;
import spring.trip.model.service.MemberService;

@Controller
public class MemberContoller extends MultiActionController {

   // field
   @Autowired
   private MemberService memberService;

   // isEmailExist
   @RequestMapping("isEmailExist.do")
   public ModelAndView isEmailExist(HttpServletRequest request, HttpServletResponse response) throws Exception {
      // form 값 받기
      String email = request.getParameter("email");

      // B.L.
      boolean foundEmail = memberService.isEmailExist(email);

      // Navi
      return new ModelAndView("JsonView", "found", foundEmail);
   }

   // isNicknameExist
   @RequestMapping("isNicknameExist.do")
   public ModelAndView isNicknameExist(HttpServletRequest request, HttpServletResponse response) throws Exception {
      String nickname = request.getParameter("nickname");

      boolean foundNick = memberService.isNicknameExist(nickname);

      return new ModelAndView("JsonView", "found", foundNick);
   }

   // moveTocheckPassForUpdate
   @RequestMapping("moveTocheckPassForUpdate.do")
   public ModelAndView moveTocheckPassForUpdate(HttpServletRequest request, HttpServletResponse response,
         HttpSession session) throws Exception {
      Member vo = (Member) session.getAttribute("mvo");
      String email = vo.getEmail();
      System.out.println(vo + "\n" + email);
      return new ModelAndView("member/checkPasswordForUpdateMember", "email", email);
   }

   // moveTocheckPassForRemove
   @RequestMapping("moveTocheckPassForRemove.do")
   public ModelAndView moveTocheckPassForRemove(HttpServletRequest request, HttpServletResponse response,
         HttpSession session) throws Exception {
      Member vo = (Member) session.getAttribute("mvo");
      String email = vo.getEmail();
      System.out.println(vo + "\n" + email);
      return new ModelAndView("member/checkPasswordForRemoveMember", "email", email);
   }

   // registerMember
   @RequestMapping("registerMember.do")
   public ModelAndView registerMember(HttpServletRequest request, HttpServletResponse response, Member pvo)
         throws Exception {
      System.out.println("회원가입 들어옴!!");
      System.out.println(pvo);
      System.out.println("1");
      memberService.uploadMemberPhoto(pvo);
      System.out.println("2");
      System.out.println(pvo);
      memberService.registerMember(pvo);
      System.out.println(pvo);
      System.out.println("3");
      System.out.println(pvo.getMemberPictureUrl());
      System.out.println("getUploadFile=="+pvo.getUploadFile());
      System.out.println("isEmpty()=="+pvo.getUploadFile().isEmpty());
      
      return new ModelAndView("index", "nickname", pvo.getNickname());
   }

   // checkPasswordForUpdate
   @RequestMapping("checkPasswordForUpdate.do")
   public ModelAndView checkPasswordForUpdate(HttpServletRequest request, HttpServletResponse response)
         throws Exception {
      String password = request.getParameter("password");
      String email = request.getParameter("email");

      System.out.println("PW : " + password + ", EMAIL : " + email);

      Member pvo = new Member();
      pvo.setPassword(password);
      pvo.setEmail(email);

      boolean passMatch = memberService.checkPassword(pvo);

      if (passMatch == false) { // 비번 일치 안 함
         request.setAttribute("email", email);
         return new ModelAndView("member/checkPasswordForUpdateMember", "p", false);
      } else { // 비번 일치함(수정 가능)
         Member rvo = memberService.showMemberInfo(pvo.getEmail());
         return new ModelAndView("member/updateMember", "mvo", rvo);
      }
   }

   // checkPasswordForRemove
   @RequestMapping("checkPasswordForRemove.do")
   public ModelAndView checkPasswordForRemove(HttpServletRequest request, HttpServletResponse response)
         throws Exception {
      String password = request.getParameter("password");
      String email = request.getParameter("email");

      System.out.println("PW : " + password + ", EMAIL : " + email);

      Member pvo = new Member();
      pvo.setPassword(password);
      pvo.setEmail(email);

      boolean passMatch = memberService.checkPassword(pvo);
      System.out.println(passMatch);

      if (passMatch == false) { // 비번 일치 안 함
         request.setAttribute("email", email);
         return new ModelAndView("member/checkPasswordForRemoveMember", "p", false);
      } else { // 비번 일치함(수정 가능)
         return new ModelAndView("redirect:/removeMember.do", "email", email);
      }
   }

   // removeMember
   @RequestMapping("removeMember.do")
   public ModelAndView removeMember(HttpServletRequest request, HttpServletResponse response) throws Exception {
      // Form 값 받기
      String email = request.getParameter("email");


      // 멤버 삭제
      memberService.removeMember(email);
      request.getSession().invalidate();
      return new ModelAndView("index", "remove", true);
   }

   // login
   @RequestMapping("login.do")
   public ModelAndView login(HttpServletRequest request, HttpServletResponse response, HttpSession session, Member pvo)
         throws Exception {
      // form 값 받기
      /*
       * String email = request.getParameter("email"); String password =
       * request.getParameter("password");
       * 
       * Member pvo = new Member(); pvo.setEmail(email);
       * pvo.setPassword(password);
       */
      System.out.println("login.do 도착");
      System.out.println("loging form 내용=="+pvo);
      
      Member rvo = memberService.login(pvo);
      System.out.println("loging 후 결과 =="+rvo);
      if (rvo != null) { // 로그인 성공
         /*
          * HttpSession session = request.getSession();
          * session.setAttribute("mvo", rvo);
          */
         // request.getSession().setAttribute("mvo", rvo);
         session.setAttribute("mvo", rvo);
         System.out.println("loging 성공 후 session 정보=="+session.getAttribute("mvo"));
         return new ModelAndView("redirect:/getMyBookList.do");
      } else { // 로그인 실패
         return new ModelAndView("index", "login", false);
      }
   }

   // logout
   @RequestMapping("logout.do")
   public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, HttpSession session)
         throws Exception {
      // HttpSession session = request.getSession();
      session.invalidate();
      return new ModelAndView("index");
   }

   // showMemberInfo
   @RequestMapping("showMemberInfo.do")
   public ModelAndView showMemberInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
      // form 값 받기
      String email = request.getParameter("email");

      // Business Logic
      Member rvo = memberService.showMemberInfo(email);

      // Navi, Data binding
      return new ModelAndView("member/showMemberInfo", "mvo", rvo);
   }

   // updateMember
   @RequestMapping("updateMember.do")
   public ModelAndView updateMember(HttpServletRequest request, HttpServletResponse response, Member pvo)
         throws Exception {
      // form 값 받기
      memberService.uploadMemberPhoto(pvo);
      memberService.updateMember(pvo);

      HttpSession session = request.getSession();
      session.setAttribute("mvo", pvo);
      return new ModelAndView("redirect:/showMemberInfo.do?email=" + pvo.getEmail());
   }
   
	@RequestMapping("findEmail.do")
	public ModelAndView findEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String nickname = request.getParameter("nickname");
		String email = memberService.findEmail(username, nickname);
		
		return new ModelAndView("JsonView","email",email);
	}
	
	@RequestMapping("findPassword.do")
	public ModelAndView findPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String nickname = request.getParameter("nickname");
		String password = memberService.findPassword(email, username, nickname);
		
		Map map = new HashMap();
		map.put("nickname", nickname);
		map.put("password", password);
		
		return new ModelAndView("JsonView",map);
	}
	
}