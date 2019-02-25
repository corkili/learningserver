package com.corkili.learningserver.service;

import java.util.Optional;

import com.corkili.learningserver.bo.BusinessObject;
import com.corkili.learningserver.po.PersistObject;

public interface Service<BO extends BusinessObject, PO extends PersistObject> {

    Optional<BO> create(BO course);

    Optional<BO> retrieve(Long id);

    Optional<BO> update(BO course);

    boolean delete(Long id);

    Optional<BO> po2bo(PO po);

    Optional<PO> bo2po(BO bo);

    boolean checkPO(PO po, boolean isUpdate);

}
