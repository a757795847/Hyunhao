package com.zy.gcode.pojo;

import com.zy.gcode.service.annocation.CsvPush;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by admin5 on 17/2/14.
 */
public class DataOrder {
    private String id;
    private String weixinId;
    private String mchNumber;
    @CsvPush("订单编号")
    private String orderNumber;
    private BigDecimal giftMoney;
    private String giftDetail;
    private int giftState;
    private String commentFile1;
    private String commentFile2;
    private String commentFile3;
    private Timestamp applyDate;
    private Timestamp approveDate;
    private Timestamp sendDate;
    private Timestamp recieveDate;
    private String rejectReason;
    private String createUserId;
    private Timestamp createDate;
    private String updateUserId;
    private Timestamp updateDate;
    private String delFlag;
    @CsvPush("买家会员名")
    private String buyerName;
    @CsvPush("买家支付宝账号")
    private String buyerZhifubao;
    @CsvPush("买家应付货款")
    private String dues;
    @CsvPush("买家应付邮费")
    private String postage;
    @CsvPush("买家支付积分")
    private String payPoints;
    @CsvPush("总金额")
    private String amount;
    @CsvPush("返点积分")
    private String rebatePoint;
    @CsvPush("买家实际支付金额")
    private String actualAmount;
    @CsvPush("买家实际支付积分")
    private String actualPayPoints;
    @CsvPush("订单状态")
    private String orderState;
    @CsvPush("买家留言")
    private String buyerNotice;
    @CsvPush("收货人姓名")
    private String receiver;
    @CsvPush("收货地址")
    private String receiverAddress;
    @CsvPush("运送方式")
    private String postKind;
    @CsvPush("联系电话")
    private String receiverTel;
    @CsvPush("联系手机")
    private String receiverMobile;
    @CsvPush("订单创建时间")
    private String orderCreateTime;
    @CsvPush("订单付款时间")
    private String orderPayTime;
    @CsvPush("宝贝标题")
    private String goodsTitle;
    @CsvPush("宝贝种类")
    private String goodsKind;
    @CsvPush("物流单号")
    private String logisticsNumber;
    @CsvPush("物流公司")
    private String logisticsCompany;
    @CsvPush("订单备注")
    private String orderRemark;
    @CsvPush("宝贝总数量")
    private String goodsNumber;
    @CsvPush("店铺Id")
    private String shopId;
    @CsvPush("店铺名称")
    private String shopName;
    @CsvPush("订单关闭原因")
    private String orderCloseReason;
    @CsvPush("卖家服务费")
    private String solderFee;
    @CsvPush("买家服务费")
    private String buyerFee;
    @CsvPush("发票抬头")
    private String invoiceTitle;
    @CsvPush("是否手机订单")
    private String isMobileOrder;
    @CsvPush("分阶段订单信息")
    private String phaseOrderInfo;
    @CsvPush("特权订金订单id")
    private String privilegeOrderId;
    @CsvPush("是否上传合同照片")
    private String isTransferAgreementPhoto;
    @CsvPush("是否上传小票")
    private String isTransferReceipt;
    @CsvPush("是否代付")
    private String isPayByAnother;
    @CsvPush("定金排名")
    private String earnestRanking;
    @CsvPush("修改后的sku")
    private String skuChanged;
    @CsvPush("修改后的收货地址")
    private String receiverAddressChanged;
    @CsvPush("异常信息")
    private String errorInfo;
    @CsvPush("天猫卡券抵扣")
    private String tmallCardsDeduction;
    @CsvPush("集分宝抵扣")
    private String pointDedution;
    @CsvPush("是否是O2O交易")
    private String isO2OTrade;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId = weixinId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getGiftMoney() {
        return giftMoney;
    }

    public void setGiftMoney(BigDecimal giftMoney) {
        this.giftMoney = giftMoney;
    }

    public String getGiftDetail() {
        return giftDetail;
    }

    public void setGiftDetail(String giftDetail) {
        this.giftDetail = giftDetail;
    }

    public int getGiftState() {
        return giftState;
    }

    public void setGiftState(int giftState) {
        this.giftState = giftState;
    }

    public String getCommentFile1() {
        return commentFile1;
    }

    public void setCommentFile1(String commentFile1) {
        this.commentFile1 = commentFile1;
    }

    public String getCommentFile2() {
        return commentFile2;
    }

    public void setCommentFile2(String commentFile2) {
        this.commentFile2 = commentFile2;
    }

    public String getCommentFile3() {
        return commentFile3;
    }

    public void setCommentFile3(String commentFile3) {
        this.commentFile3 = commentFile3;
    }

