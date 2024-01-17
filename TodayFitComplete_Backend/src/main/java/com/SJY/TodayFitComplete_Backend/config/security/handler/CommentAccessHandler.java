package com.SJY.TodayFitComplete_Backend.config.security.handler;

import com.SJY.TodayFitComplete_Backend.entity.member.type.RoleType;
import com.SJY.TodayFitComplete_Backend.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentAccessHandler extends AccessHandler {

    private final CommentRepository commentRepository;
    private List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return commentRepository.findById(id)
                .map(comment -> comment.getMember())
                .map(member -> member.getId())
                .filter(memberId -> memberId.equals(AuthHandler.extractMemberId()))
                .isPresent();
    }
}