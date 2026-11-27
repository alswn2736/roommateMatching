package controller.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.UUID;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.domain.User;
import model.dto.UserDTO;
import model.service.UserService;

// 최초 프로필 설정 요청 처리 Controller
@WebServlet({"/app/onboarding", "/app/profile-image"})
@MultipartConfig(
    maxFileSize = 5 * 1024 * 1024,
    maxRequestSize = 6 * 1024 * 1024
)
public class OnboardingServlet extends HttpServlet {

    private final UserService userService = new UserService();

    private final Path uploadDir = Path.of(
            System.getProperty("user.home"),
            "roommate-uploads", "profile");

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("loginMember") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/app/login");
            return;
        }

        if ("/app/profile-image".equals(request.getServletPath())) {
            showImage(request, response);
            return;
        }

        request.getRequestDispatcher("/views/auth/onboarding.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("loginMember") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/app/login");
            return;
        }

        if (!"/app/onboarding".equals(request.getServletPath())) {
            response.sendError(405);
            return;
        }

        User loginMember = (User) session.getAttribute("loginMember");

        try {
            UserDTO profile = new UserDTO();

            profile.setStudentId(loginMember.getStudentId());

            profile.setNickname(request.getParameter("nickname"));
            profile.setName(request.getParameter("name"));
            profile.setMajor(request.getParameter("department"));

            profile.setNamePublic(
                    request.getParameter("namePublic") != null ? "Y" : "N");

            profile.setStudentIdPublic(
                    request.getParameter("studentPublic") != null ? "Y" : "N");

            profile.setMajorPublic(
                    request.getParameter("departmentPublic") != null ? "Y" : "N");

            String imagePath = saveImage(request.getPart("profileImage"));
            profile.setProfileImg(imagePath);

            User user;

            try {
                user = userService.makeProfile(profile);
            } catch (SQLException | RuntimeException e) {

                if (imagePath != null) {
                    String fileName = imagePath.substring(
                            imagePath.indexOf("?file=") + 6);
                    Files.deleteIfExists(uploadDir.resolve(fileName));
                }

                throw e;
            }

            session.setAttribute("loginMember", user);

            response.sendRedirect(
                    request.getContextPath() + "/app/feed");

        } catch (IllegalArgumentException e) {

            request.setAttribute("error", e.getMessage());
            doGet(request, response);

        } catch (IllegalStateException e) {

            request.setAttribute("error",
                    "이미지 용량이 너무 크거나 요청이 올바르지 않습니다.");
            doGet(request, response);

        } catch (SQLException | IOException | ServletException e) {

            log("프로필 설정 실패", e);

            request.setAttribute("error",
                    "프로필 저장 중 오류가 발생했습니다.");
            doGet(request, response);
        }
    }

    private String saveImage(Part part) throws IOException {

        if (part == null || part.getSize() == 0) {
            return null;
        }

        String type = part.getContentType();

        if (!"image/jpeg".equals(type) && !"image/png".equals(type)) {
            throw new IllegalArgumentException(
                    "JPG 또는 PNG 이미지만 선택해주세요.");
        }

        BufferedImage image;

        try (var input = part.getInputStream()) {
            image = ImageIO.read(input);
        }

        if (image == null) {
            throw new IllegalArgumentException(
                    "올바른 이미지 파일이 아닙니다.");
        }

        Files.createDirectories(uploadDir);

        String format = "image/png".equals(type) ? "png" : "jpg";
        String fileName = UUID.randomUUID() + "." + format;

        Path target = uploadDir.resolve(fileName);

        try {
            if (!ImageIO.write(image, format, target.toFile())) {
                throw new IOException("이미지 저장 실패");
            }
        } catch (IOException e) {
            Files.deleteIfExists(target);
            throw e;
        }

        return "/app/profile-image?file=" + fileName;
    }

    private void showImage(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String fileName = request.getParameter("file");

        if (fileName == null ||
                !fileName.matches("[a-f0-9-]{36}\\.(jpg|png)")) {
            response.sendError(404);
            return;
        }

        Path file = uploadDir.resolve(fileName);

        if (!Files.isRegularFile(file)) {
            response.sendError(404);
            return;
        }

        response.setContentType(
                fileName.endsWith(".png") ? "image/png" : "image/jpeg");

        response.setHeader("X-Content-Type-Options", "nosniff");

        Files.copy(file, response.getOutputStream());
    }
}