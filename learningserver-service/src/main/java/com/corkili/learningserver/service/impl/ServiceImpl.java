package com.corkili.learningserver.service.impl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.JoinColumn;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.bo.BusinessObject;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.po.PersistObject;
import com.corkili.learningserver.service.Service;

public abstract class ServiceImpl<BO extends BusinessObject, PO extends PersistObject> implements Service<BO, PO> {

    private static final String CACHE_NAME = "memoryCache";

    @Override
    public final Optional<BO> create(BO bo) {
        return Optional.ofNullable(saveOrUpdate(bo, false, entityName()));
    }

    @Override
    public final Optional<BO> retrieve(Long id) {
        return Optional.ofNullable(retrieve(id, entityName()));
    }

    @Override
    public final Optional<BO> update(BO bo) {
        return Optional.ofNullable(saveOrUpdate(bo, true, entityName()));
    }

    @Override
    public final boolean delete(Long id) {
        return delete(id, entityName());
    }

    @CachePut(cacheNames = CACHE_NAME, key = "#entityName + #result.id", unless = "#result == null or #result.id == null")
    public BO saveOrUpdate(BO bo, boolean isUpdate, String entityName) {
        Optional<PO> poOptional = bo2po(bo);
        if (!poOptional.isPresent()) {
            logger().error("transfer {} bo to {} po failed.", entityName, entityName);
            return null;
        }
        PO po = poOptional.get();
        if (!checkPO(po, isUpdate)) {
            logger().error("validate {} po failed", entityName);
            return null;
        }
        if (isUpdate) {
            po.setUpdateTime(new Date());
        } else {
            po.setId(null);
        }
        try {
            po = repository().saveAndFlush(po);
        } catch (Exception e) {
            if (isUpdate) {
                logger().error("exception occurs when update {} po - {}", entityName, ServiceUtils.stringifyError(e));
            } else {
                logger().error("exception occurs when create {} po - {}", entityName, ServiceUtils.stringifyError(e));
            }
            return null;
        }
        poOptional = repository().findById(po.getId());
        if (!poOptional.isPresent()) {
            logger().error("retrieveUserByPhoneAndUserType {} po [{}] from db failed.", entityName, po.getId());
            return null;
        }
        po = poOptional.get();
        Optional<BO> boOptional = po2bo(po);
        if (!boOptional.isPresent()) {
            logger().error("transfer {} po [{}] to bo failed", entityName, po.getId());
            return null;
        }
        return boOptional.get();
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "#entityName + #result.id", unless = "#result == null or #result.id == null")
    public BO retrieve(Long id, String entityName) {
        if (id == null) {
            logger().error("id of {} is null", entityName);
        return null;
    }
        Optional<PO> poOptional = repository().findById(id);
        if (!poOptional.isPresent()) {
            logger().error("{} [{}] not exist in db", entityName, id);
            return null;
        }
        return po2bo(poOptional.get()).orElse(null);
    }

    @CacheEvict(cacheNames = CACHE_NAME, key = "#entityName + #id", condition = "#result == true")
    public boolean delete(Long id, String entityName) {
        if (id == null) {
            logger().error("id of {} is null", entityName);
            return false;
        }
        try {
            repository().deleteById(id);
        } catch (Exception e) {
            logger().error("exception occurs when delete {} from db - {}", entityName, ServiceUtils.stringifyError(e));
            return false;
        }
        return true;
    }

