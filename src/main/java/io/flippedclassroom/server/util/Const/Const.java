package io.flippedclassroom.server.util.Const;

public interface Const {
    // version
    String swaggerVersion = "v0.5.11";
    String version = "v0.0.1";

    // 身份类型
    String Student = "student";
    String Teacher = "teacher";
    //token存活时间
    long EXPIRE_TIME = 1000 * 3600 * 24 * 10;
    //日期格式化
    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    // Password Encrypt
    String algorithm = "MD5";
    int iteration = 1024;

    // 上传文件大小
    int size1M = 1024 * 1024;    // 1M
    String defaultAvatarLink = "https://www.gravatar.com/avatar/MD5?d=identicon";
    String showAvatar = "https://api.fc.xd.style/avatar";
    String coursePictureLink = "https://fc.xd.style/course/COURSEID/picture";

    // 上传文件位置
    String basePosition = "/var/www/uploads/";
    String avatarPosition = basePosition + "avatar/";
    String file_position = basePosition + "normal/";
    String course_position = basePosition + "course/";
}