    public Timestamp getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Timestamp applyDate) {
        this.applyDate = applyDate;
    }

    public Timestamp getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Timestamp approveDate) {
        this.approveDate = approveDate;
    }

    public Timestamp getSendDate() {
        return sendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
    }

    public Timestamp getRecieveDate() {
        return recieveDate;
    }

    public void setRecieveDate(Timestamp recieveDate) {
        this.recieveDate = recieveDate;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerZhifubao() {
        return buyerZhifubao;
    }

    public void setBuyerZhifubao(String buyerZhifubao) {
        this.buyerZhifubao = buyerZhifubao;
    }

    public String getDues() {
        return dues;
    }

    public void setDues(String dues) {
        this.dues = dues;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public String getPayPoints() {
        return payPoints;
    }

    public void setPayPoints(String payPoints) {
        this.payPoints = payPoints;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRebatePoint() {
        return rebatePoint;
    }

    public void setRebatePoint(String rebatePoint) {
        this.rebatePoint = rebatePoint;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getActualPayPoints() {
        return actualPayPoints;
    }

    public void setActualPayPoints(String actualPayPoints) {
        this.actualPayPoints = actualPayPoints;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getBuyerNotice() {
        return buyerNotice;
    }

    public void setBuyerNotice(String buyerNotice) {
        this.buyerNotice = buyerNotice;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getPostKind() {
        return postKind;
    }

    public void setPostKind(String postKind) {
        this.postKind = postKind;
    }

    public String getReceiverTel() {
        return receiverTel;
    }

    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getOrderPayTime() {
        return orderPayTime;
    }

    public void setOrderPayTime(String orderPayTime) {
        this.orderPayTime = orderPayTime;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsKind() {
        return goodsKind;
    }

    public void setGoodsKind(String goodsKind) {
        this.goodsKind = goodsKind;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOrderCloseReason() {
        return orderCloseReason;
    }

    public void setOrderCloseReason(String orderCloseReason) {
        this.orderCloseReason = orderCloseReason;
    }

    public String getSolderFee() {
        return solderFee;
    }

    public void setSolderFee(String solderFee) {
        this.solderFee = solderFee;
    }

    public String getBuyerFee() {
        return buyerFee;
    }

    public void setBuyerFee(String buyerFee) {
        this.buyerFee = buyerFee;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getIsMobileOrder() {
        return isMobileOrder;
    }

    public void setIsMobileOrder(String isMobileOrder) {
        this.isMobileOrder = isMobileOrder;
    }

    public String getPhaseOrderInfo() {
        return phaseOrderInfo;
    }

    public void setPhaseOrderInfo(String phaseOrderInfo) {
        this.phaseOrderInfo = phaseOrderInfo;
    }

    public String getPrivilegeOrderId() {
        return privilegeOrderId;
    }

    public void setPrivilegeOrderId(String privilegeOrderId) {
        this.privilegeOrderId = privilegeOrderId;
    }

    public String getIsTransferAgreementPhoto() {
        return isTransferAgreementPhoto;
    }

    public void setIsTransferAgreementPhoto(String isTransferAgreementPhoto) {
        this.isTransferAgreementPhoto = isTransferAgreementPhoto;
    }

    public String getIsTransferReceipt() {
        return isTransferReceipt;
    }

    public void setIsTransferReceipt(String isTransferReceipt) {
        this.isTransferReceipt = isTransferReceipt;
    }

    public String getIsPayByAnother() {
        return isPayByAnother;
    }

    public void setIsPayByAnother(String isPayByAnother) {
        this.isPayByAnother = isPayByAnother;
    }

    public String getEarnestRanking() {
        return earnestRanking;
    }

    public void setEarnestRanking(String earnestRanking) {
        this.earnestRanking = earnestRanking;
    }

    public String getSkuChanged() {
        return skuChanged;
    }

    public void setSkuChanged(String skuChanged) {
        this.skuChanged = skuChanged;
    }

    public String getReceiverAddressChanged() {
        return receiverAddressChanged;
    }

    public void setReceiverAddressChanged(String receiverAddressChanged) {
        this.receiverAddressChanged = receiverAddressChanged;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getTmallCardsDeduction() {
        return tmallCardsDeduction;
    }

    public void setTmallCardsDeduction(String tmallCardsDeduction) {
        this.tmallCardsDeduction = tmallCardsDeduction;
    }

    public String getPointDedution() {
        return pointDedution;
    }

    public void setPointDedution(String pointDedution) {
        this.pointDedution = pointDedution;
    }

    public String getIsO2OTrade() {
        return isO2OTrade;
    }

    public void setIsO2OTrade(String isO2OTrade) {
        this.isO2OTrade = isO2OTrade;
    }

    public String getMchNumber() {
        return mchNumber;
    }

    public void setMchNumber(String mchNumber) {
        this.mchNumber = mchNumber;
    }
}