    @Override
    public Optional<BO> po2bo(PO po) {
        if (po == null) {
            logger().error("{} po [] is null", entityName());
            return Optional.empty();
        }
        BO bo = newBusinessObject();
        String id = po.getId() == null ? "" : po.getId().toString();
        try {
            for (Field fieldOfPO : po.getClass().getDeclaredFields()) {
                try {
                    if (Arrays.asList(fieldOfPO.getType().getInterfaces()).contains(PersistObject.class)) {
                        Field fieldOfBO = bo.getClass().getDeclaredField(fieldOfPO.getName() + "Id");
                        if (!fieldOfBO.getType().equals(Long.class)) {
                            logger().error("{} of {} bo [{}] is not a Long", fieldOfBO.getName(), entityName(), id);
                            return Optional.empty();
                        }
                        try {
                            fieldOfPO.setAccessible(true);
                            fieldOfBO.setAccessible(true);
                            Object valueOfPO = fieldOfPO.get(po);
                            if (valueOfPO == null) {
                                fieldOfBO.set(bo, null);
                            } else {
                                fieldOfBO.set(bo, ((PersistObject) valueOfPO).getId());
                            }
                        } finally {
                            fieldOfPO.setAccessible(false);
                            fieldOfBO.setAccessible(false);
                        }
                    } else {
                        Field fieldOfBO = bo.getClass().getDeclaredField(fieldOfPO.getName());
                        if (fieldOfBO.getType().equals(fieldOfPO.getType())) { // otherwise, processed by subclass
                            try {
                                fieldOfPO.setAccessible(true);
                                fieldOfBO.setAccessible(true);
                                fieldOfBO.set(bo, fieldOfPO.get(po));
                            } finally {
                                fieldOfPO.setAccessible(false);
                                fieldOfBO.setAccessible(false);
                            }
                        }
                    }
                } catch (NoSuchFieldException e) {
                    logger().warn("exception occurs when transfer {} po [{}] to {} bo, NoSuchFieldException: {}",
                            entityName(), id, entityName(), e.getMessage());
                }
            }
        } catch (Exception e) {
            logger().error("exception occurs when transfer {} po [{}] to {} bo - {}",
                    entityName(), id, entityName(), ServiceUtils.stringifyError(e));
            return Optional.empty();
        }
        return Optional.of(bo);
    }

    @Override
    public Optional<PO> bo2po(BO bo) {
        if (bo == null) {
            logger().error("{} bo [] is null", entityName());
            return Optional.empty();
        }
        PO po = newPersistObject();
        String id = bo.getId() == null ? "" : bo.getId().toString();
        try {
            for (Field fieldOfBO : bo.getClass().getDeclaredFields()) {
                try {
                    if (fieldOfBO.getName().endsWith("Id") && fieldOfBO.getType().equals(Long.class)) {
                        Field fieldOfPO = po.getClass().getDeclaredField(
                                fieldOfBO.getName().substring(0, fieldOfBO.getName().length() - 2));
                        if (!Arrays.asList(fieldOfPO.getType().getInterfaces()).contains(PersistObject.class)) {
                            logger().error("{} of {} po [{}] is not a PersistObject", fieldOfPO.getName(), entityName(), id);
                            return Optional.empty();
                        }
                        try {
                            fieldOfPO.setAccessible(true);
                            fieldOfBO.setAccessible(true);
                            Long valueOfBO = (Long) fieldOfBO.get(bo);
                            if (valueOfBO == null) {
                                fieldOfPO.set(po, null);
                            } else {
                                Object obj = fieldOfPO.getType().getConstructor().newInstance();
                                ((PersistObject) obj).setId(valueOfBO);
                                fieldOfPO.set(po, obj);
                            }
                        } finally {
                            fieldOfPO.setAccessible(false);
                            fieldOfBO.setAccessible(false);
                        }
                    } else {
                        Field fieldOfPO = po.getClass().getDeclaredField(fieldOfBO.getName());
                        if (fieldOfBO.getType().equals(fieldOfPO.getType())) { // otherwise, processed by subclass
                            try {
                                fieldOfPO.setAccessible(true);
                                fieldOfBO.setAccessible(true);
                                fieldOfPO.set(po, fieldOfBO.get(bo));
                            } finally {
                                fieldOfPO.setAccessible(false);
                                fieldOfBO.setAccessible(false);
                            }
                        }
                    }
                } catch (NoSuchFieldException e) {
                    logger().warn("exception occurs when transfer {} bo [{}] to {} po, NoSuchFieldException: {}",
                            entityName(), id, entityName(), e.getMessage());
                }
            }
        } catch (Exception e) {
            logger().error("exception occurs when transfer {} bo [{}] to {} po - {}", entityName(), id, entityName(),
                    ServiceUtils.stringifyError(e));
            return Optional.empty();
        }
        return Optional.of(po);
    }

