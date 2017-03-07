package controller;

import org.junit.Test;

import java.util.Date;

/**
 * Created by admin5 on 17/2/27.
 */
public class OperatorControllerTest extends ControllerTest {


    @Test
    public void register() throws Exception {
        jsonPost("/proxy/list", "currentPageIndex", 1, "serverType", 1);
        System.out.println(new Date(1488003477000L));
    }
}
