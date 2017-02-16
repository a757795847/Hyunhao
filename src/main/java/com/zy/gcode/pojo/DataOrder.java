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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataOrder dataOrder = (DataOrder) o;

        if (giftState != dataOrder.giftState) return false;
        if (id != null ? !id.equals(dataOrder.id) : dataOrder.id != null) return false;
        if (weixinId != null ? !weixinId.equals(dataOrder.weixinId) : dataOrder.weixinId != null) return false;
        if (orderNumber != null ? !orderNumber.equals(dataOrder.orderNumber) : dataOrder.orderNumber != null)
            return false;
        if (giftMoney != null ? !giftMoney.equals(dataOrder.giftMoney) : dataOrder.giftMoney != null) return false;
        if (giftDetail != null ? !giftDetail.equals(dataOrder.giftDetail) : dataOrder.giftDetail != null) return false;
        if (commentFile1 != null ? !commentFile1.equals(dataOrder.commentFile1) : dataOrder.commentFile1 != null)
            return false;
        if (commentFile2 != null ? !commentFile2.equals(dataOrder.commentFile2) : dataOrder.commentFile2 != null)
            return false;
        if (commentFile3 != null ? !commentFile3.equals(dataOrder.commentFile3) : dataOrder.commentFile3 != null)
            return false;
        if (applyDate != null ? !applyDate.equals(dataOrder.applyDate) : dataOrder.applyDate != null) return false;
        if (approveDate != null ? !approveDate.equals(dataOrder.approveDate) : dataOrder.approveDate != null)
            return false;
        if (sendDate != null ? !sendDate.equals(dataOrder.sendDate) : dataOrder.sendDate != null) return false;
        if (recieveDate != null ? !recieveDate.equals(dataOrder.recieveDate) : dataOrder.recieveDate != null)
            return false;
        if (rejectReason != null ? !rejectReason.equals(dataOrder.rejectReason) : dataOrder.rejectReason != null)
            return false;
        if (createUserId != null ? !createUserId.equals(dataOrder.createUserId) : dataOrder.createUserId != null)
            return false;
        if (createDate != null ? !createDate.equals(dataOrder.createDate) : dataOrder.createDate != null) return false;
        if (updateUserId != null ? !updateUserId.equals(dataOrder.updateUserId) : dataOrder.updateUserId != null)
            return false;
        if (updateDate != null ? !updateDate.equals(dataOrder.updateDate) : dataOrder.updateDate != null) return false;
        if (delFlag != null ? !delFlag.equals(dataOrder.delFlag) : dataOrder.delFlag != null) return false;
        if (buyerName != null ? !buyerName.equals(dataOrder.buyerName) : dataOrder.buyerName != null) return false;
        if (buyerZhifubao != null ? !buyerZhifubao.equals(dataOrder.buyerZhifubao) : dataOrder.buyerZhifubao != null)
            return false;
        if (dues != null ? !dues.equals(dataOrder.dues) : dataOrder.dues != null) return false;
        if (postage != null ? !postage.equals(dataOrder.postage) : dataOrder.postage != null) return false;
        if (payPoints != null ? !payPoints.equals(dataOrder.payPoints) : dataOrder.payPoints != null) return false;
        if (amount != null ? !amount.equals(dataOrder.amount) : dataOrder.amount != null) return false;
        if (rebatePoint != null ? !rebatePoint.equals(dataOrder.rebatePoint) : dataOrder.rebatePoint != null)
            return false;
        if (actualAmount != null ? !actualAmount.equals(dataOrder.actualAmount) : dataOrder.actualAmount != null)
            return false;
        if (actualPayPoints != null ? !actualPayPoints.equals(dataOrder.actualPayPoints) : dataOrder.actualPayPoints != null)
            return false;
        if (orderState != null ? !orderState.equals(dataOrder.orderState) : dataOrder.orderState != null) return false;
        if (buyerNotice != null ? !buyerNotice.equals(dataOrder.buyerNotice) : dataOrder.buyerNotice != null)
            return false;
        if (receiver != null ? !receiver.equals(dataOrder.receiver) : dataOrder.receiver != null) return false;
        if (receiverAddress != null ? !receiverAddress.equals(dataOrder.receiverAddress) : dataOrder.receiverAddress != null)
            return false;
        if (postKind != null ? !postKind.equals(dataOrder.postKind) : dataOrder.postKind != null) return false;
        if (receiverTel != null ? !receiverTel.equals(dataOrder.receiverTel) : dataOrder.receiverTel != null)
            return false;
        if (receiverMobile != null ? !receiverMobile.equals(dataOrder.receiverMobile) : dataOrder.receiverMobile != null)
            return false;
        if (orderCreateTime != null ? !orderCreateTime.equals(dataOrder.orderCreateTime) : dataOrder.orderCreateTime != null)
            return false;
        if (orderPayTime != null ? !orderPayTime.equals(dataOrder.orderPayTime) : dataOrder.orderPayTime != null)
            return false;
        if (goodsTitle != null ? !goodsTitle.equals(dataOrder.goodsTitle) : dataOrder.goodsTitle != null) return false;
        if (goodsKind != null ? !goodsKind.equals(dataOrder.goodsKind) : dataOrder.goodsKind != null) return false;
        if (logisticsNumber != null ? !logisticsNumber.equals(dataOrder.logisticsNumber) : dataOrder.logisticsNumber != null)
            return false;
        if (logisticsCompany != null ? !logisticsCompany.equals(dataOrder.logisticsCompany) : dataOrder.logisticsCompany != null)
            return false;
        if (orderRemark != null ? !orderRemark.equals(dataOrder.orderRemark) : dataOrder.orderRemark != null)
            return false;
        if (goodsNumber != null ? !goodsNumber.equals(dataOrder.goodsNumber) : dataOrder.goodsNumber != null)
            return false;
        if (shopId != null ? !shopId.equals(dataOrder.shopId) : dataOrder.shopId != null) return false;
        if (shopName != null ? !shopName.equals(dataOrder.shopName) : dataOrder.shopName != null) return false;
        if (orderCloseReason != null ? !orderCloseReason.equals(dataOrder.orderCloseReason) : dataOrder.orderCloseReason != null)
            return false;
        if (solderFee != null ? !solderFee.equals(dataOrder.solderFee) : dataOrder.solderFee != null) return false;
        if (buyerFee != null ? !buyerFee.equals(dataOrder.buyerFee) : dataOrder.buyerFee != null) return false;
        if (invoiceTitle != null ? !invoiceTitle.equals(dataOrder.invoiceTitle) : dataOrder.invoiceTitle != null)
            return false;
        if (isMobileOrder != null ? !isMobileOrder.equals(dataOrder.isMobileOrder) : dataOrder.isMobileOrder != null)
            return false;
        if (phaseOrderInfo != null ? !phaseOrderInfo.equals(dataOrder.phaseOrderInfo) : dataOrder.phaseOrderInfo != null)
            return false;
        if (privilegeOrderId != null ? !privilegeOrderId.equals(dataOrder.privilegeOrderId) : dataOrder.privilegeOrderId != null)
            return false;
        if (isTransferAgreementPhoto != null ? !isTransferAgreementPhoto.equals(dataOrder.isTransferAgreementPhoto) : dataOrder.isTransferAgreementPhoto != null)
            return false;
        if (isTransferReceipt != null ? !isTransferReceipt.equals(dataOrder.isTransferReceipt) : dataOrder.isTransferReceipt != null)
            return false;
        if (isPayByAnother != null ? !isPayByAnother.equals(dataOrder.isPayByAnother) : dataOrder.isPayByAnother != null)
            return false;
        if (earnestRanking != null ? !earnestRanking.equals(dataOrder.earnestRanking) : dataOrder.earnestRanking != null)
            return false;
        if (skuChanged != null ? !skuChanged.equals(dataOrder.skuChanged) : dataOrder.skuChanged != null) return false;
        if (receiverAddressChanged != null ? !receiverAddressChanged.equals(dataOrder.receiverAddressChanged) : dataOrder.receiverAddressChanged != null)
            return false;
        if (errorInfo != null ? !errorInfo.equals(dataOrder.errorInfo) : dataOrder.errorInfo != null) return false;
        if (tmallCardsDeduction != null ? !tmallCardsDeduction.equals(dataOrder.tmallCardsDeduction) : dataOrder.tmallCardsDeduction != null)
            return false;
        if (pointDedution != null ? !pointDedution.equals(dataOrder.pointDedution) : dataOrder.pointDedution != null)
            return false;
        if (isO2OTrade != null ? !isO2OTrade.equals(dataOrder.isO2OTrade) : dataOrder.isO2OTrade != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (weixinId != null ? weixinId.hashCode() : 0);
        result = 31 * result + (orderNumber != null ? orderNumber.hashCode() : 0);
        result = 31 * result + (giftMoney != null ? giftMoney.hashCode() : 0);
        result = 31 * result + (giftDetail != null ? giftDetail.hashCode() : 0);
        result = 31 * result + giftState;
        result = 31 * result + (commentFile1 != null ? commentFile1.hashCode() : 0);
        result = 31 * result + (commentFile2 != null ? commentFile2.hashCode() : 0);
        result = 31 * result + (commentFile3 != null ? commentFile3.hashCode() : 0);
        result = 31 * result + (applyDate != null ? applyDate.hashCode() : 0);
        result = 31 * result + (approveDate != null ? approveDate.hashCode() : 0);
        result = 31 * result + (sendDate != null ? sendDate.hashCode() : 0);
        result = 31 * result + (recieveDate != null ? recieveDate.hashCode() : 0);
        result = 31 * result + (rejectReason != null ? rejectReason.hashCode() : 0);
        result = 31 * result + (createUserId != null ? createUserId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (updateUserId != null ? updateUserId.hashCode() : 0);
        result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
        result = 31 * result + (delFlag != null ? delFlag.hashCode() : 0);
        result = 31 * result + (buyerName != null ? buyerName.hashCode() : 0);
        result = 31 * result + (buyerZhifubao != null ? buyerZhifubao.hashCode() : 0);
        result = 31 * result + (dues != null ? dues.hashCode() : 0);
        result = 31 * result + (postage != null ? postage.hashCode() : 0);
        result = 31 * result + (payPoints != null ? payPoints.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (rebatePoint != null ? rebatePoint.hashCode() : 0);
        result = 31 * result + (actualAmount != null ? actualAmount.hashCode() : 0);
        result = 31 * result + (actualPayPoints != null ? actualPayPoints.hashCode() : 0);
        result = 31 * result + (orderState != null ? orderState.hashCode() : 0);
        result = 31 * result + (buyerNotice != null ? buyerNotice.hashCode() : 0);
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (receiverAddress != null ? receiverAddress.hashCode() : 0);
        result = 31 * result + (postKind != null ? postKind.hashCode() : 0);
        result = 31 * result + (receiverTel != null ? receiverTel.hashCode() : 0);
        result = 31 * result + (receiverMobile != null ? receiverMobile.hashCode() : 0);
        result = 31 * result + (orderCreateTime != null ? orderCreateTime.hashCode() : 0);
        result = 31 * result + (orderPayTime != null ? orderPayTime.hashCode() : 0);
        result = 31 * result + (goodsTitle != null ? goodsTitle.hashCode() : 0);
        result = 31 * result + (goodsKind != null ? goodsKind.hashCode() : 0);
        result = 31 * result + (logisticsNumber != null ? logisticsNumber.hashCode() : 0);
        result = 31 * result + (logisticsCompany != null ? logisticsCompany.hashCode() : 0);
        result = 31 * result + (orderRemark != null ? orderRemark.hashCode() : 0);
        result = 31 * result + (goodsNumber != null ? goodsNumber.hashCode() : 0);
        result = 31 * result + (shopId != null ? shopId.hashCode() : 0);
        result = 31 * result + (shopName != null ? shopName.hashCode() : 0);
        result = 31 * result + (orderCloseReason != null ? orderCloseReason.hashCode() : 0);
        result = 31 * result + (solderFee != null ? solderFee.hashCode() : 0);
        result = 31 * result + (buyerFee != null ? buyerFee.hashCode() : 0);
        result = 31 * result + (invoiceTitle != null ? invoiceTitle.hashCode() : 0);
        result = 31 * result + (isMobileOrder != null ? isMobileOrder.hashCode() : 0);
        result = 31 * result + (phaseOrderInfo != null ? phaseOrderInfo.hashCode() : 0);
        result = 31 * result + (privilegeOrderId != null ? privilegeOrderId.hashCode() : 0);
        result = 31 * result + (isTransferAgreementPhoto != null ? isTransferAgreementPhoto.hashCode() : 0);
        result = 31 * result + (isTransferReceipt != null ? isTransferReceipt.hashCode() : 0);
        result = 31 * result + (isPayByAnother != null ? isPayByAnother.hashCode() : 0);
        result = 31 * result + (earnestRanking != null ? earnestRanking.hashCode() : 0);
        result = 31 * result + (skuChanged != null ? skuChanged.hashCode() : 0);
        result = 31 * result + (receiverAddressChanged != null ? receiverAddressChanged.hashCode() : 0);
        result = 31 * result + (errorInfo != null ? errorInfo.hashCode() : 0);
        result = 31 * result + (tmallCardsDeduction != null ? tmallCardsDeduction.hashCode() : 0);
        result = 31 * result + (pointDedution != null ? pointDedution.hashCode() : 0);
        result = 31 * result + (isO2OTrade != null ? isO2OTrade.hashCode() : 0);
        return result;
    }
}
