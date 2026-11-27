package group;

import controller.group.GroupServlet;
import jakarta.servlet.http.*;
import model.dto.UserDTO;
import model.domain.User;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.*;
import static org.junit.jupiter.api.Assertions.*;

class GroupWebTest {
    private HttpClient client() {
        return HttpClient.newBuilder().cookieHandler(new CookieManager(null,CookiePolicy.ACCEPT_ALL)).build();
    }
    private HttpResponse<String> get(HttpClient c,String url) throws Exception {
        return c.send(HttpRequest.newBuilder(URI.create(url)).GET().build(),HttpResponse.BodyHandlers.ofString());
    }
    private HttpResponse<String> post(HttpClient c,String url,String token,String... fields) throws Exception {
        Map<String,String> form=new LinkedHashMap<>();form.put("csrf",token);form.put("groupId","100");form.put("month","2026-11");
        for(int i=0;i<fields.length;i+=2) form.put(fields[i],fields[i+1]);
        StringBuilder body = new StringBuilder();
        for (String name : form.keySet()) {
            if (body.length() > 0) {
                body.append("&");
            }
            body.append(URLEncoder.encode(name, StandardCharsets.UTF_8));
            body.append("=");
            body.append(URLEncoder.encode(form.get(name), StandardCharsets.UTF_8));
        }
        return c.send(HttpRequest.newBuilder(URI.create(url)).header("Content-Type","application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(body.toString())).build(),HttpResponse.BodyHandlers.ofString());
    }
    private String token(HttpResponse<String> response) {
        Matcher m=Pattern.compile("name=\"csrf\" value=\"([^\"]+)\"").matcher(response.body());
        assertTrue(m.find(),response.body());return m.group(1);
    }
    @Test void realServletAndJspFlow() throws Exception {
        GroupTestDatabase db=new GroupTestDatabase();Tomcat tomcat=new Tomcat();
        tomcat.setBaseDir(Files.createTempDirectory("roommate-group-web").toString());tomcat.setPort(0);tomcat.getConnector();
        Context ctx=tomcat.addWebapp("/roommateMatching",new File("src/main/webapp").getAbsolutePath());
        ctx.setParentClassLoader(getClass().getClassLoader());
        ((StandardJarScanner)ctx.getJarScanner()).setScanClassPath(false);
        ctx.getServletContext().setAttribute("group.service",db.service);
        Tomcat.addServlet(ctx,"group-test",new GroupServlet());
        for(String path:new String[]{"/app/group","/app/group/list","/app/matched","/app/group/role","/app/group/rule","/app/group/rule/agree","/app/group/leave","/app/group/leave/consent"})ctx.addServletMappingDecoded(path,"group-test");
        Tomcat.addServlet(ctx,"test-login",new HttpServlet() {
            private static final long serialVersionUID=1L;
            @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp) {
                UserDTO user=new UserDTO();user.setStudentId(Integer.parseInt(req.getParameter("id")));
                Object loginMember = new User(user);
                if ("dto".equals(req.getParameter("type"))) {
                    loginMember = user;
                }
                if ("string".equals(req.getParameter("type"))) {
                    loginMember = req.getParameter("id");
                }
                req.getSession(true).setAttribute("loginMember",loginMember);resp.setStatus(204);
            }
        });ctx.addServletMappingDecoded("/test-login","test-login");
        try {
            tomcat.start();String base="http://127.0.0.1:"+tomcat.getConnector().getLocalPort()+"/roommateMatching";
            HttpClient first=client(),second=client(),third=client(),outside=client();
            assertEquals(401,get(first,base+"/app/group?groupId=100").statusCode());
            HttpClient legacy = client();
            get(legacy, base + "/test-login?id=1&type=dto");
            assertEquals(200, get(legacy, base + "/app/group?groupId=100").statusCode());
            get(legacy, base + "/test-login?id=1&type=string");
            assertEquals(401, get(legacy, base + "/app/group?groupId=100").statusCode());
            get(first,base+"/test-login?id=1");get(second,base+"/test-login?id=2");get(third,base+"/test-login?id=3");get(outside,base+"/test-login?id=4");
            assertEquals(200,get(first,base+"/app/matched").statusCode());
            HttpResponse<String> page=get(first,base+"/app/group?groupId=100&month=2026-11");
            assertEquals(200,page.statusCode(),page.body());assertTrue(page.body().contains("우리 그룹"));String csrf=token(page);
            assertTrue(page.body().contains("역할 내용</label>"));
            assertTrue(page.body().contains("퇴실 사유 (선택)</label>"));
            assertTrue(page.body().contains("퇴실 요청 보내기</button>"));
            assertEquals(403,get(outside,base+"/app/group?groupId=100").statusCode());
            assertEquals(400,get(first,base+"/app/group?groupId=wrong").statusCode());
            assertEquals(405,get(first,base+"/app/group/role").statusCode());
            assertEquals(200,get(first,base+"/views/group/detail.jsp?groupId=100").statusCode());
            assertEquals(404,get(first,base+"/WEB-INF/views/group/detail.jsp").statusCode());
            assertEquals(403,post(first,base+"/app/group/role","bad","roleTitle","청소","assigneeId","2").statusCode());
            String attack="<script>alert('x')</script>";
            assertEquals(303,post(first,base+"/app/group/role",csrf,"roleTitle",attack,"assigneeId","2").statusCode());
            page=get(first,base+"/app/group?groupId=100&month=2026-11");
            assertEquals(200,page.statusCode(),page.body());assertFalse(page.body().contains(attack));assertTrue(page.body().contains("&lt;script&gt;"));
            assertEquals(303,post(first,base+"/app/group/rule",csrf,"content","소음 줄이기").statusCode());
            int rule=db.count("SELECT ruleid FROM rule");
            assertEquals(303,post(first,base+"/app/group/rule/agree",csrf,"ruleId",String.valueOf(rule)).statusCode());
            HttpResponse<String> invalid=post(first,base+"/app/group/role",csrf,"roleTitle","   ","assigneeId","2");
            assertEquals(400,invalid.statusCode());
            assertTrue(invalid.body().contains("id=\"form-retry\""));
            assertTrue(invalid.body().contains("data-dialog=\"role-new\""));
            assertTrue(invalid.body().contains("data-assignee-id=\"2\""));
            page=get(first,base+"/app/group?groupId=100");
            assertFalse(page.body().contains("공과금"));
            assertEquals(404,post(first,base+"/app/group/settle",csrf).statusCode());
            Files.writeString(new File("target/group-detail-test.html").toPath(),page.body());
            assertEquals(303,post(first,base+"/app/group/leave",csrf,"reason","이사").statusCode());
            HttpResponse<String> duplicate=post(first,base+"/app/group/leave",csrf,"reason","중복 요청");
            assertEquals(409,duplicate.statusCode());
            assertTrue(duplicate.body().contains("data-reason=\"중복 요청\""));
            assertEquals(1,db.count("SELECT COUNT(*) FROM leavegroup"));
            int leave=db.count("SELECT leaveid FROM leavegroup");
            String secondToken=token(get(second,base+"/app/group?groupId=100&month=2026-11"));
            String thirdToken=token(get(third,base+"/app/group?groupId=100&month=2026-11"));
            assertEquals(303,post(second,base+"/app/group/leave/consent",secondToken,"leaveId",String.valueOf(leave),"agreed","Y").statusCode());
            assertEquals(303,post(third,base+"/app/group/leave/consent",thirdToken,"leaveId",String.valueOf(leave),"agreed","Y").statusCode());
            assertEquals(403,get(first,base+"/app/group?groupId=100").statusCode());
            assertEquals(200,get(second,base+"/app/group?groupId=100&month=2026-11").statusCode());
            assertEquals(1,db.count("SELECT COUNT(*) FROM leavegroup WHERE status='APPROVED'"));
        } finally {tomcat.stop();tomcat.destroy();}
    }
}
