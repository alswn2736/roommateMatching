package group;

import controller.group.GroupServlet;
import jakarta.servlet.http.*;
import model.dto.UserDTO;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class GroupDemo {
    public static void main(String[] args) throws Exception {
        GroupTestDatabase db=new GroupTestDatabase();Tomcat tomcat=new Tomcat();
        tomcat.setBaseDir(Files.createTempDirectory("group-local-demo").toString());tomcat.setPort(Integer.getInteger("group.demo.port",18784));
        tomcat.getConnector().setProperty("address","127.0.0.1");
        Context ctx=tomcat.addWebapp("/roommateMatching",new File("src/main/webapp").getAbsolutePath());
        ctx.setParentClassLoader(GroupDemo.class.getClassLoader());((StandardJarScanner)ctx.getJarScanner()).setScanClassPath(false);
        ctx.getServletContext().setAttribute("group.service",db.service);
        Tomcat.addServlet(ctx,"group",new GroupServlet());
        for(String path:new String[]{"/app/group","/app/group/list","/app/matched","/app/group/role","/app/group/rule","/app/group/rule/agree","/app/group/leave","/app/group/leave/consent"})ctx.addServletMappingDecoded(path,"group");
        Tomcat.addServlet(ctx,"local-demo",new HttpServlet() {
            private static final long serialVersionUID=1L;
            @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException {
                String id=req.getParameter("id");
                if(id!=null && id.matches("[1-4]")) {
                    HttpSession old=req.getSession(false);if(old!=null)old.invalidate();
                    UserDTO user=new UserDTO();user.setStudentId(Integer.parseInt(id));req.getSession(true).setAttribute("loginMember",user);
                    resp.sendRedirect(req.getContextPath()+"/app/matched");return;
                }
                resp.setContentType("text/html;charset=UTF-8");
                resp.getWriter().println("<!doctype html><html lang='ko'><meta charset='UTF-8'><title>그룹 로컬 데모</title><h1>그룹 로컬 데모</h1><p>별도 메모리 DB. 팀 Oracle 데이터와 관계없음!</p><p>각 구성원으로 전환해 퇴실 동의 확인.</p><ul><li><a href='?id=1'>첫째</a></li><li><a href='?id=2'>둘째</a></li><li><a href='?id=3'>셋째</a></li><li><a href='?id=4'>다른 그룹 회원</a></li></ul></html>");
            }
        });ctx.addServletMappingDecoded("/local-demo","local-demo");
        Runtime.getRuntime().addShutdownHook(new TomcatShutdown(tomcat));
        tomcat.start();System.out.println("LOCAL DEMO: http://127.0.0.1:"+tomcat.getConnector().getLocalPort()+"/roommateMatching/local-demo");tomcat.getServer().await();
    }
    private static class TomcatShutdown extends Thread {
        private final Tomcat tomcat;

        TomcatShutdown(Tomcat tomcat) {
            this.tomcat = tomcat;
        }

        @Override
        public void run() {
            try {
                tomcat.stop();
                tomcat.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
