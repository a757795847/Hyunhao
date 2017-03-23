package com.zy.gcode.security;

import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.subject.Subject;

/**
 * Created by admin5 on 17/3/23.
 */
public class UnSuportsSubjectDAO implements SubjectDAO {
    @Override
    public Subject save(Subject subject) {
        return null;
    }

    @Override
    public void delete(Subject subject) {

    }
}
