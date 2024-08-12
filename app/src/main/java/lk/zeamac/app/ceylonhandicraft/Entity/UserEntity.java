package lk.zeamac.app.ceylonhandicraft.Entity;

public class UserEntity {
    private String id;
    private String name;
    private String fullName;
    private String email;
    private String type;
    private String title;
    private String birthDay;
    private String mobile;

    public UserEntity() {
    }

    public UserEntity(String id, String name, String fullName, String email, String type, String title, String birthDay, String mobile) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.email = email;
        this.type = type;
        this.title = title;
        this.birthDay = birthDay;
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
}
