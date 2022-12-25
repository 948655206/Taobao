package com.zxyapp.taobaounion.model.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Categories {
  /**
   * {
   *     "success": true,
   *     "code": 10000,
   *     "message": "获取分类成功.",
   *     "data": [
   *         {
   *             "id": 9660,
   *             "title": "推荐"
   *         },
   *         {
   *             "id": 9649,
   *             "title": "食品"
   *         }
   *     ]
   * }
   */
  @SerializedName("success")
  private Boolean success;
  @SerializedName("code")
  private Integer code;
  @SerializedName("message")
  private String message;
  @SerializedName("data")
  private List<DataDTO> data;

  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getEssage() {
    return message;
  }

  public void setEssage(String essage) {
    message = essage;
  }

  public List<DataDTO> getData() {
    return data;
  }

  public void setData(List<DataDTO> data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "Categories{" +
            "success=" + success +
            ", code=" + code +
            ", message='" + message + '\'' +
            ", data=" + data +
            '}';
  }

  public static class DataDTO {

    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    @Override
    public String toString() {
      return "DataDTO{" +
              "id=" + id +
              ", title='" + title + '\'' +
              '}';
    }
  }
}