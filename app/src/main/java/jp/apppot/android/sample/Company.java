package jp.apppot.android.sample;

import jp.co.ncdc.apppot.stew.APEntityManager;
import jp.co.ncdc.apppot.stew.APResponseSelectHandler;
import jp.co.ncdc.apppot.stew.dto.APObject;

public class Company extends APObject {

    public int companyCode;
    public String companyName;

    public Company getByObjectId(String objectId) {
        return (Company) selectByObjectId(objectId);
    }

    public void getList(APResponseSelectHandler handler) {
        select().findList(handler);
    }

    public void save() {
        APEntityManager em = APEntityManager.getInstance();
        em.saveObject(this);
    }
}
