package controller.mypage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.service.MyPageException;

/**
 * POST /mypage/profile/image (multipart, 파일 파라미터명 profileImg)
 * JPG/PNG, 5MB 이하만 허용. 파일은 {사용자 홈}/roommate-uploads/profile/ 에 저장하고
 * DB 에는 "/app/profile-image?file=UUID.jpg" 형식의 주소만 저장 (auth 최초 프로필 설정과 동일).
 */
public class ProfileImgCommand extends MyPageCommand {

    static final String IMAGE_URL = "/app/profile-image?file=";
    private static final long MAX_SIZE = 5L * 1024 * 1024;
    private static final String BACK = "redirect:/mypage/profile";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int me = loginId(request);

        Part part;
        try {
            part = request.getPart("profileImg");
        } catch (IllegalStateException e) { // @MultipartConfig 크기 제한 초과
            setFlash(request, "error", "5MB 이하의 파일만 업로드할 수 있습니다.");
            return BACK;
        } catch (Exception e) {
            setFlash(request, "error", "파일 업로드 형식이 올바르지 않습니다.");
            return BACK;
        }

        try {
            String ext = validate(part);
            String saved = save(part, ext);
            service.updateProfileImg(me, saved);
            setFlash(request, "success", "프로필 사진이 변경되었습니다.");
        } catch (MyPageException e) {
            setFlash(request, "error", e.getMessage());
        }
        return BACK;
    }

    /** 확장자/Content-Type/파일 시그니처를 모두 확인하고 저장할 확장자를 돌려준다 */
    private String validate(Part part) throws MyPageException, IOException {
        if (part == null || part.getSize() == 0) {
            throw new MyPageException("업로드할 사진을 선택해주세요.");
        }
        if (part.getSize() > MAX_SIZE) {
            throw new MyPageException("5MB 이하의 파일만 업로드할 수 있습니다.");
        }
        String fileName = part.getSubmittedFileName() == null ? "" : part.getSubmittedFileName().toLowerCase();
        String ext;
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            ext = ".jpg";
        } else if (fileName.endsWith(".png")) {
            ext = ".png";
        } else {
            throw new MyPageException("JPG, PNG 형식의 파일만 업로드할 수 있습니다.");
        }

        byte[] head = new byte[8];
        int n;
        try (InputStream in = part.getInputStream()) {
            n = in.readNBytes(head, 0, head.length);
        }
        boolean jpg = n >= 3 && (head[0] & 0xFF) == 0xFF && (head[1] & 0xFF) == 0xD8 && (head[2] & 0xFF) == 0xFF;
        boolean png = n >= 8 && (head[0] & 0xFF) == 0x89 && head[1] == 'P' && head[2] == 'N' && head[3] == 'G';
        if ((".jpg".equals(ext) && !jpg) || (".png".equals(ext) && !png)) {
            throw new MyPageException("올바른 이미지 파일이 아닙니다.");
        }
        return ext;
    }

    /**
     * 최초 프로필 설정(auth OnboardingServlet)과 같은 폴더·같은 주소 형식으로 저장한다.
     * 이미지는 /app/profile-image?file=... 로 보여지며, 서버를 재배포해도 지워지지 않는다.
     */
    private String save(Part part, String ext) throws MyPageException, IOException {
        Path dir = Path.of(System.getProperty("user.home"), "roommate-uploads", "profile");
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new MyPageException("서버에 업로드 폴더를 만들 수 없습니다.");
        }

        // 파일명은 서버에서 생성 (사용자가 보낸 이름은 사용하지 않음). 형식: UUID.jpg / UUID.png
        String fileName = UUID.randomUUID() + ext;
        Path target = dir.resolve(fileName);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return IMAGE_URL + fileName;
    }
}
