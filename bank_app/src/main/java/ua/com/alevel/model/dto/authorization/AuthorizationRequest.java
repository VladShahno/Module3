package ua.com.alevel.model.dto.authorization;

public class AuthorizationRequest {

    private Long userId;

    private String userName;

    public AuthorizationRequest(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

}
