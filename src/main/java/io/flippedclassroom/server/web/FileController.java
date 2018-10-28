package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.service.MyFileService;
import io.flippedclassroom.server.util.Const.Const;
import io.flippedclassroom.server.entity.*;
import io.flippedclassroom.server.entity.JsonResponse;
import io.flippedclassroom.server.exception.AssertException;
import io.flippedclassroom.server.exception.Http400BadRequestException;
import io.flippedclassroom.server.service.CourseService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.AssertUtils;
import io.flippedclassroom.server.util.ImageUtils;
import io.flippedclassroom.server.util.LogUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@Slf4j
@Api(tags = "文件相关", description = "目前包括：拉取头像、头像上传、拉取所有预习资料、下载特定 id 的预习资料、上传课程预习资料、拉取所有电子资料、下载特定 id 的电子资料、上传电子资料、拉取课程图片、上传课程图片、删除特定ID的预习资料、删除特定ID的电子资料")
public class FileController {
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private MyFileService myFileService;

    @GetMapping(value = "/auth/avatar")
    @ApiOperation(value = "获取头像", httpMethod = "GET", notes = "有个bug，需要先自己上传头像")
    @ApiResponses(
            @ApiResponse(code = 200, message = "返回图片流，如果用户没有设置头像，默认从 Gravatar 加载头像")
    )
    public JsonResponse getAvatar(@ApiIgnore @CurrentUser User user,HttpServletResponse response) {
        String avatarPosition = user.getAvatar();
        try {
            try {
//                 保证位置合法
                AssertUtils.assertPositionValid(avatarPosition);
                InputStream in = new FileInputStream(user.getAvatar());
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                IOUtils.copy(in, response.getOutputStream());
                in.close();
                return new JsonResponse("200", "成功获得用户 " + user.getUser_id() + "的头像！", null);
            } catch (AssertException e) {
                String md5 = DigestUtils.md5DigestAsHex(user.getUser_id().toString().getBytes());
 //               URL url = new URL(Const.defaultAvatarLink.replace("MD5", md5));
                URL url = new URL("http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                IOUtils.copy(connection.getInputStream(), response.getOutputStream());
                user.setAvatar(url.toString());
                userService.save(user);
                return new JsonResponse("200", "成功从 gravatar 生成头像！", null);
            }
        } catch (IOException ignored) {
            return new JsonResponse("5xx", "IOException!", null);
        }
    }


    @PostMapping(value = "/auth/avatar")
    @ApiOperation(value = "设置头像", httpMethod = "POST", notes = "头像文件，默认会将非 PNG 格式的图像转换为 PNG 格式存储")
    @ApiResponses(
            @ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
    )
    public JsonResponse postAvatar(@RequestPart(name = "file") MultipartFile multipartFile, @ApiIgnore @CurrentUser User user) throws Http400BadRequestException {
        AssertUtils.assertNotNUllElseThrow(multipartFile, () -> new Http400BadRequestException("上传的头像文件不能为空！"));
        try {
            String avatarPosition = Const.avatarPosition + "user-" + user.getUser_id() + ".png";
            FileUtils.writeByteArrayToFile(new File(avatarPosition), multipartFile.getBytes());
            user.setAvatar(avatarPosition);
            userService.save(user);
            return new JsonResponse("200", "成功设置头像！", null);
        } catch (IOException e) {
            log.error("io异常");
            return new JsonResponse("5xx", "IOException!", null);
        }
    }

    @PostMapping("/auth/course/{course_id}/file/{file_format}")
    @ApiOperation(value = "上传文件", httpMethod = "POST",notes = "文件格式有(\"picture\"),\n" +
            "    (\"video\"),\n" +
            "    (\"audio\"),\n" +
            "    (\"document\");")
    public JsonResponse uploadFile(@RequestPart(name = "file") MultipartFile multipartFile, @PathVariable("course_id") Long course_id, @PathVariable String file_format, @ApiIgnore @CurrentUser User user) throws Exception {
        String file_position = Const.course_position + course_id + "/" + file_format + "/" + multipartFile.getOriginalFilename();
        if(myFileService.assertReapt(course_id,file_format,file_position)){
            return new JsonResponse("400","文件重复上传",multipartFile.getOriginalFilename());
        }
        FileUtils.writeByteArrayToFile(new File(file_position), multipartFile.getBytes());
        MyFile myFile = new MyFile();
        myFile.setCourse(courseService.findById(course_id))
                .setFile_date(new Date(System.currentTimeMillis()))
                .setFile_author(user.getUser_name())
                .setFileFormat(file_format)
                .setPosition(file_position)
                .setSize(multipartFile.getSize())
                .setFile_name(multipartFile.getOriginalFilename())
        ;
        myFileService.save(myFile);
        log.info("文件{}上传成功", multipartFile.getName());
        return new JsonResponse("200", "文件上传成功", null);
    }

    @GetMapping("/course/{course_id}/all")
    @ApiOperation(value = "获取某一课程下所有文件", httpMethod = "GET")
    public JsonResponse getAllFile( @PathVariable("course_id") Long course_id) throws Exception {
        List<MyFile> list=courseService.findById(course_id).getMyFileList();
        return new JsonResponse("200","请求成功",list);
    }

    @GetMapping("/course/{course_id}/file/{file_format}")
    @ApiOperation(value = "获取某一课程下某一格式的文件列表", httpMethod = "GET",notes = "文件格式有(\"picture\"),\n" +
            "    (\"video\"),\n" +
            "    (\"audio\"),\n" +
            "    (\"document\"); 然后可以根据文件的属性position，调用下载的api进行下载")
    public JsonResponse getFileList( @PathVariable("course_id") Long course_id, @PathVariable String file_format) throws Exception {
        List<MyFile> list=courseService.findById(course_id).getMyFileList();
        list=list.parallelStream().filter((a)->a.getFileFormat().equals(file_format)).collect(Collectors.toList());
        return new JsonResponse("200","请求成功",list);
    }




    @GetMapping("/file/{file_id}")
    @ApiOperation(value = "下载文件", httpMethod = "GET")
    public JsonResponse uploadFile(HttpServletResponse response, @PathVariable long file_id) {
        MyFile myFile=myFileService.findById(file_id);
        String position=myFile.getPosition();
        String fileName="";
        try {
             fileName=new String(myFile.getFile_name().getBytes("utf-8"),"iso8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            AssertUtils.assertPositionValidElseThrow(position, () -> new Http400BadRequestException("没有这个文件！"));
            InputStream in = new FileInputStream(position);
            response.setHeader("Content-Disposition", "attachment; filename=\"" +fileName );
            response.addHeader("Content-Length", "" + myFile.getSize());
          response.setContentType("application/octet-stream;charset=UTF-8");
            IOUtils.copy(in, response.getOutputStream());
            return new JsonResponse("200", "成功下载 " + position.substring(position.lastIndexOf("/") + 1), null);
        } catch (IOException e) {
            log.info("IOException during download the file: " + e.getMessage());
            return new JsonResponse("5xx", "IOException!!!", null);
        } catch (Http400BadRequestException e) {
            return new JsonResponse("4xx", e.getMessage(), null);
        }

    }

    @RequestMapping(value = "/course/{courseID}/picture", method = RequestMethod.GET)
    @ApiOperation(value = "获取课程图片")
    @ApiResponses(
            @ApiResponse(code = 200, message = "课程图片流")
    )
    public JsonResponse getCoursePicture(@PathVariable Long courseID, HttpServletResponse response) throws Http400BadRequestException {
        Course course = courseService.findById(courseID);
        AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("未找到该课程"));
        String pictureLocation = course.getPicture();
        try {
            try {
                AssertUtils.assertPositionValid(pictureLocation);
                InputStream in = new FileInputStream(pictureLocation);
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                IOUtils.copy(in, response.getOutputStream());
                return new JsonResponse("200", "成功获取课程 " + course.getCourse_id() + " 的图片！", null);
            } catch (AssertException e) {
                String md5 = DigestUtils.md5DigestAsHex(course.getCourse_id().toString().getBytes());
                URL url = new URL(Const.defaultAvatarLink.replace("MD5", md5));
//                URL url = new URL("http://pic.58pic.com/58pic/13/74/51/99d58PIC6vm_1024.jpg");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                InputStream inputStream=connection.getInputStream();
                IOUtils.copy(inputStream, response.getOutputStream());
                inputStream.close();
                course.setPicture(url.toString());
                courseService.save(course);
                return new JsonResponse("200", "成功为课程 " + course.getCourse_id() + "从 gravatar 生成图片！", null);
            }
        } catch (IOException e) {
            return new JsonResponse("5xx", "IOException!", null);
        }
    }

    @PostMapping(value = "/course/{courseID}/picture")
    @ApiOperation(value = "上传课程图片")
    @ApiResponses(
            @ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
    )
    public JsonResponse postCoursePicture(@PathVariable Long courseID, @RequestPart(name = "file") MultipartFile multipartFile) throws Http400BadRequestException {
        Course course = courseService.findById(courseID);
        AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("未找到该课程"));
        String coursePicturePosition = Const.course_position + courseID + "/picture/" + multipartFile.getName();
        try {
            FileUtils.writeByteArrayToFile(new File(coursePicturePosition), multipartFile.getBytes());
        } catch (IOException e) {
            return new JsonResponse("5xx", "IOException occur! " + e.getMessage(), null);
        }
        course.setPicture(coursePicturePosition);
        courseService.save(course);
        return new JsonResponse("200", "成功设置课程图片！", null);
    }

}
