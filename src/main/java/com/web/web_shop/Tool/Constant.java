package com.web.web_shop.Tool;

/**
 * Constant
 */
public final  class Constant {
    public interface Code {
        int OK = 200;
        int NG = 404;
    }


    public interface UserType {
        int NOT_USER = 0;
        int USER = 1;
        int SELLER = 2;
        int ADMIN = 3;
    }

    public interface RecordStatus {
        int EXIST = 0;
        int DELETED = 1;
    }

    public interface CommodityStatus {
        int ON_SALE = 1;
        int OUT_OF_STOCK = 2;
        int OFF_SALE = 3;
    }

    public interface Limitation {
        int COMMODITY_NAME_LENGTH = 128;
        int COMMODITY_DESCRIPTION_LENGTH = 255;
        int USERNAME_LENGTH = 16;
        int PASSWORD_LENGTH = 16;
        int SHOPNAME_LENGTH = 16;
        int MAIL_LENGTH = 32;

        int CommodityStatus_LENGTH = 3;
    }

}
