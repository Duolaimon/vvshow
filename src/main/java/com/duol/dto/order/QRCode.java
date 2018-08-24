package com.duol.dto.order;

/**
 * 二维码信息
 *
 * @author Duolaimon
 * 18-8-18 下午1:06
 */
public class QRCode {
    private String orderNo;
    private String qrUrl;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }
}
