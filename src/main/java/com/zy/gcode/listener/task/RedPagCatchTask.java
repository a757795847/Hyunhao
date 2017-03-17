package com.zy.gcode.listener.task;

import com.zy.gcode.controller.delegate.BatchRe;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.service.IPayService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.HttpClientUtils;
import com.zy.gcode.utils.JsonUtils;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.TimerTask;
import java.util.TreeMap;

/**
 * Created by admin5 on 17/2/13.
 */
public class RedPagCatchTask extends TimerTask {
    ApplicationContext applicationContext;

    public RedPagCatchTask(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    IPayService payService = applicationContext.getBean(IPayService.class);

    @Override
    public void run() {
        BatchRe<RedStatus> batchRe = (BatchRe) payService.circularGetPayInfo();
        Map map = new TreeMap();
        map.put("list", batchRe.getTlist());
        map.put("error", batchRe.getErrorList());
        HttpClientUtils.postSend(Constants.properties.getProperty("callback.redinfo"), JsonUtils.objAsString(map));

    }

    public static class TaskTest extends TimerTask {
        int i = 0;

        @Override
        public void run() {
            System.out.println(i++);
        }
    }

    public static void main(String[] args) throws Exception {
 /*     System.out.println( HttpClientUtils.mapPostSend("https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=Rbnju32Xzd6wH6yMp4JRjPVmUEbqCdmDZ2-F95DVMFuz8QsR7FE1Pl_12oP6Q3dNTSHxexCn7Ur-uUL-MGgz5a-BAdko2_SmjAR5es14dVCoa6O4UVBA6E8TsROuCFJMOMJjAEAXVS","{\n" +
               "           \"template_id_short\":\"OPENTM200396847\"\n" +
               "       }"));*/

        //OfBlXNDZih36iz6s5MiqpBhZP4S68z4sAPaXzmN9dno
     /*   System.out.println( HttpClientUtils.mapPostSend("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=Rbnju32Xzd6wH6yMp4JRjPVmUEbqCdmDZ2-F95DVMFuz8QsR7FE1Pl_12oP6Q3dNTSHxexCn7Ur-uUL-MGgz5a-BAdko2_SmjAR5es14dVCoa6O4UVBA6E8TsROuCFJMOMJjAEAXVS",
                "{\n" +
                        "           \"touser\":\"ooBfdwNcoMaol2CF0zlcRUYkYE_Q\",\n" +
                        "           \"template_id\":\"OfBlXNDZih36iz6s5MiqpBhZP4S68z4sAPaXzmN9dno\",\n" +
                        "           \"url\":\"http://open.izhuiyou.com/\",            \n" +
                        "           \"data\":{\n" +
                        "                   \"first\": {\n" +
                        "                       \"value\":\"恭喜你购买成功！\",\n" +
                        "                       \"color\":\"#173177\"\n" +
                        "                   },\n" +
                        "                   \"keyword1\":{\n" +
                        "                       \"value\":\"巧克力\",\n" +
                        "                       \"color\":\"#173177\"\n" +
                        "                   },\n" +
                        "                   \"keyword2\": {\n" +
                        "                       \"value\":\"39.8元\",\n" +
                        "                       \"color\":\"#173177\"\n" +
                        "                   },\n" +
                        "                   \"keyword3\": {\n" +
                        "                       \"value\":\"2014年9月22日\",\n" +
                        "                       \"color\":\"#173177\"\n" +
                        "                   },\n" +
                        "                   \"remark\":{\n" +
                        "                       \"value\":\"欢迎再次购买！\",\n" +
                        "                       \"color\":\"#173177\"\n" +
                        "                   }\n" +
                        "           }\n" +
                        "       }"));*/
    System.out.println( HttpClientUtils.mapPostSend("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=wfV3OYs0FeJgdCwWxOcjw40qwdTd63LvsYeLH6RriixZgPLx5G7b80YpxSJQbD9fI6t22CmmyOkJJq1CGzVQnsNQm5aGFtzHo5s9cR4D7znfwkYYbo-GiTTWmNsu4Rq_GQKcACAMWQ",
             "{\n" +
                     "     \"button\":[\n" +
                     "     {\t\n" +
                     "          \"type\":\"view\",\n" +
                     "          \"name\":\"微信红包\",\n" +
                     "          \"key\":\"red\",\"url\":\"http://open.izhuiyou.com/wechat/view/home/ge111\"" +
                     "      }]}"));  //System.out.println( HttpClientUtils.mapGetSend("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=Y3jYDmFWVka5s9meiJxZLmwQYdGcMfNWLxcCuK-f2WkGsUEGFEmJJ20XbS-DZ9Bzdb5T4O3RZltJ_SszSddGR7WARTAUb1dEdTVoo8HLJ4fkzVkXKMft1u2BVD4hpl0EFWQaAEAWNH"));
    }
}
