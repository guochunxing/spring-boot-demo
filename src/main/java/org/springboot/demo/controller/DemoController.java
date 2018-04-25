package org.springboot.demo.controller;

import org.springboot.demo.service.demo.DemoService;
import org.springboot.demo.service.mq.MQSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class DemoController {

    @Resource
    private DemoService demoService;
    @Resource
    private MQSender sender;

    @RequestMapping("/demoCache")
    public String demoCache() throws InterruptedException {
        return demoService.demoCache();
    }

    @RequestMapping("/sender")
    public String sender() {
        return sender.send();
    }

    @RequestMapping("/api/authorized")
    @PreAuthorize("hasAuthority('test')")
    public String authorized(HttpServletRequest request) {
        System.out.println(request.getSession().getId());
        return "Hello Secured World";
    }

    @RequestMapping("/api/user")
    public String admin() {
        return "Hello Secured World user";
    }

    @PreAuthorize("hasAnyRole('user')")
    @RequestMapping("/test/cookie")
    public String cookie(@RequestParam("browser") String browser, HttpServletRequest request, HttpSession session) {
        //取出session中的browser
        Object sessionBrowser = session.getAttribute("browser");
        if (sessionBrowser == null) {
            System.out.println("不存在session，设置browser=" + browser);
            session.setAttribute("browser", browser);
        } else {
            System.out.println("存在session，browser=" + sessionBrowser.toString());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + " : " + cookie.getValue());
            }
        }
        return "index";
    }
}
