package model.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import model.dao.UserDAO;
import model.domain.User;
import model.dto.UserDTO;
import model.util.auth.AuthDBUtil;
import model.util.auth.PasswordUtil;
import java.nio.charset.StandardCharsets;

//회원가입, 로그인, 프로필 설정을 처리하는 Manager 클래스
public class UserService {

    public User signup(String email, String password,
            String passwordConfirm) throws SQLException {

        email = normalizeEmail(email);

        int studentId = Integer.parseInt(email.split("@")[0]);

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "비밀번호는 8자리 이상 입력해주세요.");
        }

        if (!password.equals(passwordConfirm)) {
            throw new IllegalArgumentException(
                    "비밀번호가 일치하지 않습니다.");
        }

        try (Connection conn = AuthDBUtil.getConnection()) {

            UserDAO dao = new UserDAO(conn);

            if (dao.existsByEmail(email)) {
                throw new IllegalArgumentException(
                        "이미 가입된 이메일입니다.");
            }

            if (dao.selectById(studentId) != null) {
                throw new IllegalArgumentException(
                        "이미 가입된 학번입니다.");
            }

            UserDTO dto = new UserDTO();

            dto.setStudentId(studentId);
            dto.setEmail(email);
            dto.setPassword(PasswordUtil.hash(password));

            dto.setNamePublic("N");
            dto.setStudentIdPublic("N");
            dto.setMajorPublic("N");

            try {
                if (dao.insert(dto) != 1) {
                    throw new SQLException("회원가입에 실패했습니다.");
                }
            } catch (SQLException e) {

                if (e.getErrorCode() == 1) {
                    throw new IllegalArgumentException(
                            "이미 가입된 회원입니다.");
                }

                throw e;
            }

            return new User(dto);
        }
    }

    public User login(String email, String password)
            throws SQLException {

        email = normalizeEmail(email);

        try (Connection conn = AuthDBUtil.getConnection()) {

            UserDAO dao = new UserDAO(conn);

            UserDTO dto = dao.selectByEmail(email);

            if (dto == null ||
                    !PasswordUtil.matches(password, dto.getPassword())) {

                throw new IllegalArgumentException(
                        "이메일 또는 비밀번호가 올바르지 않습니다.");
            }

            return new User(dto);
        }
    }

    public User getProfile(int studentId) throws SQLException {

        try (Connection conn = AuthDBUtil.getConnection()) {

            UserDAO dao = new UserDAO(conn);
            UserDTO dto = dao.selectById(studentId);

            return dto == null ? null : new User(dto);
        }
    }

    public User makeProfile(UserDTO profile) throws SQLException {

        String nickname = profile.getNickname();

        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException(
                    "닉네임을 입력해주세요.");
        }

        nickname = nickname.trim();
        profile.setNickname(nickname);

        checkLength(nickname, 15, "닉네임");
        checkLength(profile.getName(), 10, "이름");
        checkLength(profile.getMajor(), 30, "학과");
        checkLength(profile.getProfileImg(), 255, "이미지 경로");

        try (Connection conn = AuthDBUtil.getConnection()) {

            UserDAO dao = new UserDAO(conn);

            UserDTO current = dao.selectById(profile.getStudentId());

            if (current == null) {
                throw new IllegalArgumentException(
                        "존재하지 않는 회원입니다.");
            }

            if (!nickname.equals(current.getNickname())
                    && dao.existsByNickname(nickname)) {

                throw new IllegalArgumentException(
                        "이미 사용 중인 닉네임입니다.");
            }

            if (profile.getProfileImg() == null) {
                profile.setProfileImg(current.getProfileImg());
            }

            try {
                if (dao.updateProfile(profile) != 1) {
                    throw new SQLException("프로필 저장에 실패했습니다.");
                }
            } catch (SQLException e) {

                if (e.getErrorCode() == 1) {
                    throw new IllegalArgumentException(
                            "이미 사용 중인 닉네임입니다.");
                }

                throw e;
            }

            return new User(dao.selectById(profile.getStudentId()));
        }
    }

    private String normalizeEmail(String email) {

        if (email == null) {
            throw new IllegalArgumentException(
                    "학교 이메일을 입력해주세요.");
        }

        email = email.trim().toLowerCase(Locale.ROOT);

        if (!email.matches("[0-9]{8}@dongduk\\.ac\\.kr")) {
            throw new IllegalArgumentException(
                    "올바른 학교 이메일을 입력해주세요.");
        }

        return email;
    }

    private void checkLength(String value, int max, String label) {

        if (value != null &&
                value.getBytes(StandardCharsets.UTF_8).length > max) {

            throw new IllegalArgumentException(
                    label + "은(는) " + max + "BYTE 이하로 입력해주세요.");
        }
    }
}
