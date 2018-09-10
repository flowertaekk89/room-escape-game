package sesoc.global.escape.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import sesoc.global.escape.vo.Users;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		Users loginUser = (Users) session.getAttribute("loginUser");
		if(loginUser == null){
			response.sendRedirect("/escape/");
			return false;
		}//if
		
		return super.preHandle(request, response, handler);
	}
}
