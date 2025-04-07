package com.yang.portal.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yang.portal.core.CoreConstant;
import com.yang.portal.core.exception.InternalServerErrorException;
import com.yang.portal.core.page.PagedList;
import com.yang.portal.core.utils.ErrorMapUtil;
import com.yang.portal.core.utils.SqlOperateUtil;
import com.yang.portal.core.utils.TimeUtil;
import com.yang.portal.user.entity.Dept;
import com.yang.portal.user.mapper.DeptMapper;
import com.yang.portal.user.mapper.UserMapper;
import com.yang.portal.user.service.DeptService;
import com.yang.portal.user.service.impl.deptService.DeptNameCheckVo;
import com.yang.portal.user.service.impl.deptService.DeptSelectVo;
import com.yang.portal.user.service.impl.deptService.DeptTreeDto;
import com.yang.portal.user.service.impl.deptService.DeptVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String UPDATE_ANCESTRAL_SQL = "update DEPT set ancestral = ?,UPDATED_TIME = ? where id = ?";
    private static final Long ROOT_DEPT_ID = 0L;

    @Override
    public void create(DeptVo deptVo) {
        if (!checkDeptName(deptVo.getName(), deptVo.getParentId(), null)) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("部门名称重复"));
        }
        if (deptVo.getParentId() == 0) {
            Dept dept = new Dept()
                    .setName(deptVo.getName())
                    .setParentId(ROOT_DEPT_ID)
                    .setAncestral(String.valueOf(ROOT_DEPT_ID))
                    .setStatus(deptVo.getStatus());
            deptMapper.insert(dept);
            return;
        }
        insertDept(deptVo);
    }

    private void insertDept(DeptVo deptVo) {
        Dept parentDept = getDeptById(deptVo.getParentId());
        if (ObjectUtils.isEmpty(parentDept)) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("父部门不存在"));
        }
        if (parentDept.getStatus() && !deptVo.getStatus()) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("父部门部门状态不可用"));
        }
        Dept dept = new Dept()
                .setParentId(parentDept.getId())
                .setName(deptVo.getName())
                .setAncestral(String.format("%s,%d", parentDept.getAncestral(), deptVo.getParentId()))
                .setStatus(deptVo.getStatus());
        deptMapper.insert(dept);
    }

    @Transactional
    @Override
    public void update(DeptVo deptVo, Long id) {
        Dept dept = getDeptById(id);
        checkDeptUpdateInfo(deptVo, dept, id);
        // 更新 Ancestral
        if (deptVo.getParentId() == 0) {
            updateRootDeptAncestral(deptVo, dept, id);
        } else {
            updateDeptAncestral(deptVo, dept, id);
        }
        updateDeptDetail(deptVo, dept);
    }

    private void updateRootDeptAncestral(DeptVo deptVo, Dept dept, Long id) {
        if (dept.getParentId() != 0L) {
            String newAncestral = "0";
            String oldAncestral = dept.getAncestral();
            updateAncestral(oldAncestral, newAncestral, id);
            dept.setAncestral(newAncestral);
        }
    }

    private void updateDeptAncestral(DeptVo deptVo, Dept dept, Long id) {
        // 如果父部门变更其子部门也需要更新
        if (dept.getParentId() != deptVo.getParentId()) {
            Dept parentDept = getDeptById(deptVo.getParentId());
            String newAncestral = String.format("%s,%d", parentDept.getAncestral(), deptVo.getParentId());
            String oldAncestral = dept.getAncestral();
            updateAncestral(oldAncestral, newAncestral, id);
            dept.setAncestral(newAncestral);
        }
        // 子部门打开需要父部门都打开
        if (deptVo.getStatus() != dept.getStatus() && deptVo.getStatus() == Boolean.FALSE) {
            updateParentStatus(dept.getAncestral());
        }
    }

    private void checkDeptUpdateInfo(DeptVo deptVo, Dept dept, Long id) {
        //同级部门不能重名
        if (!checkDeptName(deptVo.getName(), deptVo.getParentId(), id)) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("部门名称重复"));
        }
        // 当部门还有成员不能关闭
        if (deptVo.getStatus() != dept.getStatus() && deptVo.getStatus() == Boolean.TRUE && userMapper.getUsersByDept(id) > 0) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("当前部门还有用户"));
        }
        // 当部门需要关闭时，其子部门需要全部关闭
        if (deptVo.getStatus() != dept.getStatus() && deptVo.getStatus() == Boolean.TRUE && deptMapper.getChildOpenStatusCount(id) > 0) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("子部门存在未关闭部门"));
        }
    }

    /**
     * 检查部门名称是否重复
     * @param name
     * @param parentId
     * @param deptId
     * @return
     */
    private boolean checkDeptName(String name, Long parentId, Long deptId) {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dept::getName, name)
                .eq(Dept::getParentId, parentId)
                .eq(Dept::getUniqueKey, CoreConstant.DEFAULT_UNIQUE_KEY)
                .ne(ObjectUtils.isNotEmpty(deptId), Dept::getId, deptId);
        return ObjectUtils.isEmpty(deptMapper.selectOne(queryWrapper));
    }

    private Dept getDeptById(Long id) {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dept::getId, id)
                .eq(Dept::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED);
        return deptMapper.selectOne(queryWrapper);
    }


    private void updateAncestral(String oldAncestral, String newAncestral, Long id) {
        List<Dept> childDept = deptMapper.findChildDept(id);
        for (Dept dept : childDept) {
            dept.setAncestral(dept.getAncestral().replace(oldAncestral, newAncestral));
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
        jdbcTemplate.batchUpdate(UPDATE_ANCESTRAL_SQL, childDept, 1000, (ps, d) -> {
            ps.setString(1, d.getAncestral());
            ps.setString(2, TimeUtil.formatDate(zonedDateTime, TimeUtil.DEFAULT_FORMATTER));
            ps.setLong(3, d.getId());
        });
    }

    private void updateParentStatus(String ancestral) {
        List<String> parentList = Arrays.stream(ancestral.split(",")).collect(Collectors.toList());
        LambdaUpdateWrapper<Dept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Dept::getId, parentList)
                .set(Dept::getStatus, Boolean.TRUE)
                .set(Dept::getUpdatedTime, ZonedDateTime.now(ZoneId.systemDefault()));
        deptMapper.update(updateWrapper);
    }

    private void updateDeptDetail(DeptVo deptVo, Dept dept) {
        LambdaUpdateWrapper<Dept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Dept::getId, dept.getId())
                .set(Dept::getName, deptVo.getName())
                .set(Dept::getAncestral, dept.getAncestral())
                .set(Dept::getStatus, deptVo.getStatus())
                .set(Dept::getParentId, deptVo.getParentId())
                .set(Dept::getUpdatedTime, ZonedDateTime.now(ZoneId.systemDefault()));
        SqlOperateUtil.OperateSuccess(deptMapper.update(updateWrapper));
    }

    @Override
    public void delete(Long id) {
        if (userMapper.getUsersByDept(id) > 0) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("当前部门还有用户"));
        }
        if (deptMapper.getChildCount(id) > 0) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("当前部门还有子部门"));
        }
        deptMapper.uniqueKeyDelete(id);
    }

    @Override
    public List<DeptTreeDto> deptTreeContainRoot(Long id) {
        List<Dept> deptAndChild = deptMapper.getDeptAndChild(id);
        Map<Long, List<Dept>> parentIdDept = deptAndChild.stream().collect(Collectors.groupingBy(Dept::getParentId));
        List<DeptTreeDto> result = new ArrayList<>();
        Dept rootDept = null;
        // 对于根目录
        if (id == 0L) {
            rootDept = new Dept()
                    .setName("部门结构");
            rootDept.setId(0L);
        } else {
            rootDept = deptAndChild.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
        }
        if (ObjectUtils.isEmpty(rootDept)) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("当前部门ID不存在"));
        }
        DeptTreeDto deptTreeDto = new DeptTreeDto();
        deptTreeDto.setDept(rootDept);
        getChild(rootDept, parentIdDept, deptTreeDto);
        result.add(deptTreeDto);
        return result;
    }

    @Override
    public List<DeptTreeDto> deptTree(Long id) {
        List<Dept> deptAndChild = deptMapper.getDeptAndChild(id);
        Map<Long, List<Dept>> parentIdDept = deptAndChild.stream().collect(Collectors.groupingBy(Dept::getParentId));
        List<DeptTreeDto> result = new ArrayList<>();
        // 对于根目录
        if (id == 0L) {
            List<Dept> deptList = parentIdDept.get(0L);
            for (Dept rootDept : deptList) {
                DeptTreeDto deptTreeDto = new DeptTreeDto();
                deptTreeDto.setDept(rootDept);
                getChild(rootDept, parentIdDept, deptTreeDto);
                result.add(deptTreeDto);
            }
        } else {
            Dept rootDept = deptAndChild.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
            if (ObjectUtils.isEmpty(rootDept)) {
                throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("当前部门ID不存在"));
            }
            DeptTreeDto deptTreeDto = new DeptTreeDto();
            deptTreeDto.setDept(rootDept);
            getChild(rootDept, parentIdDept, deptTreeDto);
            result.add(deptTreeDto);
        }
        return result;
    }

    private void getChild(Dept rootDept, Map<Long, List<Dept>> parentIdDept, DeptTreeDto deptTreeDto) {
        List<Dept> childDeptList = parentIdDept.get(rootDept.getId());
        if (ObjectUtils.isNotEmpty(childDeptList)) {
            List<DeptTreeDto> childDeptTreeDtoList = new ArrayList<>();
            for (Dept dept : childDeptList) {
                DeptTreeDto deptTreeChildDto = new DeptTreeDto();
                deptTreeChildDto.setDept(dept);
                getChild(dept, parentIdDept, deptTreeChildDto);
                childDeptTreeDtoList.add(deptTreeChildDto);
            }
            deptTreeDto.setChildren(childDeptTreeDtoList);
        }
    }

    @Override
    public List<Dept> deptAndChild(Long id) {
        return deptMapper.getDeptAndChild(id);
    }

    @Override
    public PagedList<Dept> select(DeptSelectVo deptSelectVo) {
        List<Dept> result = deptMapper.select(
                deptSelectVo,
                deptSelectVo.getPagination().getOffset(),
                deptSelectVo.getPagination().getPageSize(),
                deptSelectVo.getPagination().getSorts()
        );
        Long count = deptMapper.count(deptSelectVo);
        return PagedList.<Dept>builder()
                .result(result)
                .count(count)
                .pageNum(deptSelectVo.getPagination().getPageNum())
                .pageSize(deptSelectVo.getPagination().getPageSize())
                .build();
    }

    @Override
    public Dept detail(Long id) {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dept::getId, id)
                .eq(Dept::getIsDeleted, false);
        return deptMapper.selectOne(queryWrapper);
    }

    @Override
    public Boolean nameExist(DeptNameCheckVo deptNameCheckVo) {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dept::getName, deptNameCheckVo.getName())
                .eq(Dept::getParentId, deptNameCheckVo.getParentId())
                .eq(Dept::getUniqueKey, 0L);
        return ObjectUtils.isNotEmpty(deptMapper.selectOne(queryWrapper));
    }
}
