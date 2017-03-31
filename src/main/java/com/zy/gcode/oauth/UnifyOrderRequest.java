package com.zy.gcode.oauth;

import com.zy.gcode.service.pay.WxXmlParser;
import com.zy.gcode.utils.*;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by admin5 on 17/3/31.
 */
public class UnifyOrderRequest extends AbstractOAuthRequest<Map>{
   /* 公众账号ID	appid	是	String(32)	wxd678efh567hg6787	微信支付分配的公众账号ID（企业号corpid即为此appId）
    商户号	mchId	是	String(32)	1230000109	微信支付分配的商户号
    设备号	device_info	否	String(32)	013467007045764	自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
    随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，长度要求在32位以内。推荐随机数生成算法
    签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	通过签名算法计算得出的签名值，详见签名生成算法
    签名类型	sign_type	否	String(32)	HMAC-SHA256	签名类型，默认为MD5，支持HMAC-SHA256和MD5。
    商品描述	body	是	String(128)	腾讯充值中心-QQ会员充值
    商品简单描述，该字段请按照规范传递，具体请见参数规定
    商品详情	detail	否	String(6000)	 	单品优惠字段(暂未上线)
    附加数据	attach	否	String(127)	深圳分店	附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
    商户订单号	out_trade_no	是	String(32)	20150806125346	商户系统内部订单号，要求32个字符内、且在同一个商户号下唯一。 详见商户订单号
    标价币种	fee_type	否	String(16)	CNY	符合ISO 4217标准的三位字母代码，默认人民币：CNY，详细列表请参见货币类型
    标价金额	total_fee	是	Int	88	订单总金额，单位为分，详见支付金额
    终端IP	spbill_create_ip	是	String(16)	123.12.12.123	APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
    交易起始时间	time_start	否	String(14)	20091225091010	订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
    交易结束时间	time_expire	否	String(14)	20091227091010
    订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则
    注意：最短失效时间间隔必须大于5分钟
    商品标记	goods_tag	否	String(32)	WXG	商品标记，使用代金券或立减优惠功能时需要的参数，说明详见代金券或立减优惠
    通知地址	notify_url	是	String(256)	http://www.weixin.qq.com/wxpay/pay.php	异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
    交易类型	trade_type	是	String(16)	JSAPI	取值如下：JSAPI，NATIVE，APP等，说明详见参数规定
    商品ID	product_id	否	String(32)	12235413214070356458058	trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
    指定支付方式	limit_pay	否	String(32)	no_credit	上传此参数no_credit--可限制用户不能使用信用卡支付
    用户标识	openid	否	String(128)	oUpF8uMuAJO_M2pxb1Q9zNjWeS6o	trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。openid如何获取，可参考【获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换
    */

   private String path;
   public void init(String appid,String mchId,String body,int totalFee,String spbillCreateIp,String notifyUrl,String tradeType,String path){
        setAppid(appid);
        setMchId(mchId);
        setOutTradeNo(UniqueStringGenerator.wxbillno(mchId));
        setBody(body);
        setTotalFee(totalFee);
        setSpbillCreateIp(spbillCreateIp);
        setNotifyUrl(notifyUrl);
        setTradeType(tradeType);
        this.path = path;
        this.init();
        this.sign();
   }

    public static final String APPID="appid";
    public static final String MCH_ID="mchId";
    public static final String DEVICE_INFO="device_info";
    public static final String NONCE_STR="nonce_str";
    public static final String SIGN="sign";
    public static final String SIGN_TYPE="sign_type";
    public static final String BODY="body";
    public static final String DETAIL="detail";
    public static final String ATTACH="attach";
    public static final String OUT_TRADE_NO="out_trade_no";
    public static final String FEE_TYPE="fee_type";
    public static final String TOTAL_FEE="total_fee";
    public static final String SPBILL_CREATE_IP="spbill_create_ip";
    public static final String TIME_START="time_start";
    public static final String TIME_EXPIRE="time_expire";
    public static final String GOODS_TAG="goods_tag";
    public static final String NOTIFY_URL="notify_url";
    public static final String TRADE_TYPE="trade_type";
    public static final String PRODUCT_ID="product_id";
    public static final String LIMIT_PAY="limit_pay";
    public static final String OPENID="openid";

    public UnifyOrderRequest(){
        super("https://api.mch.weixin.qq.com/pay/unifiedorder");
    }



