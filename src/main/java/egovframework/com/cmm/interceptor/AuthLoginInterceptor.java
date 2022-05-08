package egovframework.com.cmm.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//로그인처리를 담당하는 인터셉터
public class AuthLoginInterceptor extends HandlerInterceptorAdapter{

 // preHandle() : 컨트롤러보다 먼저 수행되는 메서드
 @Override
 public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
         throws Exception {
     // session 객체를 가져옴
     HttpSession session = request.getSession();
     // login처리를 담당하는 사용자 정보를 담고 있는 객체를 가져옴
     Object obj = session.getAttribute("login");
     boolean chkLogin = chkNoLogin(request);
     if ( !chkLogin &&obj == null ){
    	 //System.out.println("로그인폼으로 보낸다.");
         // 로그인이 안되어 있는 상태임으로 로그인 폼으로 다시 돌려보냄(redirect)
         response.sendRedirect("login.do");
         return false; // 더이상 컨트롤러 요청으로 가지 않도록 false로 반환함
     }
     
     if(obj!=null) {
    	 boolean chkAuth = chkAuth(request);
    	 if(!chkAuth) {
    		 session.removeAttribute("login"); // 기존값을 제거해 준다.
    		 session.removeAttribute("authList"); // 기존값을 제거해 준다.
    		 response.sendRedirect("login.do");
             return false; // 더이상 컨트롤러 요청으로 가지 않도록 false로 반환함
    	 }
    	 
     }
     // preHandle의 return은 컨트롤러 요청 uri로 가도 되냐 안되냐를 허가하는 의미임
     // 따라서 true로하면 컨트롤러 uri로 가게 됨.
     return true;
 }

 // 컨트롤러가 수행되고 화면이 보여지기 직전에 수행되는 메서드
 @Override
 public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
         ModelAndView modelAndView) throws Exception {
     // TODO Auto-generated method stub
     super.postHandle(request, response, handler, modelAndView);
 }
 
 //로그인 예외처리 메소드
 private boolean chkNoLogin(HttpServletRequest request){
	 Vector<String> exvec = new Vector<String>();
     boolean noLogin=false;
     //exvec.add("main.do");
     exvec.add("login.do");//여기에 예외경로 추가한다
	 exvec.add("loginPost.do");
	 exvec.add("logOut.do");
	 exvec.add("mem_login_ajax.do");
	 //exvec.add("mem_ajax.do");
	 
	 String url = request.getRequestURL().toString();
	 for(String str:exvec){
		 if(url.indexOf(str)>-1){
			 noLogin=true;
			 break;
		 }
	 }
     return noLogin;
 }
 private boolean chkAuth(HttpServletRequest request) {
	 boolean auth=true;
	 String url = request.getRequestURL().toString();
	 HttpSession session = request.getSession();
     // login처리를 담당하는 사용자 정보를 담고 있는 객체를 가져옴
	 List<?> authList = (List<?>)session.getAttribute("authList");
     if(authList!=null) {
    	 //System.out.println("authList size"+authList.size());
    	 for(Object obj:authList) {
    		 HashMap map =(HashMap)obj;
    		 String mu_method = map.get("mu_method")==null?"null":(String)map.get("mu_method");
    		 String au_list = map.get("au_list")==null?"N":(String)map.get("au_list");
    		 //System.out.println("url"+url+"mu_method===>"+mu_method);
    		 if(au_list.equals("N")&&url.indexOf(mu_method)>-1) {
    			 auth=false;
    			 break;
    		 }
    	 }
     }
     return auth;
 }
}