    @Override
    public boolean checkPO(PO po, boolean isUpdate) {
        if (po == null) {
            logger().error("{} po [] is null", entityName());
            return false;
        }
        String id = po.getId() == null ? "" : po.getId().toString();
        if (isUpdate) {
            if (po.getId() == null) {
                logger().error("id of {} po [] is null when update", entityName());
                return false;
            }
            if (po.getUpdateTime() == null) {
                logger().error("updateTime of {} po [{}] is null when update", entityName(), id);
                return false;
            }
        }
        for (Field field : po.getClass().getDeclaredFields()) {
            String name = field.getName();
            if (name.equals("id") || name.equals("updateTime") || name.equals("createTime") || name.equals("deleted")) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object value = field.get(po);
                if (field.getType().equals(String.class)) {
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }
                    Column column = field.getAnnotation(Column.class);
                    String str = (String) value;
                    if (!column.nullable()) {
                        if (StringUtils.isBlank(str)) {
                            logger().error("{} of {} po [{}] is empty", name, entityName(), id);
                            return false;
                        }
                        if (str.length() > column.length()) {
                            logger().error("{} of {} po [{}] exceed {}", name, entityName(), id, column.length());
                            return false;
                        }
                    } else {
                        if (StringUtils.isNotBlank(str) && str.length() > column.length()) {
                            logger().error("{} of {} po [{}] exceed {}", name, entityName(), id, column.length());
                            return false;
                        }
                    }
                } else if (field.getType().equals(Integer.class)) {
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }
                    Column column = field.getAnnotation(Column.class);
                    Integer i = (Integer) value;
                    if (!column.nullable()) {
                        if (i == null) {
                            logger().error("{} of {} po [{}] is empty", name, entityName(), id);
                            return false;
                        }
                    }
                    if (field.isAnnotationPresent(Range.class)) {
                        Range range = field.getAnnotation(Range.class);
                        if (i != null && (i < range.min() || i > range.max())) {
                            logger().error("{} of {} po [{}] out of range [{}, {}]", name, entityName(), id, range.min(), range.max());
                            return false;
                        }
                    }
                } else if (field.getType().equals(Double.class)) {
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }
                    Column column = field.getAnnotation(Column.class);
                    Double d = (Double) value;
                    if (!column.nullable()) {
                        if (d == null) {
                            logger().error("{} of {} po [{}] is empty", name, entityName(), id);
                            return false;
                        }
                    }
                    if (field.isAnnotationPresent(Range.class)) {
                        Range range = field.getAnnotation(Range.class);
                        if (d != null && (d < range.min() || d > range.max())) {
                            logger().error("{} of {} po [{}] out of range [{}, {}]", name, entityName(), id, range.min(), range.max());
                            return false;
                        }
                    }
                } else if (Arrays.asList(field.getType().getInterfaces()).contains(PersistObject.class)) {
                    if (!field.isAnnotationPresent(JoinColumn.class)) {
                        continue;
                    }
                    JoinColumn column = field.getAnnotation(JoinColumn.class);
                    PersistObject persistObject = (PersistObject) value;
                    if (!column.nullable()) {
                        if (ServiceUtils.isEntityReferenceNull(persistObject)) {
                            logger().error("{} of {} po [{}] or id of {} of {} po [{}] is null",
                                    name, entityName(), id, name, entityName(), id);
                            return false;
                        }
                    } else {
                        if (persistObject != null && ServiceUtils.isEntityReferenceNull(persistObject)) {
                            logger().error("id of {} of {} po [{}] is null", name, entityName(), id);
                            return false;
                        }
                    }
                } else {
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }
                    Column column = field.getAnnotation(Column.class);
                    if (!column.nullable() && value == null) {
                        logger().error("{} of {} po [{}] is null", name, entityName(), id);
                        return false;
                    }
                }
            } catch (Exception e) {
                logger().error("exception occurs when check {} po [{}]", entityName(), id);
                return false;
            } finally {
                field.setAccessible(false);
            }
        }
        return true;
    }
    
    protected ServiceResult recordErrorAndCreateFailResultWithMessage(String msg, Object... args) {
        logger().error(msg, args);
        return ServiceResult.failResultWithMessage(ServiceUtils.format(msg, args));
    }

    protected ServiceResult recordWarnAndCreateSuccessResultWithMessage(String msg, Object... args) {
        logger().warn(msg, args);
        return ServiceResult.successResultWithMesage(ServiceUtils.format(msg, args));
    }

    protected abstract JpaRepository<PO, Long> repository();

    protected abstract String entityName();

    protected abstract BO newBusinessObject();

    protected abstract PO newPersistObject();
    
    protected abstract Logger logger();

}
