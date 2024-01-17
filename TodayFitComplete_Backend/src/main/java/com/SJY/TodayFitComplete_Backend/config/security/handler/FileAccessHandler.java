package com.SJY.TodayFitComplete_Backend.config.security.handler;

import com.SJY.TodayFitComplete_Backend.entity.member.type.RoleType;
import com.SJY.TodayFitComplete_Backend.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileAccessHandler extends AccessHandler {

    private final FileRepository fileRepository;
    private List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return fileRepository.findById(id)
                .map(file -> file.getBoard())
                .map(board -> board.getMember())
                .map(member -> member.getId())
                .filter(memberId -> memberId.equals(AuthHandler.extractMemberId()))
                .isPresent();
    }
}