    @Override
    public Map start(){
       HttpResponse response = HttpClientUtils.postSend(this.url, WxXmlParser.map2xml(map));
        if(response.getStatusLine().getStatusCode()!=200){
            try {
                Du.pl(MzUtils.inputStreamToString(response.getEntity().getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            return WxXmlParser.Xml2Map(MzUtils.inputStreamToString(response.getEntity().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map map = new HashMap(20);

    public String getAppid() {
        return (String)map.get(APPID);
    }

    public void setAppid(String appid) {
        map.put(APPID,appid);
    }

    public String getMchId() {
        return (String)map.get(MCH_ID);
    }

    public void setMchId(String mchId) {
        map.put(MCH_ID, mchId);
    }

    public String getDeviceInfo() {
        return (String)map.get(DEVICE_INFO);
    }

    public void setDeviceInfo(String deviceInfo) {
        map.put(DEVICE_INFO,deviceInfo);
    }

    public String getNonceStr() {
        return (String)map.get(NONCE_STR);
    }

    public void setNonceStr() {
        map.put(NONCE_STR,UniqueStringGenerator.getUniqueCode());
    }

    public String getSign() {
        return (String)map.get(SIGN);
    }

    public void sign(){
       Set<String> set =  map.keySet();
       Object[] keys = set.toArray();
       Arrays.sort(keys);
       StringBuilder builder = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            builder.append(keys[i].toString()).append("=")
                    .append(map.get(keys[i])).append("&");
        }
        map.put(SIGN, UniqueStringGenerator.getMd5(builder.substring(0,builder.length())));
    }

    private void init(){
        setSignType();
        setFeeType();
    }

    public String getSignType() {
        return (String)map.get(SIGN_TYPE);
    }

    private void setSignType() {
        map.put(SIGN_TYPE,"md5");
    }

    public String getBody() {
        return (String)map.get(BODY);
    }

    public void setBody(String body) {
        map.put(BODY,body);
    }

    public String getDetail() {
        return (String)map.get(DETAIL);
    }

    public void setDetail(String detail) {
        map.put(DETAIL,detail);
    }

    public String getAttach() {
        return (String)map.get(ATTACH);
    }

    public void setAttach(String attach) {
        map.put(ATTACH,attach);
    }

    public String getOutTradeNo() {
        return (String)map.get(OUT_TRADE_NO);
    }

    public void setOutTradeNo(String out_trade_no) {
           map.put(OUT_TRADE_NO,out_trade_no);
    }

    public String getFeeType() {
        return (String)map.get(FEE_TYPE);
    }

    public void setFeeType() {
        map.put(FEE_TYPE,"CNY");
    }

    public int getTotalFee() {
        return (int)map.get(TOTAL_FEE);
    }

    public void setTotalFee(int totalFee) {
        map.put(TOTAL_FEE,totalFee);
    }

    public String getSpbillCreateIp() {
        return (String)map.get(SPBILL_CREATE_IP);
    }

    public void setSpbillCreateIp(String spbill_create_ip) {
        map.put(SPBILL_CREATE_IP,spbill_create_ip);
    }

    public Date getTimeStart() {
        try {
            return DateUtils.parse((String)map.get(TIME_START),"yyyyMMddHHmmss");
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public void setTimeStart(long timeStart) {
        map.put(TIME_START,String.valueOf(timeStart));
    }

    public Date getTimeExpire() {
        try {
            return DateUtils.parse((String)map.get(TIME_EXPIRE),"yyyyMMddHHmmss");
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public void setTimeExpire(long timeExpire) {
        map.put(TIME_EXPIRE,String.valueOf(timeExpire));
    }

    public String getGoodsTag() {
        return (String)map.get(GOODS_TAG);
    }

    public void setGoodsTag(String goodsTag) {
        map.put(GOODS_TAG,goodsTag);
    }

    public String getNotifyUrl() {
        return (String)map.get(NOTIFY_URL);
    }

    public void setNotifyUrl(String notifyUrl) {
        map.put(NOTIFY_URL,notifyUrl);
    }

    public String getTradeType() {
        return (String)map.get(TRADE_TYPE);
    }

    public void setTradeType(String tradeType) {
        map.put(TRADE_TYPE,tradeType);
    }

    public String getProductId() {
        return (String)map.get(PRODUCT_ID);
    }

    public void setProductId(String productId) {
        map.put(PRODUCT_ID,productId);
    }

    public String getLimitPay() {
        return (String)map.get(LIMIT_PAY);
    }

    public void setLimitPay(String limitPay) {
        map.put(LIMIT_PAY,limitPay);
    }

    public String getOpenid() {
        return (String)map.get(OPENID);
    }

    public void setOpenid(String openid) {
        map.put(OPENID,openid);
    }
}
