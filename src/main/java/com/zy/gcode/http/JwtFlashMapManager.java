package com.zy.gcode.http;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.AbstractFlashMapManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by admin5 on 17/3/22.
 */
public class JwtFlashMapManager extends AbstractFlashMapManager {



    @Override
    protected List<FlashMap> retrieveFlashMaps(HttpServletRequest request) {

        return null;
    }

    @Override
    protected void updateFlashMaps(List<FlashMap> flashMaps, HttpServletRequest request, HttpServletResponse response) {

    }
}
