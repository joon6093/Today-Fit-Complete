package com.SJY.TodayFitComplete_Backend.config.security.handler;

import com.SJY.TodayFitComplete_Backend.entity.member.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberAccessHandler extends AccessHandler {
    private List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return id.equals(AuthHandler.extractMemberId());
    }
}