package com.zxyapp.taobaounion.model.domain;

public class TicketResult {

    private boolean success;
    private int code;
    private String message;
    private DataBean data;

    @Override
    public String toString() {
        return "TicketResult{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "tbk_tpwd_create_response=" + tbk_tpwd_create_response +
                    '}';
        }

        private TbkTpwdCreateResponseBean tbk_tpwd_create_response;

        public TbkTpwdCreateResponseBean getTbk_tpwd_create_response() {
            return tbk_tpwd_create_response;
        }

        public void setTbk_tpwd_create_response(TbkTpwdCreateResponseBean tbk_tpwd_create_response) {
            this.tbk_tpwd_create_response = tbk_tpwd_create_response;
        }

        public static class TbkTpwdCreateResponseBean {
            @Override
            public String toString() {
                return "TbkTpwdCreateResponseBean{" +
                        "data=" + data +
                        ", request_id='" + request_id + '\'' +
                        '}';
            }

            private DataDoto data;
            private String request_id;

            public DataDoto getData() {
                return data;
            }

            public void setData(DataDoto data) {
                this.data = data;
            }

            public String getRequest_id() {
                return request_id;
            }

            public void setRequest_id(String request_id) {
                this.request_id = request_id;
            }

            public static class DataDoto {

                private String model;

                public String getModel() {
                    return model;
                }

                public void setModel(String model) {
                    this.model = model;
                }

                @Override
                public String toString() {
                    return "DataDoto{" +
                            "model='" + model + '\'' +
                            '}';
                }
            }
        }
    }
}
