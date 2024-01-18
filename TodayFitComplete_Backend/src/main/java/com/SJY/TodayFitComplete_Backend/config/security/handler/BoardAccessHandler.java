package com.SJY.TodayFitComplete_Backend.config.security.handler;

import com.SJY.TodayFitComplete_Backend.entity.member.type.RoleType;
import com.SJY.TodayFitComplete_Backend.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardAccessHandler extends AccessHandler {

    private final BoardRepository boardRepository;
    private List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return boardRepository.findById(id)
                .map(board -> board.getMember())
                .map(member -> member.getId())
                .filter(memberId -> memberId.equals(AuthHandler.extractMemberId()))
                .isPresent();
    }
}