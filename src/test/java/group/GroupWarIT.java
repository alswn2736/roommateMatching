package group;

import java.io.File;
import java.net.URI;
import java.net.http.*;
import java.nio.file.Files;
import java.util.zip.ZipFile;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GroupWarIT {
    @Test void packagedWarRegistersServletAndExcludesTestLogin() throws Exception {
        File war=new File(System.getProperty("group.warPath"));assertTrue(war.isFile());
        try(ZipFile zip=new ZipFile(war)) {
            assertNotNull(zip.getEntry("WEB-INF/classes/controller/group/GroupServlet.class"));
            assertNotNull(zip.getEntry("WEB-INF/classes/mybatis/group/GroupMapper.xml"));
            assertNotNull(zip.getEntry("WEB-INF/classes/mybatis/group/config.xml"));
            assertNotNull(zip.getEntry("WEB-INF/classes/util/mybatis-config.xml"));
            assertNotNull(zip.getEntry("WEB-INF/classes/model/mapper/MyPageMapper.xml"));
            assertNotNull(zip.getEntry("WEB-INF/classes/mybatis/config/auth-mybatis-config.xml"));
            assertNull(zip.getEntry("WEB-INF/classes/group/GroupTestDatabase.class"));
            int jstlApiCount = 0;
            int jstlCount = 0;
            java.util.Enumeration<? extends java.util.zip.ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                assertFalse(name.startsWith("WEB-INF/lib/jakarta.servlet-api-"));
                assertFalse(name.startsWith("WEB-INF/lib/tomcat-embed-"));
                assertFalse(name.contains("h2-2.") || name.contains("junit-"));
                if (name.startsWith("WEB-INF/lib/jakarta.servlet.jsp.jstl-api-") && name.endsWith(".jar")) {
                    jstlApiCount++;
                } else if (name.startsWith("WEB-INF/lib/jakarta.servlet.jsp.jstl-") && name.endsWith(".jar")) {
                    jstlCount++;
                }
            }
            assertEquals(1, jstlApiCount);
            assertEquals(1, jstlCount);
        }
        Tomcat tomcat=new Tomcat();java.nio.file.Path baseDir=Files.createTempDirectory("group-war");
        Files.createDirectories(baseDir.resolve("webapps"));tomcat.setBaseDir(baseDir.toString());
        tomcat.getHost().setAppBase(baseDir.resolve("webapps").toString());
        tomcat.setPort(0);tomcat.getConnector();
        tomcat.addWebapp("/roommateMatching",war.getAbsolutePath());
        try {
            tomcat.start();String base="http://127.0.0.1:"+tomcat.getConnector().getLocalPort()+"/roommateMatching";
            try(HttpClient client=HttpClient.newHttpClient()) {
                HttpResponse<String> page=client.send(HttpRequest.newBuilder(URI.create(base+"/app/group?groupId=100")).GET().build(),HttpResponse.BodyHandlers.ofString());
                assertEquals(401,page.statusCode(),page.body());assertTrue(page.body().contains("로그인"));
                assertEquals(404,client.send(HttpRequest.newBuilder(URI.create(base+"/test-login?id=1")).GET().build(),HttpResponse.BodyHandlers.ofString()).statusCode());
            }
        } finally {tomcat.stop();tomcat.destroy();}
    }
}
