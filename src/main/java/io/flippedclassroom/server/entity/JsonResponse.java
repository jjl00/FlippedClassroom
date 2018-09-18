package io.flippedclassroom.server.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

//通用返回json的格式
@ApiModel(value = "通用返回json类", description = "普通API的返回信息都是通过该类进行构建的")
@Data
@AllArgsConstructor
public class JsonResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(
            value = "返回状态码",
            required = true,
            example = "请求成功为200，客户端请求出错为4xx,服务器错误为5xx"
    )
    private String code;
    @ApiModelProperty(
            value = "操作结果附带的说明信息",
            required = true,
            example = "操作成功/失败!"
    )
    private String message;
    @ApiModelProperty(
            value = "返回的数据",
            required = true,
            example = "course:{...}或者courses:[...]为了易于区分返回数据," +
                    "有如下规定data:后接其他的键，如user,users,course...复数表示多个数据"
    )
    private Object data;
}
