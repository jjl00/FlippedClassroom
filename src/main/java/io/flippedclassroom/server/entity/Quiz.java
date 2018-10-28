package io.flippedclassroom.server.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 描述:
 * 随堂测试
 *
 * @author HASEE
 * @create 2018-09-14 19:14
 */
@ApiModel(value = "随堂测试",description = "一次测试，包括多道题")
@Entity
@Table(name = "tb_quiz")
@Data
@Accessors(chain = true)
public class Quiz {
    @Id
    @GeneratedValue
    private long quiz_id;

    @ApiModelProperty(name = "测试题内容",notes = "建议使用逗号分隔")
    private String quiz_content;

    @ApiModelProperty(name = "测试题答案",notes = "建议使用逗号分隔")
    private String quiz_answer;

    @ApiModelProperty(name = "每道题的解析",notes = "建议使用逗号分隔")
    private String quiz_analyze;

}