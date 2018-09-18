package io.flippedclassroom.server.web;//package io.flippedclassroom.server.web;
//
//import io.flippedclassroom.server.annotation.CurrentUser;
//import io.flippedclassroom.server.config.Const;
//import io.flippedclassroom.server.entity.Course;
//import io.flippedclassroom.server.entity.Quiz;
//import io.flippedclassroom.server.entity.User;
//import io.flippedclassroom.server.entity.UserQuizResult;
//import io.flippedclassroom.server.entity.response.JsonResponse;
//import io.flippedclassroom.server.exception.Http400BadRequestException;
//import io.flippedclassroom.server.service.CourseService;
//import io.flippedclassroom.server.service.QuizService;
//import io.flippedclassroom.server.service.UserQuizResultService;
//import io.flippedclassroom.server.util.AssertUtils;
//import io.swagger.annotations.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import springfox.documentation.annotations.ApiIgnore;
//
//import javax.validation.constraints.NotNull;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Api(tags = "随堂测试", description = "目前包括：开启测试入口、关闭测试入口、获取全部测试题、新增测试题、提交单个测试题的答案、查看统计结果")
//@RestController
//public class QuizController {
//	@Autowired
//	private CourseService courseService;
//	@Autowired
//	private QuizService quizService;
//	@Autowired
//	private UserQuizResultService userQuizResultService;
//
//	@RequestMapping(value = "/course/{courseId}/quiz/start", method = RequestMethod.GET)
//	@ApiOperation(value = "教师请求此API打开学生端随堂测试入口", httpMethod = "GET")
//	@ApiResponses(
//			@ApiResponse(code = 200, message = "成功情况：{\n" +
//					"\"status\": \"SUCCESS\",\n" +
//					"\"message\": \"成功开启随堂测试入口！\"\n" +
//					"}\n失败情况：{\n" +
//					"\"status\": \"FAILED\",\n" +
//					"\"message\": \"课程 id 非法！\"\n" +
//					"}")
//	)
//	public JsonResponse startQuiz(@PathVariable Long courseId) throws Http400BadRequestException {
//		Course course = courseService.findById(courseId);
//		AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("课程 id 非法！"));
//		course.setQuizEnable(true);
//		courseService.save(course);
//		return new JsonResponse("200", "成功开启随堂测试入口！",null);
//	}
//
//	@RequestMapping(value = "/course/{courseId}/quiz/stop", method = RequestMethod.GET)
//	@ApiOperation(value = "教师请求此API以关闭学生端随堂测试入口", httpMethod = "GET")
//	@ApiResponses(
//			@ApiResponse(code = 200, message = "成功情况：{\n" +
//					"\"status\": \"SUCCESS\",\n" +
//					"\"message\": \"成功关闭随堂测试！\"\n" +
//					"}\n失败情况：{\n" +
//					"\"status\": \"FAILED\",\n" +
//					"\"message\": \"课程 id 非法！\"\n" +
//					"}")
//	)
//	public JsonResponse stopQuiz(@PathVariable Long courseId) throws Http400BadRequestException {
//		Course course = courseService.findById(courseId);
//		AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("课程 id 非法！"));
//		course.setQuizEnable(false);
//		courseService.save(course);
//		return new JsonResponse("200", "成功关闭随堂测试！",null);
//	}
//
//	@RequestMapping(value = "/course/{courseId}/quiz", method = RequestMethod.GET)
//	@ApiOperation(value = "获取随堂测试题目，默认全部获取", httpMethod = "GET")
//	@ApiResponses(
//			@ApiResponse(code = 200, message = "成功请求：\n{\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"quizList\":[\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"id\": 3,\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"content\": \"请从一二三四中随机选择一个数字？A. 一：B. 二：C. 三：D. 四\",\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"answer\": \"C\"\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"id\": 4,\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"content\": \"Java中类的访问权限有几种？A. 4种：B. 3种：C. 2种：D. 1种\",\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"answer\": \"A\"\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;],\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"SUCCESS\"\n" +
//					"}\n请求失败，一般是教师端尚未开启接口：\n{\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"FAILED\",\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"尚未开始随堂测试，请等待教师开始后再尝试！\"\n" +
//					"}")
//	)
//	public JsonResponse getQuizs(@PathVariable Long courseId) throws Http400BadRequestException {
//		Course course = courseService.findById(courseId);
//		AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("课程 id 非法！"));
//		Map<String, Object> map = new HashMap<>();
//		if (course.isQuizEnable()) {
//			map.put("status", Const.SUCCESS);
//			map.put("quizList", course.getQuizList());
//			return new JsonResponse("200","请求成功",map);
//		} else {
//			return new JsonResponse("4xx","尚未开始随堂测试，请等待教师开始后再尝试！",null);
//		}
//
//	}
//
//	@RequestMapping(value = "/course/{courseId}/quiz", method = RequestMethod.POST)
//    @ApiOperation(value = "新增随堂测试题", httpMethod = "POST")
//    @ApiImplicitParams(
//            @ApiImplicitParam(name = "quiz", value = "提交 JSON 示例：\n{\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;  \"content\":\"Java类的关键字是？A. class：B.classroom：C. c：D. public\",\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;  \"answer\":\"A\"\n" +
//                    "}", required = true, dataTypeClass = Quiz.class, paramType = "body")
//    )
//    @ApiResponses(
//            @ApiResponse(code = 200, message = "")
//    )
//	public JsonResponse postQuiz(@PathVariable Long courseId, @NotNull @RequestBody Quiz quiz) throws Http400BadRequestException {
//	    Course course = courseService.findById(courseId);
//	    AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("课程 id 非法！"));
//	    Quiz newQuiz = new Quiz(quiz.getContent(), quiz.getAnswer());
//	    newQuiz.setCourse(course);
//	    quizService.save(newQuiz);
//	    return new JsonResponse("200", "成功设置随堂测试题！",null);
//    }
//
//	@RequestMapping(value = "/course/{courseId}/quiz/{quizId}", method = RequestMethod.POST)
//	@ApiOperation(value = "提交某一测试题的答案", httpMethod = "POST")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "courseId", value = "课程 id", required = true, paramType = "path", dataTypeClass = Integer.class),
//            @ApiImplicitParam(name = "quizId", value = "测试题 id", required = true, paramType = "path", dataTypeClass = Integer.class),
//            @ApiImplicitParam(name = "postResult", value = "数据示例：\n{\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;  \"answer\": \"string\"\n" +
//                    "}", required = true, paramType = "body", dataTypeClass = UserQuizResult.class)
//    })
//	@ApiResponses(
//			@ApiResponse(code = 200, message = "提交成功：\n{\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"SUCCESS\",\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"成功提交结果！\"\n" +
//					"}\n提交失败（课程id非法）：\n{\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"FAILED\",\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"课程 id 非法！\"\n" +
//					"}\n提交失败（测试题id非法）：\n{\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"FAILED\",\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"测试题 id 非法！\"\n" +
//					"}\n提交失败（提交入口已关闭）：\n{\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"FAILED\",\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"随堂测试题提交入口已关闭！\"\n" +
//					"}\n提交失败（重复提交）：\n{\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"FAILED\",\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"不能重复提交答案！\"\n" +
//					"}\n提交失败（教师不能参与提交答案）\n{\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"FAILED\",\n" +
//					"&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"教师不能参与随堂测试！\"\n" +
//					"}")
//	)
//	public JsonResponse postQuizAnswer(@PathVariable Long courseId, @PathVariable Long quizId, @ApiIgnore @CurrentUser User user, @RequestBody UserQuizResult postResult) throws Http400BadRequestException {
//		Course course = courseService.findById(courseId);
//		AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("课程 id 非法！"));
//		Quiz quiz = quizService.findById(quizId);
//		AssertUtils.assertNotNUllElseThrow(quiz, () -> new Http400BadRequestException("测试题 id 非法！"));
//
//		System.out.println("提交用户的 Role：" + user.getRole().getRole());
//		if (user.getRole().getRole().equals(Const.Teacher)) {
//			throw new Http400BadRequestException("教师不能参与随堂测试！");
//		}
//
//		if (course.isQuizEnable()) {
//			UserQuizResult result = userQuizResultService.findByUserAndQuiz(user, quiz);
//			Map<String, Object> response = new HashMap<>();
//			if (result == null) {
//				result = new UserQuizResult(postResult.getAnswer(), quiz.getAnswer(), user, quiz);
//				userQuizResultService.save(result);
//				return new JsonResponse("200", "成功提交结果！",null);
//			} else {
//				throw new Http400BadRequestException("不能重复提交答案！");
//			}
//		} else {
//			throw new Http400BadRequestException("随堂测试题提交入口已关闭！");
//		}
//	}
//
//	@RequestMapping(value = "/course/{courseId}/quiz/{quizId}/analysis", method = RequestMethod.GET)
//	@ApiOperation(value = "查看测试题目结果统计", httpMethod = "GET")
//	@ApiResponses(
//			@ApiResponse(code = 200, message = "统计示例：\n{\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;\"rightUsers\":[{\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"nickname\": \"2\",\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"avatar\": \"https://api.fc.xd.style/avatar\",\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\":{\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\": \"student\"\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;}],\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;\"rightPercent\": 0,\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;\"totalSubmit\": 2,\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;\"wrongUserAndResults\":[{\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"id\": 2,\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"answer\": \"A\",\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"rightAnswer\": \"C\",\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"user\":{\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"nickname\": \"1\",\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"avatar\": \"https://api.fc.xd.style/avatar\",\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\":{\"role\": \"student\"}\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;}],\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;\"rightCount\": 1,\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"SUCCESS\",\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;\"noSubmitUsers\":[{\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"nickname\": \"3\",\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"avatar\": \"https://api.fc.xd.style/avatar\",\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\":{\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\": \"student\"\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
//                    "&nbsp;&nbsp;&nbsp;&nbsp;}]\n" +
//                    "}")
//	)
//	public JsonResponse quizAnalysis(@PathVariable Long courseId, @PathVariable Long quizId, @ApiIgnore @CurrentUser User user) throws Http400BadRequestException {
//		Course course = courseService.findById(courseId);
//		AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("课程 id 非法！"));
//		Quiz quiz = quizService.findById(quizId);
//		AssertUtils.assertNotNUllElseThrow(quiz, () -> new Http400BadRequestException("测试题 id 非法！"));
//
//		if (course.isQuizEnable()) {
//			throw new Http400BadRequestException("随堂测试尚未结束，不能查看统计信息！");
//		}
//
//		Map<String, Object> response = new HashMap<>();
//		List<User> users = course.getUserList()
//				.parallelStream()
//				.filter(u -> u.getRole().getRole().equals("student"))
//				.collect(Collectors.toList());
//		List<User> rightUsers = new LinkedList<>();
//		List<UserQuizResult> wrongUsers = new LinkedList<>();
//		List<UserQuizResult> results = userQuizResultService.findByQuiz(quiz);
//
//		for (UserQuizResult result : results) {
//			if (result.getAnswer().equals(quiz.getAnswer())) {
//				rightUsers.add(result.getUser());
//			} else {
//				wrongUsers.add(result);
//			}
//			users.remove(result.getUser());
//		}
//		response.put("status", Const.SUCCESS);
//		response.put("totalSubmit", results.size());
//		response.put("rightCount", rightUsers.size());
//		if (results.size() == 0) {
//			response.put("rightPercent", 0);
//		} else {
//			response.put("rightPercent", rightUsers.size() / results.size());
//		}
//		response.put("rightUsers", rightUsers);
//		response.put("wrongUserAndResults", wrongUsers);
//		response.put("noSubmitUsers", users);
//		return new JsonResponse("200","请求成功",response);
//	}
//}
