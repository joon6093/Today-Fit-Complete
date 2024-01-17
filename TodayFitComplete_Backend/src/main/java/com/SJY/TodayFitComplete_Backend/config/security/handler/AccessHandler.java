package com.SJY.TodayFitComplete_Backend.config.security.handler;

import com.SJY.TodayFitComplete_Backend.entity.member.type.RoleType;
import java.util.List;

public abstract class AccessHandler {
    public final boolean check(Long id) {
        return hasRole(getRoleTypes()) || isResourceOwner(id);
    }

    abstract protected List<RoleType> getRoleTypes();
    abstract protected boolean isResourceOwner(Long id);

    private boolean hasRole(List<RoleType> roleTypes) {
        return AuthHandler.extractMemberRoles().containsAll(roleTypes);
    }
}